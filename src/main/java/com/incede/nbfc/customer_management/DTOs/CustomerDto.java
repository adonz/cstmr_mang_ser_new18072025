package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Integer customerId; 

    
    private Integer tenantId;

    @NotBlank(message = "Customer code is required")
    @Size(max = 15, message = "Customer code must be at most 15 characters")
    //@Pattern(regexp = "^[A-Z]{}[A-Z]{2}\\d{5}$", message = "Customer code format must be like TVMPL00001")
    private String customerCode;

    @NotNull(message = "Salutation ID is required")
    private Integer salutationId;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name must be at most 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @Size(max = 100, message = "Display name must be at most 100 characters")
    private String displayName; // You can compute this in service layer if not provided

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be 10 digits starting with 6-9")
    private String customerMobileno;

    @NotNull(message = "Tax category ID is required")
    private Integer taxCatId;

    @NotNull(message = "Customer status is required")
    private Integer customerStatus;

    @Size(max = 100, message = "CRM Reference ID must be at most 100 characters")
    private String crmReferenceId;

    private UUID identity;

    // from BaseEntity
    private Boolean isDelete;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
