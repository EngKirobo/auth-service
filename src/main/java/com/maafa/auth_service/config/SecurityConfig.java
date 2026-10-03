package com.maafa.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // We are using JWT, so CSRF is not required
            .csrf(csrf -> csrf.disable())

            // Authentication is stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // API authorization rules
            .authorizeHttpRequests(auth -> auth

                // Login
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/login"
                ).permitAll()

                // Registration
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/register"
                ).permitAll()

                // Refresh JWT
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/refresh"
                ).permitAll()

                // Public health check
                .requestMatchers(
                    "/api/auth/health"
                ).permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}