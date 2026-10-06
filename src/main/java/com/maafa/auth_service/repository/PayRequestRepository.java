package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.PayRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayRequestRepository
        extends JpaRepository<PayRequest, Integer> {

    List<PayRequest> findByUserId(Integer userId);

    Optional<PayRequest> findByIdAndUserId(
            Integer id,
            Integer userId
    );
}