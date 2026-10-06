package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Dependant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DependantRepository
        extends JpaRepository<Dependant, Long> {

    List<Dependant> findByUserId(Long userId);

    Optional<Dependant> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}