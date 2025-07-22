package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadCustomerMappingDTO {
    private Integer mappingId;
    private Integer leadId;
    private Integer customerId;
    private Date conversionDate;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
    private Boolean isDelete;
    private UUID identity;
}
