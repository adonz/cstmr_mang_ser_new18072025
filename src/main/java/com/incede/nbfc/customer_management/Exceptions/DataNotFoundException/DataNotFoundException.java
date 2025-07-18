package com.incede.nbfc.customer_management.Exceptions.DataNotFoundException;

public class DataNotFoundException extends RuntimeException {
	
	 private final String errorCode;

  public DataNotFoundException(String message) {
      super(message);
      this.errorCode = "";
  }
  
  public DataNotFoundException(String message, String errorCode) {
      super(message);
      this.errorCode = errorCode;
  }

  public String getErrorCode() {
      return errorCode;
  }
}

