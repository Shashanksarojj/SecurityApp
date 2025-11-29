package com.example.securityapp.controller;


import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.ApiResponse;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.service.UserService;
import com.example.securityapp.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers(HttpServletRequest request) {

        List<UserEntity> users = userService.getAllUsers();

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        "All users fetched successfully",
                        users,
                        request.getRequestURI()
                )
        );
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest req,
            HttpServletRequest request) {

        UserEntity updatedUser = userService.updateUserByAdmin(id, req);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        "User updated successfully",
                        updatedUser,
                        request.getRequestURI()
                )
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long id,
            HttpServletRequest request) {

        String result = userService.deleteUser(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        result,
                        null,
                        request.getRequestURI()
                )
        );
    }
}