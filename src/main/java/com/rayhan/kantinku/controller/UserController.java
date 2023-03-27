package com.rayhan.kantinku.controller;

import com.rayhan.kantinku.dao.ResetPasswordDao;
import com.rayhan.kantinku.dao.UserDao;
import com.rayhan.kantinku.dto.RegisterFormDto;
import com.rayhan.kantinku.entity.User;
import com.rayhan.kantinku.entity.UserPassword;
import com.rayhan.kantinku.exception.ResetPasswordInvalidException;
import com.rayhan.kantinku.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
public class UserController {

    @Autowired private UserService userService;
    @Autowired private ResetPasswordDao resetPasswordDao;
    @Autowired private UserDao userDao;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/register/form")
    public ModelMap displayForm() {
        return new ModelMap()
                .addAttribute(new RegisterFormDto());
    }

    @PostMapping("/register/form")
    public String processForm(@ModelAttribute RegisterFormDto registerFormDto, BindingResult errors, SessionStatus status) {

        if(errors.hasErrors()) {
            log.debug("Errors : {}", errors.toString());
            return "form";
        }

        log.debug("Name : {}", registerFormDto.getName());
        log.debug("Phone : {}", registerFormDto.getPhone());
        log.debug("Email : {}", registerFormDto.getEmail());

        userService.register(registerFormDto);

        status.setComplete();
        return "redirect:success";
    }

    @GetMapping("/register/success")
    public void registrationSuccess() {

    }

    @GetMapping("/register/verify/email")
    public String verifyEmail(@RequestParam(name = "code") String uniqueCode) throws ResetPasswordInvalidException{
        userService.verifyEmail(uniqueCode);
        return "redirect:/password/reset?code="+uniqueCode;
    }

    @GetMapping("/password/reset")
    public ModelMap displayFormResetPassword(@RequestParam(name = "code") String uniqueCode) {
        ModelMap modelMap = new ModelMap();
        try {
            User user = userService.verifyResetPasswordLink(uniqueCode);
            modelMap.addAttribute("user", user);
        } catch (ResetPasswordInvalidException e) {
            log.warn(("Reset Password code invalid : "+uniqueCode));
            modelMap.addAttribute("error", "Reset password code invalid");
        }
        return modelMap;
    }

    @PostMapping("/password/reset")
    public String processFormResetPassword(@RequestParam("user") String id,
                                           @RequestParam String password,
                                           @RequestParam(name = "confirm_password") String confirm) {
        User user = userDao.findById(id);
        if (user != null
                && StringUtils.hasText(password)
                && StringUtils.hasText(confirm)
                && password.equals(confirm)) {
            userService.setNewPassword(user, password);
        } else {

        }

        return "redirect:success";
    }

    @GetMapping("/password/success")
    public void resetPasswordSuccess() {

    }

    @GetMapping("/password/forgot")
    public void displayForgotPasswordForm() {

    }

    @PostMapping("/password/forgot")
    public String processForgotPasswordForm(@RequestParam String email) {
        userService.forgotPassword(email);
        return "redirect:sent";
    }

    @GetMapping("/password/sent")
    public void forgotPasswordSent() {

    }
}
