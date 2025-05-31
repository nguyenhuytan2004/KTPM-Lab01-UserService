package com.example.UserService.service;

import com.example.UserService.model.User;

public interface IUserService {
    User getUserById(Integer userId);

    User getUserByUsername(String username);

    User createUser(String username, String password);

    User updateUser(Integer userId, String username, String password);

    User deleteUser(Integer userId);
}