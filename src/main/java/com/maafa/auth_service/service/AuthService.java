package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.LoginRequest;
import com.maafa.auth_service.dto.LoginResponse;
import com.maafa.auth_service.dto.RegisterRequest;
import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.entity.User;
import com.maafa.auth_service.repository.UserRepository;
import com.maafa.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // ==========================================
    // REGISTER
    // ==========================================
    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    // ==========================================
    // LOGIN
    // ==========================================
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password")
                );

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return createAuthenticationResponse(user);
    }

    // ==========================================
    // CREATE AUTHENTICATION RESPONSE
    // (shared by login + handoff)
    // ==========================================
    public LoginResponse createAuthenticationResponse(User user) {

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("User account is disabled");
        }

        String role = null;
        if (user.getRole() != null) {
            role = user.getRole().getName();
        }

        Set<String> permissions = Set.of();
        if (user.getRole() != null &&
                user.getRole().getPermissions() != null) {

            permissions = user.getRole()
                    .getPermissions()
                    .stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());
        }

        // Generate JWT
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                role,
                permissions
        );

        // Save session in atcmaafa.user_sessions
        sessionService.createSession(
                user.getId(),
                token,
                jwtExpirationMs
        );

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(role)
                .permissions(permissions)
                .build();
    }

    // ==========================================
    // AUTHENTICATION HANDOFF
    // ==========================================
    public LoginResponse authenticateByHandoff(Integer userId) {

        if (userId == null) {
            throw new RuntimeException("User ID is required");
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User associated with handoff was not found"
                        )
                );

        return createAuthenticationResponse(user);
    }
}