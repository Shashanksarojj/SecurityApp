package com.example.securityapp.controller;


import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService service;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("/users/{id}")
    public UserEntity updateUser(
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest req
    ) {
        return service.updateUserByAdmin(id, req);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }
}
