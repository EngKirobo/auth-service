package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.HandoffCreateResponse;
import com.maafa.auth_service.dto.HandoffExchangeRequest;
import com.maafa.auth_service.dto.LoginResponse;
import com.maafa.auth_service.security.JwtService;
import com.maafa.auth_service.service.AuthHandoffService;
import com.maafa.auth_service.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/handoff")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://localhost:4300"
        }
)
public class AuthHandoffController {

    private final JwtService jwtService;

    private final AuthHandoffService authHandoffService;

    private final AuthService authService;


    // ==========================================
    // CREATE HANDOFF
    // ==========================================
    @PostMapping("/create")
    public ResponseEntity<?> createHandoff(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorization
    ) {

        try {

            // ==========================================
            // CHECK AUTHORIZATION HEADER
            // ==========================================
            if (
                    authorization == null ||
                    !authorization.startsWith("Bearer ")
            ) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                Map.of(
                                        "error",
                                        "Authentication token is required"
                                )
                        );
            }


            // ==========================================
            // EXTRACT JWT
            // ==========================================
            String token = authorization
                    .substring(7)
                    .trim();


            if (
                    token.isEmpty() ||
                    !jwtService.isValid(token)
            ) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                Map.of(
                                        "error",
                                        "Invalid or expired token"
                                )
                        );
            }


            // ==========================================
            // EXTRACT CLAIMS
            // ==========================================
            Claims claims = jwtService.extractClaims(token);


            // ==========================================
            // GET USER ID
            // ==========================================
            Integer userId = claims.get(
                    "userId",
                    Integer.class
            );


            if (userId == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                Map.of(
                                        "error",
                                        "User ID not found in token"
                                )
                        );
            }


            // ==========================================
            // CREATE ONE-TIME HANDOFF CODE
            // ==========================================
            String code = authHandoffService
                    .createHandoff(userId);


            // ==========================================
            // RETURN HANDOFF CODE
            // ==========================================
            return ResponseEntity.ok(
                    new HandoffCreateResponse(
                            code,
                            60
                    )
            );


        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "error",
                                    "Unable to create authentication handoff"
                            )
                    );
        }
    }


    // ==========================================
    // EXCHANGE HANDOFF
    // ==========================================
    @PostMapping("/exchange")
    public ResponseEntity<?> exchangeHandoff(
            @Valid
            @RequestBody
            HandoffExchangeRequest request
    ) {

        try {

            // ==========================================
            // CONSUME HANDOFF CODE
            // ==========================================
            Integer userId = authHandoffService
                    .consumeHandoff(
                            request.getCode()
                    );


            // ==========================================
            // GENERATE FRESH JWT
            // ==========================================
            LoginResponse loginResponse =
                    authService.authenticateByHandoff(
                            userId
                    );


            // ==========================================
            // RETURN COMPLETE AUTHENTICATION RESPONSE
            // ==========================================
            return ResponseEntity.ok(
                    loginResponse
            );


        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );


        } catch (Exception e) {

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "error",
                                    "Authentication handoff failed"
                            )
                    );
        }
    }
}
