package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.CollectionRequestDTO;
import com.maafa.auth_service.dto.CollectionResponseDTO;
import com.maafa.auth_service.service.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CollectionController {

    private final CollectionService collectionService;

    // CREATE
    @PostMapping
    @PreAuthorize("hasAuthority('COLLECTION_CREATE')")
    public ResponseEntity<CollectionResponseDTO> create(
            @Valid @RequestBody CollectionRequestDTO request
    ) {

        CollectionResponseDTO response =
                collectionService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('COLLECTION_READ')")
    public ResponseEntity<List<CollectionResponseDTO>> getAll() {

        return ResponseEntity.ok(
                collectionService.getAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COLLECTION_READ')")
    public ResponseEntity<CollectionResponseDTO> getById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                collectionService.getById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COLLECTION_UPDATE')")
    public ResponseEntity<CollectionResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CollectionRequestDTO request
    ) {

        return ResponseEntity.ok(
                collectionService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COLLECTION_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id
    ) {

        collectionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}