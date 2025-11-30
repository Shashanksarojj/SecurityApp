package com.example.securityapp.controller;

import com.example.securityapp.dto.ApiResponse;
import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;
import com.example.securityapp.service.AuthService;
import com.example.securityapp.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request,
                                                HttpServletRequest servletRequest) {

        String msg = authService.register(request);

        return ResponseEntity.ok(
                ResponseBuilder.success(msg, null, servletRequest.getRequestURI())
        );
    }


    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse> registerAdmin(@RequestBody RegisterRequest request,HttpServletRequest servletRequest) {

        String msg = authService.registerAdmin(request);

        return ResponseEntity.ok(
                ResponseBuilder.success(msg, null, servletRequest.getRequestURI())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request,
                                             HttpServletRequest servletRequest) {

        AuthResponse token = authService.login(request);

        return ResponseEntity.ok(
                ResponseBuilder.success("Login successful", token, servletRequest.getRequestURI())
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refresh(
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {

        String token = body.get("refreshToken");
        AuthResponse response = authService.refreshAccessToken(token);

        return ResponseEntity.ok(
                ResponseBuilder.success("Token refreshed successfully", response, request.getRequestURI())
        );
    }


}
