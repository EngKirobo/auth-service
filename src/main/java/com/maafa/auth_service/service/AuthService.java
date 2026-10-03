package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.LoginRequest;
import com.maafa.auth_service.dto.LoginResponse;
import com.maafa.auth_service.dto.RegisterRequest;
import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.entity.User;
import com.maafa.auth_service.repository.UserRepository;
import com.maafa.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
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

        // NEVER save plain-text password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setEnabled(true);

        return userRepository.save(user);
    }


    // ==========================================
    // LOGIN
    // ==========================================
    public LoginResponse login(LoginRequest request) {

        // 1. Find user
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );


        // 2. Check whether account is enabled
        // if (!user.isEnabled()) {
        //     throw new RuntimeException(
        //             "User account is disabled"
        //     );
        // }


        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException(
                    "User account is disabled"
            );
        }


        // 3. Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }


        // 4. Get role
        String role = null;

        if (user.getRole() != null) {
            role = user.getRole().getName();
        }


        // 5. Get permissions
        Set<String> permissions = Set.of();

        if (user.getRole() != null &&
                user.getRole().getPermissions() != null) {

            permissions = user.getRole()
                    .getPermissions()
                    .stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());
        }


        // 6. Generate JWT
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                role,
                permissions
        );


        // 7. Return login response
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(role)
                .permissions(permissions)
                .build();
    }
}