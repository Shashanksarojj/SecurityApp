package com.example.securityapp.service.impl;


import com.example.securityapp.config.JwtUtil;
import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

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
        UserEntity user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token);
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
}

