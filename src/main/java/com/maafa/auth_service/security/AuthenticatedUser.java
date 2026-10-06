package com.maafa.auth_service.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthenticatedUser {

    private final Long userId;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(
            Long userId,
            String username,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
    }
}