package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerFollowupDto {
    
    private Integer followupId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Staff ID is required")
    private Integer staffId;

    @NotNull(message = "Follow-up date is required")
    @PastOrPresent(message = "Follow-up date cannot be in the future")
    private LocalDate followupDate;

    @NotNull(message = "Follow-up type is required")
    @Min(value = 1, message = "Invalid follow-up type")
    @Max(value = 4, message = "Invalid follow-up type")
    private Integer followupType;

    private String followupNotes;

    private LocalDate nextFollowupDate;

    @NotNull(message = "Status is required")
    @Min(value = 1, message = "Invalid follow-up status")
    @Max(value = 3, message = "Invalid follow-up status")
    private Integer status;

    private UUID identity;

    private Boolean isDelete;

    private Integer createdBy;

    private Integer updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
