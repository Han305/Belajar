package com.rayhan.kantinku.dao;

import com.rayhan.kantinku.entity.ResetPassword;
import com.rayhan.kantinku.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ResetPasswordDao extends CrudRepository<ResetPassword, String> {
    Optional<ResetPassword> findByUniqueCode(String code);
    void deleteByUser(User user);
}
