package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.dto.ApiResponse;
import com.sharedbalance.sharedbalancebackend.dto.LoginRequest;
import com.sharedbalance.sharedbalancebackend.entity.User;
import com.sharedbalance.sharedbalancebackend.repository.UserRepository;
import com.sharedbalance.sharedbalancebackend.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://shared-balance-azure.vercel.app"
})
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // GET USER PROFILE
    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of(
                "payload", user
        );
    }


    // UPDATE PROFILE
    @PutMapping("/update")
    public Map<String,Object> updateProfile(
            @RequestBody Map<String,String> req,
            Authentication authentication){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(req.get("firstName"));
        user.setLastName(req.get("lastName"));        
        user.setEmail(req.get("email"));
        user.setProfileImage(req.get("profileImage")); // ADD THIS

        if(req.get("password") != null && !req.get("password").isEmpty()){
            user.setPassword(passwordEncoder.encode(req.get("password")));
        }

        userRepository.save(user);

        return Map.of("message","Profile updated successfully");
    }

    public Map<String,String> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String path = "uploads/" + fileName;

        File dest = new File(path);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        user.setProfileImage("http://localhost:8080/" + path);
        userRepository.save(user);

        return Map.of("url", user.getProfileImage());
    }

    @GetMapping("/all")
    public Map<String, Object> getAllUsers(){
        return Map.of(
                "payload", userRepository.findAll()
        );
    }
}




