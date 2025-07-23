package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductAndServicesDto {

 	private Integer productServiceId;
	
 	@NotNull(message="tenent id should not be null")
 	private Integer tenantId;
	
 	@NotNull(message ="product service (ps_name) should not be null")
 	private String productServiceName;
	
 	@NotNull(message="product service code (ps_code) should not be null")
 	private String productServiceCode;
	
 	@NotNull(message="product service type (ps_type) should not be null")
 	private String productServiceType;
	
 	private String description;
	
 	private Boolean isActive;
 	
 	private Boolean isDelete;
 	
 	private Integer createdBy;
 	
 	private Integer updatedBy;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
}
