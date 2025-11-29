package com.example.securityapp.controller;

import com.example.securityapp.dto.ApiResponse;
import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.service.UserService;
import com.example.securityapp.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(
            Authentication auth,
            HttpServletRequest request) {

        UserEntity user = userService.getMyProfile(auth.getName());

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        "User profile fetched successfully",
                        user,
                        request.getRequestURI()
                )
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProfile(
            Authentication auth,
            @RequestBody UpdateUserRequest req,
            HttpServletRequest request) {

        UserEntity updatedUser = userService.updateMyProfile(auth.getName(), req);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        "User profile updated successfully",
                        updatedUser,
                        request.getRequestURI()
                )
        );
    }
}

