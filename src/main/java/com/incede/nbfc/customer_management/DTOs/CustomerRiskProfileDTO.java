package com.incede.nbfc.customer_management.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRiskProfileDTO {
    private Integer riskId;
    private Integer customerId;
    private Integer assessmentTypeId;
    private BigDecimal riskScore;
    private Integer riskCategory;
    private Date assessmentDate;
    private String riskReason;
    private Boolean isDelete;
    private UUID identity;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
