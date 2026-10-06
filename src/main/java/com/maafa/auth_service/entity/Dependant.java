package com.maafa.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "dependants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dependant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * This user_id belongs to the central Auth Service.
     * We intentionally do not use @ManyToOne because the
     * Auth Service has a separate database.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "relationship", length = 100)
    private String relationship;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}