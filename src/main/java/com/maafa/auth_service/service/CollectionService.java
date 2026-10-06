package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.CollectionRequestDTO;
import com.maafa.auth_service.dto.CollectionResponseDTO;

import java.util.List;

public interface CollectionService {

    CollectionResponseDTO create(CollectionRequestDTO request);

    CollectionResponseDTO getById(Integer id);

    List<CollectionResponseDTO> getAll();

    CollectionResponseDTO update(
            Integer id,
            CollectionRequestDTO request
    );

    void delete(Integer id);
}