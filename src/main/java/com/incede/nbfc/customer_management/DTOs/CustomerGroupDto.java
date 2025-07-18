package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;


@Data
public class CustomerGroupDto {
	

	    private Integer groupId;
	    private Integer tenantId;
	    private String groupName;
	    private String description;
	    private Boolean isActive = true;
	    private UUID identity;
	    private Boolean isDelete;
	    private Integer createdBy;
	    private LocalDateTime createdAt;
	    private Integer updatedBy;
	    private LocalDateTime updatedAt;
	    

}
