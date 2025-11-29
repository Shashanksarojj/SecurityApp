package com.example.securityapp.service;

import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity getMyProfile(String email);
    UserEntity updateMyProfile(String email, UpdateUserRequest req);

    // ADMIN features
    List<UserEntity> getAllUsers();
    UserEntity updateUserByAdmin(Long id, AdminUpdateUserRequest req);
    String deleteUser(Long id);
}
