package com.maafa.auth_service.service;

import com.maafa.auth_service.entity.UserSession;
import com.maafa.auth_service.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserSessionRepository sessionRepository;

    @Transactional
    public UserSession createSession(Integer userId, String token, long expirationMs) {
        sessionRepository.revokeAllByUserId(userId);

        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(Math.max(1, expirationMs / 1000));

        UserSession session = UserSession.builder()
                .userId(userId)
                .token(token)
                .expiresAt(expiresAt)
                .revoked(false)
                .build();

        return sessionRepository.save(session);
    }

    public Optional<UserSession> getActiveSessionForUser(Integer userId) {
        return sessionRepository
                .findFirstByUserIdAndRevokedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                        userId,
                        LocalDateTime.now()
                );
    }

    public boolean isTokenActive(String token) {
        return sessionRepository
                .findByTokenAndRevokedFalseAndExpiresAtAfter(token, LocalDateTime.now())
                .isPresent();
    }

    @Transactional
    public void revokeUserSessions(Integer userId) {
        sessionRepository.revokeAllByUserId(userId);
    }

    @Transactional
    public void revokeToken(String token) {
        sessionRepository.revokeByToken(token);
    }
}