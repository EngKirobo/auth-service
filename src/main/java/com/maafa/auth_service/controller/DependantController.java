package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.DependantRequestDTO;
import com.maafa.auth_service.dto.DependantResponseDTO;
import com.maafa.auth_service.security.AuthenticatedUser;
import com.maafa.auth_service.service.DependantService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dependants")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DependantController {

    private final DependantService dependantService;


    /*
     * ============================================================
     * CREATE DEPENDANT
     * ============================================================
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DEPENDANT_CREATE')")
    public ResponseEntity<DependantResponseDTO> create(
            @Valid @RequestBody DependantRequestDTO request,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        DependantResponseDTO response =
                dependantService.create(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /*
     * ============================================================
     * GET ALL DEPENDANTS FOR LOGGED-IN USER
     * ============================================================
     */
    @GetMapping
    @PreAuthorize("hasAuthority('DEPENDANT_READ')")
    public ResponseEntity<List<DependantResponseDTO>> getAll(
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                dependantService.getAll(userId)
        );
    }


    /*
     * ============================================================
     * GET DEPENDANT BY ID
     * ============================================================
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPENDANT_READ')")
    public ResponseEntity<DependantResponseDTO> getById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                dependantService.getById(
                        userId,
                        id
                )
        );
    }


    /*
     * ============================================================
     * UPDATE DEPENDANT
     * ============================================================
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPENDANT_UPDATE')")
    public ResponseEntity<DependantResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DependantRequestDTO request,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                dependantService.update(
                        userId,
                        id,
                        request
                )
        );
    }


    /*
     * ============================================================
     * DELETE DEPENDANT
     * ============================================================
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPENDANT_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        dependantService.delete(
                userId,
                id
        );

        return ResponseEntity.noContent().build();
    }


    /*
     * ============================================================
     * GET AUTHENTICATED USER ID
     * ============================================================
     *
     * The dependant belongs to the logged-in user through:
     *
     *     dependants.user_id
     *
     * There is intentionally no JPA relationship to User because
     * the dependant data is handled independently inside the
     * central auth_service application.
     */
    private Long getUserId(Authentication authentication) {

        if (authentication == null) {

            throw new IllegalStateException(
                    "Authentication information not found"
            );
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedUser)) {

            throw new IllegalStateException(
                    "Authenticated user not found"
            );
        }

        AuthenticatedUser user =
                (AuthenticatedUser) principal;

        Long userId = user.getUserId();

        if (userId == null) {

            throw new IllegalStateException(
                    "User ID not found in authenticated user"
            );
        }

        return userId;
    }
}