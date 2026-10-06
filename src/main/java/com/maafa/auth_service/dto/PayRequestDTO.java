package com.maafa.auth_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayRequestDTO {

    @Size(
            max = 100,
            message = "Details cannot exceed 100 characters"
    )
    private String details;

    @DecimalMin(
            value = "0.00",
            message = "Amount cannot be negative"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Amount must have up to 10 integer digits and 2 decimal places"
    )
    private BigDecimal amount;

    private Boolean authorised;

    private Boolean paid;

    @Size(
            max = 200,
            message = "Remarks cannot exceed 200 characters"
    )
    private String remarks;
}