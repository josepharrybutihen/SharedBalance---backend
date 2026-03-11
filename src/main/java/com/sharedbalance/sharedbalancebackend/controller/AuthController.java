package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.dto.*;
import com.sharedbalance.sharedbalancebackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}