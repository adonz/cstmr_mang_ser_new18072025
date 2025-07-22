package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class LeadActivityTypeDto {
	
	 private Integer activityTypeId;
	 private Integer tenantId;
	 private String typeName;
	 private UUID identity;
	 private Integer createdBy;
	 private LocalDateTime createdAt;
	 private Integer updatedBy;
	 private LocalDateTime updatedAt;
	 private Boolean isDelete;

}
