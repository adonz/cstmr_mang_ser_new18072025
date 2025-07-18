package com.incede.nbfc.customer_management.DTOs;

import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerKYCDTO {

    private Integer kycId;
    
    @NotNull
    private Integer customerId;
    
    @NotNull
    private Integer documentId;
    
    @NotNull
    private String documentType;
    
    @NotNull
    private String idNumber;
    
    @NotNull
    private String placeOfIssue;
    
    @NotNull
    private String issuingAuthority;
    
    @NotNull
    private Date validFrom;
    
    @NotNull
    private Date validTo;
    
    @NotNull
    private Integer documentFileId;	
    
    @NotNull
    private Boolean isVerified;
    
    private Boolean isActive;
    private UUID identity;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}