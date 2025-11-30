package com.example.securityapp.service;

import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserEntity getMyProfile(String email);
    UserEntity updateMyProfile(String email, UpdateUserRequest req);

    // ADMIN features
    List<UserEntity> getAllUsers();
    Page<UserEntity> getAllUsersPaged(int page, int size, String sortBy, String direction, String emailFilter);
    UserEntity updateUserByAdmin(Long id, AdminUpdateUserRequest req);
    String deleteUser(Long id);
}
