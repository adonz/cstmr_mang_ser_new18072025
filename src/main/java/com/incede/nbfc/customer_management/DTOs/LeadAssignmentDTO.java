package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadAssignmentDTO {

    private Integer assignmentId;
    private Integer leadId;
    private Integer assignedTo;
    private LocalDateTime assignedOn;
    private Boolean deletedFlag;
    private UUID identityGuid;

    private Integer createdBy;
    private LocalDateTime createdAt;

    private Integer updatedBy;
    private LocalDateTime updatedAt;
}

