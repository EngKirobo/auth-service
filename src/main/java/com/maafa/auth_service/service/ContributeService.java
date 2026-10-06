package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.MonthlyContributionProjection;
import com.maafa.auth_service.dto.MonthlyContributionResponseDTO;
import com.maafa.auth_service.entity.Contribute;
import com.maafa.auth_service.repository.ContributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContributeService {

    private final ContributeRepository contributeRepository;


    // ============================================================
    // GET ALL CONTRIBUTION RECORDS
    // ============================================================

    /**
     * Returns every contribution record from the contribute view.
     */
    public List<Contribute> getAllContributions() {

        return contributeRepository.findAll();
    }


    // ============================================================
    // GET CONTRIBUTIONS FOR ONE USER
    // ============================================================

    /**
     * Returns all contribution records belonging to a particular user.
     *
     * @param userId user ID
     * @return list of contribution records
     */
    public List<Contribute> getUserContributions(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return contributeRepository.findByUserIdOrderByDateDesc(userId);
    }


    // ============================================================
    // GET MONTHLY CONTRIBUTIONS FOR ALL USERS
    // ============================================================

    /**
     * Returns monthly contribution totals for every user.
     *
     * Example:
     *
     * User 1 - January 2026 = 100,000
     * User 1 - February 2026 = 150,000
     * User 2 - January 2026 = 80,000
     */
    public List<MonthlyContributionResponseDTO> getMonthlyContributions() {

        List<MonthlyContributionProjection> results =
                contributeRepository.findMonthlyContributions();

        return results.stream()
                .map(row -> new MonthlyContributionResponseDTO(
                        row.getUserId(),
                        row.getFullname(),
                        row.getYear(),
                        row.getMonth(),
                        row.getTotalAmount()
                ))
                .toList();
    }


    // ============================================================
    // GET MONTHLY CONTRIBUTIONS FOR ONE USER
    // ============================================================

    /**
     * Returns monthly contribution totals for one user.
     *
     * @param userId user ID
     * @return monthly contribution totals
     */
    public List<MonthlyContributionResponseDTO>
    getMonthlyContributionsByUser(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return contributeRepository
                .findMonthlyContributionsByUser(userId)
                .stream()
                .map(row -> new MonthlyContributionResponseDTO(
                        row.getUserId(),
                        row.getFullname(),
                        row.getYear(),
                        row.getMonth(),
                        row.getTotalAmount()
                ))
                .toList();
    }


    // ============================================================
    // GET CONTRIBUTION TOTAL FOR ONE USER
    // ============================================================

    /**
     * Calculates the total amount contributed by one user.
     *
     * @param userId user ID
     * @return total contribution
     */
    public BigDecimal getUserTotalContribution(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return contributeRepository
                .findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(Contribute::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // ============================================================
    // GET GRAND TOTAL OF ALL CONTRIBUTIONS
    // ============================================================

    /**
     * Calculates the total contribution from all users.
     *
     * @return grand total
     */
    public BigDecimal getTotalContributions() {

        return contributeRepository
                .findAll()
                .stream()
                .map(Contribute::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // ============================================================
    // GET CONTRIBUTIONS FOR A SPECIFIC YEAR
    // ============================================================

    /**
     * Returns monthly contributions for all users for a particular year.
     *
     * @param year contribution year
     * @return monthly contributions
     */
    public List<MonthlyContributionResponseDTO>
    getMonthlyContributionsByYear(Integer year) {

        if (year == null) {
            throw new IllegalArgumentException("Year cannot be null");
        }

        return contributeRepository
                .findMonthlyContributionsByYear(year)
                .stream()
                .map(row -> new MonthlyContributionResponseDTO(
                        row.getUserId(),
                        row.getFullname(),
                        row.getYear(),
                        row.getMonth(),
                        row.getTotalAmount()
                ))
                .toList();
    }


    // ============================================================
    // GET MONTHLY CONTRIBUTIONS FOR ONE USER AND YEAR
    // ============================================================

    /**
     * Returns monthly contributions for one user in one year.
     *
     * @param userId user ID
     * @param year year
     * @return monthly contributions
     */
    public List<MonthlyContributionResponseDTO>
    getMonthlyContributionsByUserAndYear(
            Integer userId,
            Integer year) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (year == null) {
            throw new IllegalArgumentException("Year cannot be null");
        }

        return contributeRepository
                .findMonthlyContributionsByUserAndYear(userId, year)
                .stream()
                .map(row -> new MonthlyContributionResponseDTO(
                        row.getUserId(),
                        row.getFullname(),
                        row.getYear(),
                        row.getMonth(),
                        row.getTotalAmount()
                ))
                .toList();
    }
}