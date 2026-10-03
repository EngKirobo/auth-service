package com.maafa.auth_service.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Integer  userId;

    private String username;

    private String role;

    private Set<String> permissions;
}