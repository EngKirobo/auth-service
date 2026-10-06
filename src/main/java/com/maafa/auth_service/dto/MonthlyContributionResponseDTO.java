package com.maafa.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyContributionResponseDTO {

    private Integer userId;

    private String fullname;

    private Integer year;

    private Integer month;

    private BigDecimal totalAmount;
}