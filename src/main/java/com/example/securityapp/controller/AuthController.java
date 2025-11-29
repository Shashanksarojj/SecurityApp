package com.example.securityapp.controller;

import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;
import com.example.securityapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService service;


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return service.login(request);
    }

    @PostMapping("/register-admin")
    public String registerAdmin(@RequestBody RegisterRequest request) {
        return service.registerAdmin(request);
    }
}
