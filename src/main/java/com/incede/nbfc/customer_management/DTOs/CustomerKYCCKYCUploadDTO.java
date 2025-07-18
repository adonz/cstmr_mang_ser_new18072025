package com.incede.nbfc.customer_management.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerKYCCKYCUploadDTO {

    private Integer ckycUploadId;
    
    @NotNull
    private Integer customerId;
    
    @NotNull
    private Integer kycId;
    
    @NotNull
    private String uploadStatus;
    
    @NotNull
    private String ckycReferenceNo;
    
    @NotNull
    private String responsePayload;
    
    
    private Boolean isDel;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
    private LocalDateTime uploadDate;
}