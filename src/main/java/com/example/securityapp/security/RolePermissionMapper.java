package com.example.securityapp.security;

import java.util.List;
import java.util.Map;

import static com.example.securityapp.security.Permissions.*;

public final class RolePermissionMapper {

    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(
            "USER", List.of(USER_READ, USER_UPDATE),
            "ADMIN", List.of(USER_READ, USER_UPDATE, ADMIN_READ_USERS, ADMIN_MANAGE_USERS)
    );

    public static List<String> getPermissionsForRole(String role) {
        return ROLE_PERMISSIONS.getOrDefault(role, List.of());
    }
}
