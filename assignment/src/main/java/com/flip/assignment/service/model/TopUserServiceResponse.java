package com.flip.assignment.service.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
public class TopUserServiceResponse implements Serializable {
    private String username;
    private BigDecimal transactedValue;
}
