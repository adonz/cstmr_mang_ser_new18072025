package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeadStageMasterManagementDto {

 	private Integer stageId;
	
 	@NotNull(message="tenent id should not be null")
 	private Integer tenentId;
	
 	@NotNull(message="stage name should not be null")
 	private String stageName;
	
 	private UUID identity;
 	
 	private Boolean isDelete;
 	
 	private Integer createdBy;
 	
 	private Integer updatedBy;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
}
