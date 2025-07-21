package com.incede.nbfc.customer_management.DTOs;


import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LeadMasterDto {

	private Integer leadId;
	
	@NotNull(message="tenant id should not be null")
 	private Integer tenantId;
	
 	private String leadCode;
	
 	@NotNull(message="full name should not be null")
 	private String fullName;
 	
 	@Pattern(
 		    regexp = "^(\\+91[-\\s]?)?(0)?([6-9][0-9]{9}|[1-9][0-9]{1,4}[-\\s]?[0-9]{6,8})$",
 		    message = "Invalid Indian contact number format"
 		)
  	@NotNull(message="contact number should not be null")
 	private String contactNumber;
	
 	private Integer genderId;
	
 	@Email(message="Inavllid email")
 	private String emailId;
 	
 	private String doorNumber;
	
 	private String addressLine1;
	
 	private String addressLine2;
	
 	private String landMark;
	
 	private String placeName;
	
 	private String city;
	
 	private String district;
	
 	private String stateName;
	
 	private Integer countryId;
	
 	private  String pincode;
	
 	private String remarks;
	
 	@NotNull(message="source Id should not be null")
 	private Integer sourceId;
	
 	@NotNull(message ="stage Id should not be null")
 	private Integer stageId;
	
 	@NotNull(message="status Id should not be null")
 	private Integer statusId;
	
 	@NotNull(message="Intrested product Id should not be null")
 	private Integer interestedProductId;
	
 	private UUID identity;
 	
	private Boolean isDelete;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
   	private Integer createdBy;
 	
 	private Integer updatedBy;
	
}

