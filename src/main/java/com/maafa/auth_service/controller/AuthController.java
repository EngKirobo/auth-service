package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.LoginRequest;
import com.maafa.auth_service.dto.LoginResponse;
import com.maafa.auth_service.dto.RegisterRequest;
import com.maafa.auth_service.entity.User;
import com.maafa.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        try {

            User user = authService.register(request);

            Map<String, Object> response = new HashMap<>();

            response.put("message", "User registered successfully");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {

            LoginResponse response = authService.login(request);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            Map<String, String> error = new HashMap<>();

            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(error);
        }
    }
}