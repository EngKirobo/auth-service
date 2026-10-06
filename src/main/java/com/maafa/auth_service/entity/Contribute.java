package com.maafa.auth_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contribute")
@Getter
@Setter
@NoArgsConstructor
public class Contribute {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "Date")
    private LocalDateTime date;
}