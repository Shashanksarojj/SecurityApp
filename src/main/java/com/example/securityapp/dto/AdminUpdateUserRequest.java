package com.example.securityapp.dto;

import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String name;
    private String roleName;  // ADMIN or USER
}
