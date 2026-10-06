package com.maafa.auth_service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        /*
         * ============================================================
         * NO JWT
         * ============================================================
         */
        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {

            /*
             * ========================================================
             * VALIDATE JWT
             * ========================================================
             */
            if (!jwtService.isValid(token)) {

                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }

            Claims claims =
                    jwtService.extractClaims(token);


            /*
             * ========================================================
             * EXTRACT USER INFORMATION
             * ========================================================
             */
            String username =
                    claims.getSubject();

            Integer jwtUserId =
                    claims.get("userId", Integer.class);

            String role =
                    claims.get("role", String.class);


            if (jwtUserId == null) {

                throw new IllegalStateException(
                        "User ID not found in JWT"
                );
            }

            Long userId =
                    jwtUserId.longValue();


            /*
             * ========================================================
             * AUTHORITIES
             * ========================================================
             */
            Collection<SimpleGrantedAuthority> authorities =
                    new ArrayList<>();


            /*
             * ROLE
             */
            if (role != null && !role.isBlank()) {

                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                );
            }


            /*
             * PERMISSIONS
             */
            Object permissionsObject =
                    claims.get("permissions");

            if (permissionsObject instanceof List<?> permissions) {

                for (Object permission : permissions) {

                    if (permission != null) {

                        String permissionName =
                                permission.toString();

                        authorities.add(
                                new SimpleGrantedAuthority(
                                        permissionName
                                )
                        );
                    }
                }
            }


            /*
             * ========================================================
             * CUSTOM AUTHENTICATED USER
             * ========================================================
             */
            AuthenticatedUser authenticatedUser =
                    new AuthenticatedUser(
                            userId,
                            username,
                            authorities
                    );


            /*
             * ========================================================
             * SPRING SECURITY AUTHENTICATION
             * ========================================================
             */
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            null,
                            authorities
                    );


            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


        } catch (Exception e) {

            /*
             * Invalid JWT must never leave an old authentication
             * object in the SecurityContext.
             */
            SecurityContextHolder.clearContext();

            System.out.println(
                    "JWT authentication failed: "
                            + e.getMessage()
            );
        }


        filterChain.doFilter(request, response);
    }
}