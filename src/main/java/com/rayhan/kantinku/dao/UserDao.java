package com.rayhan.kantinku.dao;

import com.rayhan.kantinku.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserDao extends PagingAndSortingRepository<User, String> {

    void save(User newUser);
    User findById(String id);

    Optional<User> findByUsername(String email);
}
