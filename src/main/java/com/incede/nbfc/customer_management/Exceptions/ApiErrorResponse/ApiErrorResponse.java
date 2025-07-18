package com.incede.nbfc.customer_management.Exceptions.ApiErrorResponse;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ApiErrorResponse {
 
  private Date timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}
