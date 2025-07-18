package com.incede.nbfc.customer_management.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerContactDto {

    private Integer contactId;

    //@NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Contact type is required")
    @Min(value = 1, message = "Contact type must be between 1 and 4")
    @Max(value = 4, message = "Contact type must be between 1 and 4")
    private Integer contactType; // 1=Mobile, 2=WhatsApp, 3=Landline, 4=Email

   // @NotBlank(message = "Contact value is required")
    private String contactValue;

    //@NotNull
    private Boolean isVerified = false;

   // @NotNull
    private Boolean isActive = true;

    private Boolean isPrimary = false;
    private Boolean isDel = false;

    //@NotNull(message = "Identity UUID is required")
    private UUID identity;

//    @NotNull(message = "Created By is required")
    private Integer createdBy;

    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
