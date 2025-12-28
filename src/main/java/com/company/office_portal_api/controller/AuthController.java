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
public User login(@RequestBody LoginRequest request) {

    Optional<User> user = userRepository.findByEmail(request.getEmail());

    if (user.isEmpty()) {
        throw new RuntimeException("User not found");
    }

    if (!user.get().getPassword().equals(request.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    return user.get(); // return full user object
}

}
