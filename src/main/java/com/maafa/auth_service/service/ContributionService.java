package com.maafa.auth_service.service;

import com.maafa.auth_service.entity.Contribution;
import com.maafa.auth_service.repository.ContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository repository;

    public List<Contribution> getAll() {
        return repository.findAll();
    }

    public List<Contribution> getByYear(Integer year) {
        return repository.findByYear(year);
    }

    public List<Contribution> getByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public List<Contribution> getByUserIdAndYear(
            Integer userId,
            Integer year
    ) {
        return repository.findByUserIdAndYear(userId, year);
    }
}