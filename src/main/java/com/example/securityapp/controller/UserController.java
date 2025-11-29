package com.example.securityapp.controller;

import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public UserEntity getProfile(Authentication auth) {
        return service.getMyProfile(auth.getName());
    }

    @PutMapping("/update")
    public UserEntity updateProfile(
            Authentication auth,
            @RequestBody UpdateUserRequest req
    ) {
        return service.updateMyProfile(auth.getName(), req);
    }
}

