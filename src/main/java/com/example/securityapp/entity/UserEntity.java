package com.example.securityapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Entity
@Data
@Table(name = "users")
@SoftDelete( // Hibernate 6.4+ native soft delete
        strategy = SoftDeleteType.DELETED // true = deleted (default)
        // columnName can be customized; default is "deleted"
        // columnName = "deleted"
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}

