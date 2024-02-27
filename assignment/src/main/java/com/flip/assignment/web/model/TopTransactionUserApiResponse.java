package com.flip.assignment.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopTransactionUserApiResponse implements Serializable {

    private String username;
    private BigDecimal amount;


    private String type;

    public BigDecimal getAmount() {
        if(type.equals("DEBIT")){
            return amount.negate();
        }
        return amount;
    }
}
