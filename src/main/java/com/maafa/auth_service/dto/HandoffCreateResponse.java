package com.maafa.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HandoffCreateResponse {

    private String code;

    private long expiresIn;
}