package com.maafa.auth_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRequestDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Amount cannot be negative"
    )
    @Digits(
        integer = 10,
        fraction = 2,
        message = "Amount must have maximum 10 integer digits and 2 decimal places"
    )
    private BigDecimal amount;
}