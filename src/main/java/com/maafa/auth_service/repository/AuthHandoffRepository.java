package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.AuthHandoff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthHandoffRepository
        extends JpaRepository<AuthHandoff, Long> {

    Optional<AuthHandoff>
    findByCodeHashAndConsumedAtIsNull(
            String codeHash
    );
}