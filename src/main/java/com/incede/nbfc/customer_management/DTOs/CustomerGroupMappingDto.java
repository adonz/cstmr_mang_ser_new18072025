package com.incede.nbfc.customer_management.DTOs;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
@Data
public class CustomerGroupMappingDto {
	
	private Integer groupMappingId;
	
	@NotNull(message="customer Id cannot be empty")
 	private Integer customerId;
	
	@NotNull(message="group id cannot be null")
 	private Integer groupId;
	
	@PastOrPresent(message="Assigned date cannot be in the future")
 	private LocalDateTime assignedDate;
	
 	private Boolean isActive;
	
 	private UUID identity;
 	
	private Boolean isDelete;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
  	private Integer createdBy;
 	
 	private Integer updatedBy;
 	
 
}

