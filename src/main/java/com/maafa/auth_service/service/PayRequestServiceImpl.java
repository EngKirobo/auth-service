package com.maafa.auth_service.service;

import com.maafa.auth_service.dto.PayRequestDTO;
import com.maafa.auth_service.dto.PayRequestResponseDTO;
import com.maafa.auth_service.entity.PayRequest;
import com.maafa.auth_service.repository.PayRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayRequestServiceImpl
        implements PayRequestService {

    private final PayRequestRepository payRequestRepository;

    @Override
    public PayRequestResponseDTO create(
            Integer userId,
            PayRequestDTO request
    ) {

        PayRequest payRequest = PayRequest.builder()
                .userId(userId)
                .details(request.getDetails())
                .amount(request.getAmount())
                .authorised(
                        request.getAuthorised() != null
                                ? request.getAuthorised()
                                : false
                )
                .paid(
                        request.getPaid() != null
                                ? request.getPaid()
                                : false
                )
                .remarks(request.getRemarks())
                .build();

        PayRequest saved =
                payRequestRepository.save(payRequest);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayRequestResponseDTO> getAll(
            Integer userId
    ) {

        return payRequestRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PayRequestResponseDTO getById(
            Integer userId,
            Integer id
    ) {

        PayRequest payRequest =
                payRequestRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Payment request not found"
                                )
                        );

        return mapToResponse(payRequest);
    }

    @Override
    public PayRequestResponseDTO update(
            Integer userId,
            Integer id,
            PayRequestDTO request
    ) {

        PayRequest payRequest =
                payRequestRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Payment request not found"
                                )
                        );

        payRequest.setDetails(
                request.getDetails()
        );

        payRequest.setAmount(
                request.getAmount()
        );

        if (request.getAuthorised() != null) {
            payRequest.setAuthorised(
                    request.getAuthorised()
            );
        }

        if (request.getPaid() != null) {
            payRequest.setPaid(
                    request.getPaid()
            );
        }

        payRequest.setRemarks(
                request.getRemarks()
        );

        PayRequest updated =
                payRequestRepository.save(payRequest);

        return mapToResponse(updated);
    }

    @Override
    public void delete(
            Integer userId,
            Integer id
    ) {

        PayRequest payRequest =
                payRequestRepository
                        .findByIdAndUserId(id, userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Payment request not found"
                                )
                        );

        payRequestRepository.delete(payRequest);
    }

    private PayRequestResponseDTO mapToResponse(
            PayRequest payRequest
    ) {

        return PayRequestResponseDTO.builder()
                .id(payRequest.getId())
                .userId(payRequest.getUserId())
                .details(payRequest.getDetails())
                .amount(payRequest.getAmount())
                .createdAt(payRequest.getCreatedAt())
                .updatedAt(payRequest.getUpdatedAt())
                .authorised(payRequest.getAuthorised())
                .paid(payRequest.getPaid())
                .remarks(payRequest.getRemarks())
                .build();
    }
}