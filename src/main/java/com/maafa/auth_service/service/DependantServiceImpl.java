package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.DependantRequestDTO;
import com.maafa.auth_service.dto.DependantResponseDTO;
import com.maafa.auth_service.entity.Dependant;
import com.maafa.auth_service.repository.DependantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DependantServiceImpl implements DependantService {

    private final DependantRepository dependantRepository;

    @Override
    public DependantResponseDTO create(
            Long userId,
            DependantRequestDTO request
    ) {

        Dependant dependant = Dependant.builder()
                .userId(userId)
                .name(request.getName())
                .relationship(request.getRelationship())
                .phone(request.getPhone())
                .build();

        Dependant saved =
                dependantRepository.save(dependant);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DependantResponseDTO> getAll(
            Long userId
    ) {

        return dependantRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DependantResponseDTO getById(
            Long userId,
            Long id
    ) {

        Dependant dependant =
                dependantRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Dependant not found"
                                )
                        );

        return mapToResponse(dependant);
    }

    @Override
    public DependantResponseDTO update(
            Long userId,
            Long id,
            DependantRequestDTO request
    ) {

        Dependant dependant =
                dependantRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Dependant not found"
                                )
                        );

        dependant.setName(request.getName());
        dependant.setRelationship(request.getRelationship());
        dependant.setPhone(request.getPhone());

        Dependant updated =
                dependantRepository.save(dependant);

        return mapToResponse(updated);
    }

    @Override
    public void delete(
            Long userId,
            Long id
    ) {

        Dependant dependant =
                dependantRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Dependant not found"
                                )
                        );

        dependantRepository.delete(dependant);
    }

    private DependantResponseDTO mapToResponse(
            Dependant dependant
    ) {

        return DependantResponseDTO.builder()
                .id(dependant.getId())
                .userId(dependant.getUserId())
                .name(dependant.getName())
                .relationship(dependant.getRelationship())
                .phone(dependant.getPhone())
                .createdAt(dependant.getCreatedAt())
                .updatedAt(dependant.getUpdatedAt())
                .build();
    }
}