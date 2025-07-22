package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LeadSourceMasterDto {
	
	
	    private Integer sourceId;
	    
		
	    private Integer tenantId;
		
		
	    private String sourceName;
	  
	    private UUID identity;
		private Boolean isDelete;
	 	
	 	private LocalDateTime createdAt;
	 	
	 	private LocalDateTime updatedAt;
	 	
	 	  @JsonProperty("createdBy")
	 	private Integer createdBy;
	 	
	 	private Integer updatedBy;
	    

}
