package com.example.UserService.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserService.model.User;
import com.example.UserService.repository.IUserRepository;
import com.example.UserService.service.IUserService;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository _userRepository;

    @Override
    public User getUserById(Integer userId) {
        return _userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return _userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User createUser(String username, String password) {
        Optional<User> existingUser = _userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with username " + username + " already exists.");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return _userRepository.save(newUser);
    }

    @Override
    public User updateUser(Integer userId, String username, String password) {
        User user = _userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        user.setUsername(username);
        user.setPassword(password);
        return _userRepository.save(user);
    }

    @Override
    public User deleteUser(Integer userId) {
        User user = _userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        _userRepository.delete(user);
        return user;
    }
}
