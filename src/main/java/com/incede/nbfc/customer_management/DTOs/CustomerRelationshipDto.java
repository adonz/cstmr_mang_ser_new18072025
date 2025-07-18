package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerRelationshipDto {
	

	    private Integer relationshipId;
	    
	    @NotNull(message = "Customer ID is required")
	    private Integer customerId;
	    
	    @NotNull(message = "Staff ID is required")
	    private Integer staffId;
	    
	    @NotNull(message = "Assigned From Date is required")
	    private LocalDate assignedFrom;
	    
	    private LocalDate assignedTo;
	    
	    private LocalDate lastContacted;
	    
	
	    private Boolean isActive = true;
	    
	    
	    private UUID identity;
	    
	    private Boolean isDelete;
	    private Integer createdBy;
	    private LocalDateTime createdAt;
	    private Integer updatedBy;
	    private LocalDateTime updatedAt;


	    
}
