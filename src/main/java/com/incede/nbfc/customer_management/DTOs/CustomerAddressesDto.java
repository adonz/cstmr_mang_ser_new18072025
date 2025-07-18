package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAddressesDto {

 	private Integer addressId;
	
 	@NotNull(message="customer id should not be empty")
 	private Integer customerId;
	
 	@NotNull(message="address Type should not be null and should be in 'Present','Permanent'")
 	private String addressType;
	
 	private String doorNumber;
	
 	private String addressLineOne;
	
 	private String addressLineTwo;
	
 	private String  landMark;
	
 	private String placeName;
	
 	private String city;
	
 	private String district;
	
 	private String stateName;
	
 	private Integer country;
	
 	private String pincode;
	
 	private Boolean isActive;
	
 	private UUID identity;
 	
 	private Boolean isDelete;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
 	@NotNull(message="Create by should not be null")
 	private Integer createdBy;
 	
 	private Integer updatedBy;
	
}
