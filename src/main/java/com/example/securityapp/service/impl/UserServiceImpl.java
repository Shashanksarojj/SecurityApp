package com.example.securityapp.service.impl;

import com.example.securityapp.dto.AdminUpdateUserRequest;
import com.example.securityapp.dto.UpdateUserRequest;
import com.example.securityapp.entity.PermissionEntity;
import com.example.securityapp.entity.RoleEntity;
import com.example.securityapp.entity.UserEntity;
import com.example.securityapp.exception.ResourceNotFoundException;
import com.example.securityapp.repository.PermissionRepository;
import com.example.securityapp.repository.RoleRepository;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PermissionRepository permissionRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (req.getName() != null) {
            user.setName(req.getName());
        }

        if (req.getRoleName() != null) {
            RoleEntity role = roleRepository.findByName(req.getRoleName())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + req.getRoleName()));
            user.setRole(role);
        }

        return repo.save(user);
    }


    @Override
    public String deleteUser(Long id) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        repo.delete(user);
        return "User deleted successfully";
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

    @Override
    @Transactional
    public String restoreUser(Long id) {
        int updated = repo.restoreUser(id);
        if (updated == 0) {
            throw new ResourceNotFoundException("User not found or not deleted");
        }
        return "User restored successfully";
    }

    @Override
    @Transactional
    public RoleEntity updateRolePermissions(String roleName, List<String> perms) {
        RoleEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        List<PermissionEntity> entities = perms.stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Permission not found: " + name))
                )
                .toList();

        role.setPermissions(entities);
        return roleRepository.save(role);
    }
}