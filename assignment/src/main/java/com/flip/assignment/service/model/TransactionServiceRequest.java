package com.flip.assignment.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TransactionServiceRequest {
  private BigDecimal amount;
  private String to;
  private String from;
}
