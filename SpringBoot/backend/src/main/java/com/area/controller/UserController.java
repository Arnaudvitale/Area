package com.area.controller;

import com.area.entity.User;
import com.area.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users/add")
    public ResponseEntity<?> add(@RequestBody User payload) {
        if (userRepository.findByUsernameAndEmail(payload.getUsername(), payload.getEmail()).isPresent()) {
            return ResponseEntity.ok(Map.of("status", "error", "message", "Username already taken"));
        }
        User user = new User();
        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("user", "user added successfully"));
    }
}
