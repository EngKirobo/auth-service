package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository
        extends JpaRepository<Contribution, Integer> {

    List<Contribution> findByYear(Integer year);

    List<Contribution> findByUserId(Integer userId);

    List<Contribution> findByUserIdAndYear(Integer userId, Integer year);
}