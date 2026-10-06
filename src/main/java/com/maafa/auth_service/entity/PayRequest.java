package com.maafa.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payrequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "details", length = 100)
    private String details;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(
            name = "created_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime updatedAt;

    @Column(name = "authorised")
    private Boolean authorised;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "remarks", length = 200)
    private String remarks;
}