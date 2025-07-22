package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFreezeActionsDto {

    private Integer freezeId;

    @NotNull(message = "Customer ID must not be null")
    private Integer customerId;

    @NotNull(message = "Freeze type must not be null")
    @Pattern(regexp = "PARTIAL|FULL", message = "Freeze type must be either PARTIAL or FULL")
    private String freezeType;  

    @NotNull(message = "Reason is required")
    private String reason;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Pattern(regexp = "ACTIVE|LIFTED", message = "Status must be either ACTIVE or LIFTED")
    private String status;

    private Boolean isDelete;

    @NotNull(message = "Created By must not be null")
    private Integer createdBy;

    private Integer updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
