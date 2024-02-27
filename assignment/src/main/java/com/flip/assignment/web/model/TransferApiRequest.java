package com.flip.assignment.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferApiRequest implements Serializable {

  private BigDecimal amount;
  @JsonProperty("to_username")
  private String toUsername;
}
