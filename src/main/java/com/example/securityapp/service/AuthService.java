package com.example.securityapp.service;

import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;

public interface AuthService {
    public String register(RegisterRequest req);
    public AuthResponse login(AuthRequest req);
    String registerAdmin(RegisterRequest req);
    AuthResponse refreshAccessToken(String refreshToken);

}
