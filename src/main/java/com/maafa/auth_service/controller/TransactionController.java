package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.TransactionDTO;
import com.maafa.auth_service.dto.TransactionResponseDTO;
import com.maafa.auth_service.security.AuthenticatedUser;
import com.maafa.auth_service.service.TransactionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {


    private final TransactionService transactionService;


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    @PreAuthorize("hasAuthority('TRANSACTION_CREATE')")
    public ResponseEntity<TransactionResponseDTO> create(
            @Valid @RequestBody TransactionDTO request,
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        transactionService.create(
                                userId,
                                request
                        )
                );
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @GetMapping
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public ResponseEntity<List<TransactionResponseDTO>> getAll(
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                transactionService.getAll(userId)
        );
    }


    // ============================================================
    // GET ACTIVE
    // ============================================================

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public ResponseEntity<List<TransactionResponseDTO>> getActive(
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                transactionService.getActive(userId)
        );
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable Integer id,
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                transactionService.getById(
                        userId,
                        id
                )
        );
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_UPDATE')")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody TransactionDTO request,
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                transactionService.update(
                        userId,
                        id,
                        request
                )
        );
    }


    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            Authentication authentication
    ) {

        Integer userId =
                getUserId(authentication);

        transactionService.delete(
                userId,
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    // ============================================================
    // GET USER ID FROM JWT
    // ============================================================

    private Integer getUserId(
            Authentication authentication
    ) {

        if (authentication == null) {

            throw new IllegalStateException(
                    "Authentication information not found"
            );
        }

        Object principal =
                authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedUser)) {

            throw new IllegalStateException(
                    "Authenticated user not found"
            );
        }

        AuthenticatedUser user =
                (AuthenticatedUser) principal;

        Long userId =
                user.getUserId();

        if (userId == null) {

            throw new IllegalStateException(
                    "User ID not found in authenticated user"
            );
        }

        return Math.toIntExact(userId);
    }
}