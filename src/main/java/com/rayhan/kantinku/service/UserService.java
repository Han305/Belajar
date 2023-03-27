package com.rayhan.kantinku.service;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.rayhan.kantinku.dao.ResetPasswordDao;
import com.rayhan.kantinku.dao.UserDao;
import com.rayhan.kantinku.dao.UserPasswordDao;
import com.rayhan.kantinku.dto.RegisterFormDto;
import com.rayhan.kantinku.entity.ResetPassword;
import com.rayhan.kantinku.entity.Role;
import com.rayhan.kantinku.entity.User;
import com.rayhan.kantinku.entity.UserPassword;
import com.rayhan.kantinku.exception.ResetPasswordInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service @Transactional
public class UserService {
    private static final String ROLE_ID_NEW_USER = "role_new_user";

    @Value("${gmail.account.username}")
    private String emailSender;

    @Value("${email.subject.register}")
    private String emailSubjectRegister;

    @Autowired private ResetPasswordDao resetPasswordDao;

    @Autowired private UserDao userDao;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UserPasswordDao userPasswordDao;

    @Autowired private GmailApiService gmailApiService;

    @Autowired private MustacheFactory mustacheFactory;

    public void register(RegisterFormDto registerFormDto) {

        User newUser = new User();
        newUser.setUsername(registerFormDto.getEmail());
        newUser.setActive(false);

        Role newUserRole = new Role();
        newUserRole.setId(ROLE_ID_NEW_USER);
        newUser.setRole(newUserRole);

        userDao.save(newUser);

        ResetPassword rp = new ResetPassword();
        rp.setUser(newUser);
        resetPasswordDao.save(rp);

        String uriVerifikasiEmail = buildUriEmailVerification(rp);

        log.debug("URI Verifikasi Email : {}", uriVerifikasiEmail);

        String content = generateRegistrationEmailContent(newUser, uriVerifikasiEmail).toString();
        log.debug("Registration email content : {}", content);
        gmailApiService.kirimEmail(emailSender,
                newUser.getUsername(),
                emailSubjectRegister,
                content);
    }

    public void verifyEmail(String uniqueCode) throws ResetPasswordInvalidException {
        ResetPassword rp = resetPasswordDao.findByUniqueCode(uniqueCode)
                .orElseThrow(()-> new ResetPasswordInvalidException("UniqueCode tidak terdaftar"));

        User user = rp.getUser();
        user.setActive(true);
        userDao.save(user);

    }

    public User verifyResetPasswordLink(String uniqueCode) throws ResetPasswordInvalidException {
        Optional<ResetPassword> orp = resetPasswordDao.findByUniqueCode(uniqueCode);
        ResetPassword rp = orp.orElseThrow(() -> new ResetPasswordInvalidException("Invalid code " + uniqueCode));
        if(LocalDateTime.now().isAfter(rp.getExpired())) {
            throw new ResetPasswordInvalidException("Unique code " +uniqueCode + " expired");
        }
        User user = rp.getUser();
        resetPasswordDao.deleteByUser(user);
        return user;
    }

    public void setNewPassword(User user, String password) {
        UserPassword userPassword = userPasswordDao.findByUser(user);
        if (userPassword == null) {
            userPassword = new UserPassword();
            userPassword.setUser(user);
        }

        String newPassword = passwordEncoder.encode(password);
        userPassword.setPassword(newPassword);
        userPasswordDao.save(userPassword);
    }

    public void forgotPassword(String email) {
        Optional<User> optuser = userDao.findByUsername(email);
        if (optuser.isPresent()) {
            resetPasswordDao.deleteByUser(optuser.get());
            ResetPassword resetPassword = new ResetPassword();
            resetPassword.setUser(optuser.get());
            resetPasswordDao.save(resetPassword);
        }
    }

    private String generateRegistrationEmailContent(User newUser, String uriVerifikasiEmail) {
        Mustache templateEmail = mustacheFactory.compile("templates/email/registrasi.html");
        Map<String, String> data = new HashMap<>();
        data.put("username", newUser.getUsername());
        data.put("uriVerifikasi", uriVerifikasiEmail);

        StringWriter emailcontent = new StringWriter();
        templateEmail.execute(emailcontent, data);
        return emailcontent.toString();
    }

    private static String buildUriEmailVerification(ResetPassword rp) {
        String uriVerifikasiEmail = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/register/verify/email")
                .queryParam("code", rp.getUniqueCode())
                .build().toString();
        return uriVerifikasiEmail;
    }

}
