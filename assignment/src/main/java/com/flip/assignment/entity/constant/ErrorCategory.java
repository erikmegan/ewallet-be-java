package com.flip.assignment.entity.constant;

import org.springframework.http.HttpStatus;

public enum ErrorCategory {

  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),

  USER_EXIST(HttpStatus.CONFLICT, "Username alreaady exists"),

  DESTINATION_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Destination user not found"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
  INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "Insufficient balance"),
  INVALID_TOPUP_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid topup amount");

  private final HttpStatus code;
  private final String message;

  ErrorCategory(HttpStatus code, String message) {
    this.message = message;
    this.code = code;
  }

  public HttpStatus getCode(){
    return this.code;
  }

  public String getMessage(){
    return this.message;
  }
}
