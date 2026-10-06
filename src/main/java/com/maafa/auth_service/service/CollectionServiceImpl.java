package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.CollectionRequestDTO;
import com.maafa.auth_service.dto.CollectionResponseDTO;
import com.maafa.auth_service.entity.Collection;
import com.maafa.auth_service.repository.CollectionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Override
    public CollectionResponseDTO create(CollectionRequestDTO request) {

        Collection collection = Collection.builder()
                .amount(request.getAmount())
                .build();

        Collection saved = collectionRepository.save(collection);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionResponseDTO getById(Integer id) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Collection with ID " + id + " not found"
                        )
                );

        return mapToResponse(collection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponseDTO> getAll() {

        return collectionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CollectionResponseDTO update(
            Integer id,
            CollectionRequestDTO request
    ) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Collection with ID " + id + " not found"
                        )
                );

        collection.setAmount(request.getAmount());

        Collection updated = collectionRepository.save(collection);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {

        if (!collectionRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Collection with ID " + id + " not found"
            );
        }

        collectionRepository.deleteById(id);
    }

    private CollectionResponseDTO mapToResponse(Collection collection) {

        return CollectionResponseDTO.builder()
                .id(collection.getId())
                .amount(collection.getAmount())
                .build();
    }
}