package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Integer userId);

    List<Transaction> findByCollectId(Integer collectId);

    List<Transaction> findByUserIdAndDeletedFalse(Integer userId);
}