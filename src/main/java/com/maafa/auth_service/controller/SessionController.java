package com.maafa.auth_service.controller;

import com.maafa.auth_service.entity.UserSession;
import com.maafa.auth_service.security.JwtService;
import com.maafa.auth_service.service.SessionService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/session")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    // ==========================================
    // GET CURRENT SESSION (used by 4200 before opening 4300)
    // ==========================================
    @GetMapping("/current")
    public ResponseEntity<?> current(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        System.out.println("AUTH HEADER = [" + authorization + "]");
        String token = extractToken(authorization);
        if (token == null) {
            return unauthorized("Authentication required");
        }

        if (!jwtService.isValid(token) || !sessionService.isTokenActive(token)) {
            return unauthorized("Session expired or revoked");
        }

        Claims claims = jwtService.extractClaims(token);
        Integer userId = claims.get("userId", Integer.class);

        UserSession session = sessionService.getActiveSessionForUser(userId).orElse(null);
        if (session == null) {
            return unauthorized("No active session");
        }

        return ResponseEntity.ok(Map.of(
                "token", session.getToken(),
                "userId", userId,
                "username", claims.getSubject(),
                "expiresAt", session.getExpiresAt().toString()
        ));
    }

    // ==========================================
    // VALIDATE SESSION (used by 4300 / 8091)
    // ==========================================
    @GetMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        String token = extractToken(authorization);
        boolean valid = token != null
                && jwtService.isValid(token)
                && sessionService.isTokenActive(token);

        return ResponseEntity.ok(Map.of("valid", valid));
    }

    // ==========================================
    // LOGOUT – revoke session in DB
    // ==========================================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        String token = extractToken(authorization);
        if (token != null) {
            sessionService.revokeToken(token);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    private String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        String token = authorization.substring(7).trim();
        return token.isEmpty() ? null : token;
    }

    private ResponseEntity<?> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", message));
    }
}