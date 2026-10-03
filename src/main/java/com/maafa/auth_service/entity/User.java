package com.maafa.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_users_username",
            columnNames = "username"
        ),
        @UniqueConstraint(
            name = "uk_users_email",
            columnNames = "email"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // ==========================================
    // ROLE
    // ==========================================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "role_id",
        foreignKey = @ForeignKey(name = "fk_users_role")
    )
    private Role role;

    // ==========================================
    // USERNAME
    // ==========================================
    @Column(
        name = "username",
        nullable = false,
        unique = true,
        length = 100
    )
    private String username;

    // ==========================================
    // EMAIL
    // ==========================================
    @Column(
        name = "email",
        unique = true,
        length = 150
    )
    private String email;

    // ==========================================
    // PASSWORD
    // ==========================================
    @Column(
        name = "password",
        nullable = false,
        length = 255
    )
    private String password;

    // ==========================================
    // ACCOUNT STATUS
    // ==========================================
    @Column(
        name = "enabled",
        nullable = false
    )
    @Builder.Default
    private Boolean enabled = true;

    // ==========================================
    // CREATED / UPDATED
    // ==========================================
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}