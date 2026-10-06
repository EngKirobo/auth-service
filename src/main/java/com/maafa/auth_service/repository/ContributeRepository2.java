package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Contribute;
import com.maafa.auth_service.dto.MonthlyContributionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributeRepository2 extends JpaRepository<Contribute, Integer> {

    List<Contribute> findByUserIdOrderByDateDesc(Integer userId);

    @Query("""
        SELECT c
        FROM Contribute c
        ORDER BY c.userId, c.date DESC
    """)
    List<Contribute> findAllContributions();

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
            user_id,
            YEAR(`Date`) DESC,
            MONTH(`Date`) DESC
        """,
        nativeQuery = true)
    List<MonthlyContributionProjection> findMonthlyContributions();
}