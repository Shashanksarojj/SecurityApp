package com.example.securityapp.service.impl;


import com.example.securityapp.config.JwtUtil;
import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;
import com.example.securityapp.entity.RefreshToken;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.exception.TooManyRequestsException;
import com.example.securityapp.repository.RefreshTokenRepository;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.security.LoginRateLimiter;
import com.example.securityapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginRateLimiter loginRateLimiter;

    @Override
    public String register(RegisterRequest req) {
        UserEntity user = new UserEntity();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setName(req.getName());
        user.setRole("USER");

        repo.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponse login(AuthRequest req) {
        log.info("Login attempt for email={}", req.getEmail());
        if (!loginRateLimiter.isAllowed(req.getEmail())) {
            throw new TooManyRequestsException("Too many login attempts. Please try again later.");
        }
        UserEntity user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setUser(user);
        refresh.setExpiry(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(refresh);

        return new AuthResponse(accessToken, refresh.getToken());
    }


    @Override
    public String registerAdmin(RegisterRequest req) {
        UserEntity user = new UserEntity();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("ADMIN"); // admin role
        user.setName(req.getName());
        repo.save(user);
        return "Admin user created successfully";
    }

    @Override
    public AuthResponse refreshAccessToken(String token) {

        RefreshToken refresh = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refresh.isRevoked() || refresh.getExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        UserEntity user = refresh.getUser();

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(newAccessToken, token);
    }

}

