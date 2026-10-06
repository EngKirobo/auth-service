package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.DependantRequestDTO;
import com.maafa.auth_service.dto.DependantResponseDTO;

import java.util.List;

public interface DependantService {

    DependantResponseDTO create(
            Long userId,
            DependantRequestDTO request
    );

    List<DependantResponseDTO> getAll(
            Long userId
    );

    DependantResponseDTO getById(
            Long userId,
            Long id
    );

    DependantResponseDTO update(
            Long userId,
            Long id,
            DependantRequestDTO request
    );

    void delete(
            Long userId,
            Long id
    );
}