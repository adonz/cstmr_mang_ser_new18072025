package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAdditionalReferenceValueDto {

	private Integer CustomerAdditionalReferenceValueId;
	
	@NotNull(message="Customer Id should not be null")
 	private Integer customerId;
	
 	private String CustomerAdditionalReferenceName;
	
 	private String CustomerAdditionalReferenceValue;
	
 	@NotNull(message="UUid Cannot be Null or empty")
  	private UUID identity;
 	
	private Boolean isDelete;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
 	@NotNull(message="Create by should not be null")
 	private Integer createdBy;
 	
 	private Integer updatedBy;
}
