package com.maafa.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "auth_handoffs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_auth_handoffs_code_hash",
                        columnNames = "code_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_auth_handoffs_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_auth_handoffs_expires_at",
                        columnList = "expires_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthHandoff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "code_hash",
            nullable = false,
            unique = true,
            length = 64
    )
    private String codeHash;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Integer userId;

    @Column(
            name = "expires_at",
            nullable = false
    )
    private LocalDateTime expiresAt;

    @Column(
            name = "consumed_at"
    )
    private LocalDateTime consumedAt;

    @Column(
            name = "created_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;
}