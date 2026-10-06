package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.PayRequestDTO;
import com.maafa.auth_service.dto.PayRequestResponseDTO;
import com.maafa.auth_service.security.AuthenticatedUser;
import com.maafa.auth_service.service.PayRequestService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payrequests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PayRequestController {

    private final PayRequestService payRequestService;


    /*
     * ============================================================
     * CREATE
     * ============================================================
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PAYREQUEST_CREATE')")
    public ResponseEntity<PayRequestResponseDTO> create(
            @Valid @RequestBody PayRequestDTO request,
            Authentication authentication
    ) {

        Integer userId = getUserId(authentication);

        PayRequestResponseDTO response =
                payRequestService.create(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /*
     * ============================================================
     * GET ALL
     * ============================================================
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PAYREQUEST_READ')")
    public ResponseEntity<List<PayRequestResponseDTO>> getAll(
            Authentication authentication
    ) {

        Integer userId = getUserId(authentication);

        return ResponseEntity.ok(
                payRequestService.getAll(userId)
        );
    }


    /*
     * ============================================================
     * GET BY ID
     * ============================================================
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYREQUEST_READ')")
    public ResponseEntity<PayRequestResponseDTO> getById(
            @PathVariable Integer id,
            Authentication authentication
    ) {

        Integer userId = getUserId(authentication);

        return ResponseEntity.ok(
                payRequestService.getById(
                        userId,
                        id
                )
        );
    }


    /*
     * ============================================================
     * UPDATE
     * ============================================================
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYREQUEST_UPDATE')")
    public ResponseEntity<PayRequestResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody PayRequestDTO request,
            Authentication authentication
    ) {

        Integer userId = getUserId(authentication);

        return ResponseEntity.ok(
                payRequestService.update(
                        userId,
                        id,
                        request
                )
        );
    }


    /*
     * ============================================================
     * DELETE
     * ============================================================
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYREQUEST_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            Authentication authentication
    ) {

        Integer userId = getUserId(authentication);

        payRequestService.delete(
                userId,
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    /*
     * ============================================================
     * GET AUTHENTICATED USER ID
     * ============================================================
     *
     * AuthenticatedUser stores userId as Long.
     * PayRequest.entity uses Integer userId.
     *
     * Therefore we safely convert Long -> Integer.
     * ============================================================
     */
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


        /*
         * Safely convert Long to Integer.
         *
         * Math.toIntExact() throws ArithmeticException
         * if the Long value cannot fit into an Integer.
         */
        try {

            return Math.toIntExact(userId);

        } catch (ArithmeticException e) {

            throw new IllegalStateException(
                    "Authenticated user ID is outside the Integer range",
                    e
            );
        }
    }
}
