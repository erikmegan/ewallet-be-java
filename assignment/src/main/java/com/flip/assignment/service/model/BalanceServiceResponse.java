package com.flip.assignment.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class BalanceServiceResponse {
    private BigDecimal balance;
}
