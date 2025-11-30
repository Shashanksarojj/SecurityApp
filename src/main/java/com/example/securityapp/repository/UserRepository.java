package com.example.securityapp.repository;

import com.example.securityapp.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    @Query(
            value = "SELECT * FROM users WHERE deleted = true",
            nativeQuery = true
    )

    List<UserEntity> findAllSoftDeleted();

    @Modifying
    @Query(value = "UPDATE users SET deleted = false WHERE id = :id AND deleted = true", nativeQuery = true)
    int restoreUser(@Param("id") Long id);

    @Query(value = "SELECT * FROM users WHERE deleted = true", nativeQuery = true)
    List<UserEntity> findDeletedUsers();

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);
}
