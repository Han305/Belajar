package com.rayhan.kantinku.dao;

import com.rayhan.kantinku.entity.User;
import com.rayhan.kantinku.entity.UserPassword;
import org.springframework.data.repository.CrudRepository;

public interface UserPasswordDao extends CrudRepository<UserPassword, String> {
    UserPassword findByUser(User user);
}
