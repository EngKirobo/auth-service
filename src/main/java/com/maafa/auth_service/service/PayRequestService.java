package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.PayRequestDTO;
import com.maafa.auth_service.dto.PayRequestResponseDTO;

import java.util.List;

public interface PayRequestService {

    PayRequestResponseDTO create(
            Integer userId,
            PayRequestDTO request
    );

    List<PayRequestResponseDTO> getAll(
            Integer userId
    );

    PayRequestResponseDTO getById(
            Integer userId,
            Integer id
    );

    PayRequestResponseDTO update(
            Integer userId,
            Integer id,
            PayRequestDTO request
    );

    void delete(
            Integer userId,
            Integer id
    );
}