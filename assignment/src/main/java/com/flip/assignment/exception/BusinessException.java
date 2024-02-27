package com.flip.assignment.exception;

import com.flip.assignment.entity.constant.ErrorCategory;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BusinessException extends RuntimeException{

  private ErrorCategory errorCode;
}
