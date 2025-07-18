package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRelationDetailsDto {

	 private Integer customerId;
	    private Integer maritalStatusId;
	    private Integer genderId;
	    private LocalDate dob;
	    private Boolean isMinor;
	    private String fatherName;
	    private String motherName;
	    private Integer annualIncome;
	    private String customerListType;
	    private Integer customerValueScore;
	    private Integer loyaltyPoints;

    private Boolean isDelete;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}