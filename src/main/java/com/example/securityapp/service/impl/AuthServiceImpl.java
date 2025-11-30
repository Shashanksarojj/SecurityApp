package com.example.securityapp.service.impl;


import com.example.securityapp.config.JwtUtil;
import com.example.securityapp.dto.AuthRequest;
import com.example.securityapp.dto.AuthResponse;
import com.example.securityapp.dto.RegisterRequest;
import com.example.securityapp.entity.PermissionEntity;
import com.example.securityapp.entity.RefreshToken;
import com.example.securityapp.entity.RoleEntity;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.exception.InvalidCredentialsException;
import com.example.securityapp.exception.RoleNotFoundException;
import com.example.securityapp.exception.TooManyRequestsException;
import com.example.securityapp.exception.UserAlreadyExistsException;
import com.example.securityapp.repository.PermissionRepository;
import com.example.securityapp.repository.RefreshTokenRepository;
import com.example.securityapp.repository.RoleRepository;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.security.LoginRateLimiter;
import com.example.securityapp.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository repo;

    private final PasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginRateLimiter loginRateLimiter;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public String register(RegisterRequest req) {

        // 1️⃣ Check if email already exists
        if (repo.existsByEmail(req.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + req.getEmail());
        }

        // 2️⃣ Normalize role (default = USER)
        String roleName = (req.getRole() == null || req.getRole().isBlank())
                ? "USER"
                : req.getRole().trim().toUpperCase();

        // 3️⃣ Ensure role exists (create if not)
        RoleEntity role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName(roleName);
                    newRole.setPermissions(new java.util.ArrayList<>());
                    return roleRepository.save(newRole);
                });

        // 4️⃣ Handle permissions (optional)
        List<String> incomingPerms = req.getPermissions() != null
                ? req.getPermissions()
                : List.of();

        List<PermissionEntity> permissionEntities = incomingPerms.stream()
                .map(p -> p.trim().toUpperCase())
                .map(p -> permissionRepository.findByName(p)
                        .orElseGet(() -> {
                            PermissionEntity perm = new PermissionEntity();
                            perm.setName(p);
                            return permissionRepository.save(perm);
                        })
                )
                .toList();

        // 5️⃣ Attach permissions to role
        if (!permissionEntities.isEmpty()) {
            role.getPermissions().addAll(permissionEntities);
            roleRepository.save(role);
        }

        // 6️⃣ Create User
        UserEntity user = new UserEntity();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(role);

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
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user);

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setUser(user);
        refresh.setExpiry(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(refresh);

        return new AuthResponse(accessToken, refresh.getToken());
    }


    @Override
    public String registerAdmin(RegisterRequest req) {

        RoleEntity adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role missing"));

        UserEntity user = new UserEntity();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setName(req.getName());
        user.setRole(adminRole);

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
        String newAccessToken = jwtUtil.generateToken(user);

        return new AuthResponse(newAccessToken, token);
    }

}

