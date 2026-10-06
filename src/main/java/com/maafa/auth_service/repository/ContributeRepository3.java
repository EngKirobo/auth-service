package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Contribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributeRepository3 extends JpaRepository<Contribute, Integer> {

    List<Contribute> findByUserIdOrderByDateDesc(Integer userId);

    @Query("""
        SELECT c
        FROM Contribute c
        ORDER BY c.userId, c.date DESC
    """)
    List<Contribute> findAllContributions();
}