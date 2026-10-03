package com.maafa.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }


    // ==========================================
    // GENERATE JWT TOKEN
    // ==========================================
    public String generateToken(
            Integer  userId,
            String username,
            String role,
            Object permissions) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + expiration
        );

        return Jwts.builder()
                .subject(username)
                .claims(Map.of(
                        "userId", userId,
                        "role", role,
                        "permissions", permissions
                ))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }


    // ==========================================
    // EXTRACT JWT CLAIMS
    // ==========================================
    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // ==========================================
    // VALIDATE TOKEN
    // ==========================================
    public boolean isValid(String token) {

        try {

            extractClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}