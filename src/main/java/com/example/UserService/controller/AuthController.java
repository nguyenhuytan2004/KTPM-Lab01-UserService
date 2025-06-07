package com.example.UserService.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.UserService.model.User;
import com.example.UserService.service.IUserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private IUserService _userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> account) {
        String username = account.get("username");
        String password = account.get("password");
        System.out.println(username);
        System.err.println(password);

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password must be provided.");
        }

        try {
            User user = _userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }
            if (!user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            return ResponseEntity.ok("Login successful for user: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while logging in: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> account) {
        String username = account.get("username");
        String password = account.get("password");
        String confirmPassword = account.get("confirmPassword");

        if (username == null || password == null || confirmPassword == null) {
            return ResponseEntity.badRequest().body("Username, password, and confirm password must be provided.");
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }
        try {
            User existingUser = _userService.getUserByUsername(username);
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            _userService.createUser(username, password);

            return ResponseEntity.ok("Registration successful for user: " + newUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering: " + e.getMessage());
        }
    }
}
