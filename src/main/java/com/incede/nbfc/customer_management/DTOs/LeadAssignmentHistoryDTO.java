package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadAssignmentHistoryDTO {

    private Integer leadId;
    private Integer assignedTo;
    private String assignedToName;
    private String assignedToRole;
    private LocalDateTime assignedOn;
    private Integer createdBy;
    private Boolean isDelete;
}

