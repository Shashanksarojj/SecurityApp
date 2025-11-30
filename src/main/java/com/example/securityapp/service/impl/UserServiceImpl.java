package com.example.securityapp.service.impl;

import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public UserEntity getMyProfile(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserEntity updateMyProfile(String email, UpdateUserRequest req) {
        UserEntity user = getMyProfile(email);
        user.setName(req.getName());
        return repo.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public UserEntity updateUserByAdmin(Long id, AdminUpdateUserRequest req) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(req.getName());
        user.setRole(req.getRole());

        return repo.save(user);
    }

    @Override
    public String deleteUser(Long id) {
        repo.deleteById(id);
        return "Deleted Successfully";
    }

    @Override
    public Page<UserEntity> getAllUsersPaged(int page, int size, String sortBy, String direction, String emailFilter) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (emailFilter != null && !emailFilter.isBlank()) {
            return repo.findByEmailContainingIgnoreCase(emailFilter, pageable);
        }
        return repo.findAll(pageable);
    }

}