package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.dto.*;
import com.sharedbalance.sharedbalancebackend.services.AuthService;
import com.sharedbalance.sharedbalancebackend.repository.UserRepository;
import com.sharedbalance.sharedbalancebackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ApiResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.error(Map.of(
                    "message", "Invalid credentials",
                    "type", "AUTH_FAIL"
            ));
        }

        // 🔥 GENERATE TOKEN
        String token = jwtService.generateToken(user.getEmail());

        // 🔥 RETURN TOKEN + USER
        return ApiResponse.success(Map.of(
                "account", user,
                "accessToken", token
        ));
    }
}