package com.example.securityapp.controller;


import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.ApiResponse;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.service.UserService;
import com.example.securityapp.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN_READ_USERS')")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String emailFilter,
            HttpServletRequest request
    ) {
        log.info("Admin fetching users: page={}, size={}", page, size);
        Page<UserEntity> usersPage = userService.getAllUsersPaged(page, size, sortBy, direction, emailFilter);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        "Users fetched successfully",
                        usersPage,
                        request.getRequestURI()
                )
        );
    }


    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_USERS')")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest req,
            HttpServletRequest request) {
        log.info("Admin updating user id={}", id);
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
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_USERS')")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long id,
            HttpServletRequest request) {
        log.warn("Admin deleting user id={}", id);
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