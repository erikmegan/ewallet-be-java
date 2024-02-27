package com.flip.assignment.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TopUpBalanceServiceRequest {
    private BigDecimal amount;
    private String token;
}
