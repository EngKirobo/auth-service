package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findFirstByUserIdAndRevokedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            Integer userId,
            LocalDateTime now
    );

    Optional<UserSession> findByTokenAndRevokedFalseAndExpiresAtAfter(
            String token,
            LocalDateTime now
    );

    @Modifying
    @Query("UPDATE UserSession s SET s.revoked = true WHERE s.userId = :userId AND s.revoked = false")
    void revokeAllByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE UserSession s SET s.revoked = true WHERE s.token = :token")
    void revokeByToken(@Param("token") String token);
}