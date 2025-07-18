package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerOptionalDetailsDto {

	private  Integer customerId;
	
 	private Integer loanPurposeId;
	
 	@NotNull(message="Residential Status cannot be empty")
 	private String residentialStatus;
	
 	private String educationalLevel;
	
 	private Boolean hasHomeLoan;
	
 	private Integer homeLoanAmount;
	
 	private String homeLoanCompany;
	
 	private Integer languageId;
 	
	private Boolean isDelete = false;
	
	@NotNull(message="Created by should not be null")
 	private Integer createdBy ;
	
 	private LocalDateTime createdAt;
	
 	private Integer updatedBy;
	
 	private LocalDateTime updatedAt;
	
}
