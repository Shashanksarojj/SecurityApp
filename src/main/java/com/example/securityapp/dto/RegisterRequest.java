package com.example.securityapp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Register user request DTO")
public class RegisterRequest {

    @Schema(example = "user@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(example = "Password@123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(example = "Test User")
    @NotBlank(message = "Name is required")
    private String name;


//    private String roleName; // optional, default USER
    @Schema(example = "ADMIN", description = "Role name (optional). Default = USER")
    private String role;                 // user/admin/customRole

    @Schema(
            description = "List of permissions to assign to role",
            example = "[\"ADMIN_READ_USERS\", \"ADMIN_MANAGE_USERS\"]"
    )
    private List<String> permissions;    // optional permissions
}
