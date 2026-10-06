package com.maafa.auth_service.repository;

import com.maafa.auth_service.dto.MonthlyContributionProjection;
import com.maafa.auth_service.entity.Contribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributeRepository
        extends JpaRepository<Contribute, Integer> {


    // ============================================================
    // ALL CONTRIBUTIONS
    // ============================================================

    @Query("""
        SELECT c
        FROM Contribute c
        ORDER BY c.date DESC
    """)
    List<Contribute> findAllContributions();


    // ============================================================
    // CONTRIBUTIONS FOR ONE USER
    // ============================================================

    List<Contribute> findByUserIdOrderByDateDesc(Integer userId);


    // ============================================================
    // MONTHLY CONTRIBUTIONS - ALL USERS
    // ============================================================

    @Query(value = """
        SELECT
            user_id AS userId,
            fullname AS fullname,
            YEAR(`Date`) AS year,
            MONTH(`Date`) AS month,
            SUM(amount) AS totalAmount
        FROM contribute
        GROUP BY
            user_id,
            fullname,
            YEAR(`Date`),
            MONTH(`Date`)
        ORDER BY
            user_id ASC,
            YEAR(`Date`) DESC,
            MONTH(`Date`) DESC
        """,
        nativeQuery = true)
    List<MonthlyContributionProjection> findMonthlyContributions();


    // ============================================================
    // MONTHLY CONTRIBUTIONS - ONE USER
    // ============================================================

    @Query(value = """
        SELECT
            user_id AS userId,
            fullname AS fullname,
            YEAR(`Date`) AS year,
            MONTH(`Date`) AS month,
            SUM(amount) AS totalAmount
        FROM contribute
        WHERE user_id = :userId
        GROUP BY
            user_id,
            fullname,
            YEAR(`Date`),
            MONTH(`Date`)
        ORDER BY
            YEAR(`Date`) DESC,
            MONTH(`Date`) DESC
        """,
        nativeQuery = true)
    List<MonthlyContributionProjection>
    findMonthlyContributionsByUser(
            @Param("userId") Integer userId
    );


    // ============================================================
    // MONTHLY CONTRIBUTIONS - SPECIFIC YEAR
    // ============================================================

    @Query(value = """
        SELECT
            user_id AS userId,
            fullname AS fullname,
            YEAR(`Date`) AS year,
            MONTH(`Date`) AS month,
            SUM(amount) AS totalAmount
        FROM contribute
        WHERE YEAR(`Date`) = :year
        GROUP BY
            user_id,
            fullname,
            YEAR(`Date`),
            MONTH(`Date`)
        ORDER BY
            user_id ASC,
            MONTH(`Date`) ASC
        """,
        nativeQuery = true)
    List<MonthlyContributionProjection>
    findMonthlyContributionsByYear(
            @Param("year") Integer year
    );


    // ============================================================
    // MONTHLY CONTRIBUTIONS - USER + YEAR
    // ============================================================

    @Query(value = """
        SELECT
            user_id AS userId,
            fullname AS fullname,
            YEAR(`Date`) AS year,
            MONTH(`Date`) AS month,
            SUM(amount) AS totalAmount
        FROM contribute
        WHERE user_id = :userId
          AND YEAR(`Date`) = :year
        GROUP BY
            user_id,
            fullname,
            YEAR(`Date`),
            MONTH(`Date`)
        ORDER BY
            MONTH(`Date`) ASC
        """,
        nativeQuery = true)
    List<MonthlyContributionProjection>
    findMonthlyContributionsByUserAndYear(
            @Param("userId") Integer userId,
            @Param("year") Integer year
    );
}