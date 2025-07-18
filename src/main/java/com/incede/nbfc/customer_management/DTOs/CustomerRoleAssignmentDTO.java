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
public class CustomerRoleAssignmentDTO {

    private Integer roleAssignmentId;
    private Integer customerId;
    private Integer roleId;
    private Integer assignedBy;
    private Date assignedDate;
    private Date expirationDate;
    private Boolean isActive;
    private Boolean isDel;
    private UUID identity;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;

}

