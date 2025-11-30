package com.example.securityapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRolePermissionsRequest {
    private List<String> permissions;
}

