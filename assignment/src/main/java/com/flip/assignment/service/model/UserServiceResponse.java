package com.flip.assignment.service.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class UserServiceResponse {
  private Long id;
  private String username;
  private Date createdDate;
  private Date updatedDate;
}
