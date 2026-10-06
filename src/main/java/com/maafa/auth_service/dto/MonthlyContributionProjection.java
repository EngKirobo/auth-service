package com.maafa.auth_service.dto;

import java.math.BigDecimal;

public interface MonthlyContributionProjection {

    Integer getUserId();

    String getFullname();

    Integer getYear();

    Integer getMonth();

    BigDecimal getTotalAmount();
}