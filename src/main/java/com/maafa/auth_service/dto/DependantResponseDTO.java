package com.maafa.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DependantResponseDTO {

    private Long id;

    private Long userId;

    private String name;

    private String relationship;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}