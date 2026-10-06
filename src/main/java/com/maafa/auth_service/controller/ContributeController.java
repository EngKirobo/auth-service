package com.maafa.auth_service.controller;

import com.maafa.auth_service.dto.MonthlyContributionResponseDTO;
import com.maafa.auth_service.entity.Contribute;
import com.maafa.auth_service.service.ContributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ContributeController {

    private final ContributeService contributeService;

    /**
     * Get all individual contribution records
     */
    @GetMapping
    public ResponseEntity<List<Contribute>> getAllContributions() {

        return ResponseEntity.ok(
                contributeService.getAllContributions()
        );
    }

    /**
     * Get contributions for one user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Contribute>> getUserContributions(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                contributeService.getUserContributions(userId)
        );
    }

    /**
     * Get monthly contribution totals for all users
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyContributionResponseDTO>>
    getMonthlyContributions() {

        return ResponseEntity.ok(
                contributeService.getMonthlyContributions()
        );
    }
}