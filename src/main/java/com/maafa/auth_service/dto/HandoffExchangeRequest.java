package com.maafa.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HandoffExchangeRequest {

    @NotBlank(message = "Handoff code is required")
    private String code;
}