package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.TransactionDTO;
import com.maafa.auth_service.dto.TransactionResponseDTO;
import com.maafa.auth_service.entity.Transaction;
import com.maafa.auth_service.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;


    // ============================================================
    // CREATE
    // ============================================================

    public TransactionResponseDTO create(
            Integer userId,
            TransactionDTO request
    ) {

        Transaction transaction =
                Transaction.builder()
                        .userId(userId)
                        .collectId(request.getCollectId())
                        .status(request.getStatus())
                        .verified(request.getVerified())
                        .deleted(
                                request.getDeleted() != null
                                        ? request.getDeleted()
                                        : false
                        )
                        .build();

        Transaction saved =
                transactionRepository.save(transaction);

        return toResponse(saved);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAll(
            Integer userId
    ) {

        return transactionRepository
                .findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ============================================================
    // GET ALL ACTIVE
    // ============================================================

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getActive(
            Integer userId
    ) {

        return transactionRepository
                .findByUserIdAndDeletedFalse(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public TransactionResponseDTO getById(
            Integer userId,
            Integer id
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        if (!transaction.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to access this transaction"
            );
        }

        return toResponse(transaction);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    public TransactionResponseDTO update(
            Integer userId,
            Integer id,
            TransactionDTO request
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        if (!transaction.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to update this transaction"
            );
        }

        transaction.setCollectId(
                request.getCollectId()
        );

        transaction.setStatus(
                request.getStatus()
        );

        transaction.setVerified(
                request.getVerified()
        );

        if (request.getDeleted() != null) {

            transaction.setDeleted(
                    request.getDeleted()
            );
        }

        Transaction updated =
                transactionRepository.save(transaction);

        return toResponse(updated);
    }


    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            Integer userId,
            Integer id
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction not found"
                                )
                        );

        if (!transaction.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to delete this transaction"
            );
        }

        transactionRepository.delete(transaction);
    }


    // ============================================================
    // CONVERT ENTITY -> RESPONSE DTO
    // ============================================================

    private TransactionResponseDTO toResponse(
            Transaction transaction
    ) {

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .collectId(transaction.getCollectId())
                .status(transaction.getStatus())
                .verified(transaction.getVerified())
                .deleted(transaction.getDeleted())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
