package com.maafa.auth_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "contribution")
@Getter
@Setter
@NoArgsConstructor
public class Contribution {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "year")
    private Integer year;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}