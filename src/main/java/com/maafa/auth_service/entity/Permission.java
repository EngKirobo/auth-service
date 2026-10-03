package com.maafa.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "permissions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_permissions_name",
            columnNames = "name"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
        name = "name",
        nullable = false,
        unique = true,
        length = 100
    )
    private String name;

    @Column(
        name = "description",
        length = 255
    )
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}