package com.company.office_portal_api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.company.office_portal_api.dto.LoginRequest;
import com.company.office_portal_api.model.User;
import com.company.office_portal_api.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
public String login(@RequestBody LoginRequest request) {

    System.out.println("LOGIN ATTEMPT: " + request.getEmail() + " | " + request.getPassword());

    Optional<User> user = userRepository.findByEmail(request.getEmail());

    if (user.isEmpty()) {
        return "User not found";
    }

    if (!user.get().getPassword().equals(request.getPassword())) {
        return "Invalid password";
    }

    return "Login successful | Role: " + user.get().getRole();
}
}
