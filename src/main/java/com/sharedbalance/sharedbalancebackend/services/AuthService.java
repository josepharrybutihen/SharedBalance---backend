package com.sharedbalance.sharedbalancebackend.services;

import com.sharedbalance.sharedbalancebackend.dto.*;
import com.sharedbalance.sharedbalancebackend.entity.User;
import com.sharedbalance.sharedbalancebackend.repository.UserRepository;
import com.sharedbalance.sharedbalancebackend.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ApiResponse register(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error(Map.of(
                    "type", "INPUT_INVALID",
                    "message", "Passwords do not match"
            ));
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ApiResponse.error(Map.of(
                    "type", "CONFLICT",
                    "message", "Email already exists"
            ));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ApiResponse.success(Map.of(
                "account", user
        ));
    }

    public ApiResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElse(null);

    if (user == null ||
            !passwordEncoder.matches(request.getPassword(), user.getPassword())) {

        return ApiResponse.error(Map.of(
                "type", "AUTH_FAIL",
                "message", "Invalid credentials"
        ));
    }

    // ✅ GENERATE TOKEN
    String token = jwtService.generateToken(user.getEmail());

    return ApiResponse.success(Map.of(
            "account", user,
            "accessToken", token   // 🔥 THIS IS WHAT YOU WERE MISSING
    ));
}
}