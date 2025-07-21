package com.incede.nbfc.customer_management.DTOs;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
@Data
public class CustomerCategoryMappingDto {

 	private Integer categoryMappingId;
	
 	@NotNull(message="customer id should not be null")
 	private Integer customerId;
	
 	@NotNull(message="category id should not be null")
 	private Integer categoryId;
	
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

