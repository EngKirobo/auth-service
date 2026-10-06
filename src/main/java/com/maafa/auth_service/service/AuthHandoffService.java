package com.maafa.auth_service.service;

import com.maafa.auth_service.entity.AuthHandoff;
import com.maafa.auth_service.repository.AuthHandoffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthHandoffService {

    private static final int EXPIRATION_SECONDS = 60;

    private final AuthHandoffRepository handoffRepository;

    private final SecureRandom secureRandom =
            new SecureRandom();

    /**
     * Create a temporary one-time code.
     */
    @Transactional
    public String createHandoff(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }

        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        String code =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

        String codeHash =
                sha256(code);

        AuthHandoff handoff =
                AuthHandoff.builder()
                        .codeHash(codeHash)
                        .userId(userId)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusSeconds(
                                                EXPIRATION_SECONDS
                                        )
                        )
                        .build();

        handoffRepository.save(handoff);

        return code;
    }

    /**
     * Consume the code.
     *
     * Returns the user ID associated with the code.
     *
     * The code can only be consumed once.
     */
    @Transactional
    public Integer consumeHandoff(String code) {

        if (code == null ||
                code.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid handoff code"
            );
        }

        String codeHash =
                sha256(code);

        AuthHandoff handoff =
                handoffRepository
                        .findByCodeHashAndConsumedAtIsNull(
                                codeHash
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired handoff code"
                                )
                        );

        if (
                handoff.getExpiresAt()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Handoff code has expired"
            );
        }

        /*
         * Mark the code as consumed BEFORE returning.
         */
        handoff.setConsumedAt(
                LocalDateTime.now()
        );

        handoffRepository.save(handoff);

        return handoff.getUserId();
    }

    private String sha256(String value) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            value.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte b : hash) {

                result.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return result.toString();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to generate code hash",
                    e
            );
        }
    }
}