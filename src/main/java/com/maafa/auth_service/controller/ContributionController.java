package com.maafa.auth_service.controller;

import com.maafa.auth_service.entity.Contribution;
import com.maafa.auth_service.service.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributn")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ContributionController {

    private final ContributionService service;

    // GET /api/contributn
    @GetMapping
    @PreAuthorize("hasAuthority('CONTRIBUTION_READ')")
    public ResponseEntity<List<Contribution>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/contributn/year/2026
    @GetMapping("/year/{year}")
    @PreAuthorize("hasAuthority('CONTRIBUTION_READ')")
    public ResponseEntity<List<Contribution>> getByYear(
            @PathVariable Integer year
    ) {
        return ResponseEntity.ok(service.getByYear(year));
    }

    // GET /api/contributn/user/1
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('CONTRIBUTION_READ')")
    public ResponseEntity<List<Contribution>> getByUserId(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // GET /api/contributn/user/1/year/2026
    @GetMapping("/user/{userId}/year/{year}")
    @PreAuthorize("hasAuthority('CONTRIBUTION_READ')")
    public ResponseEntity<List<Contribution>> getByUserIdAndYear(
            @PathVariable Integer userId,
            @PathVariable Integer year
    ) {
        return ResponseEntity.ok(
                service.getByUserIdAndYear(userId, year)
        );
    }
}