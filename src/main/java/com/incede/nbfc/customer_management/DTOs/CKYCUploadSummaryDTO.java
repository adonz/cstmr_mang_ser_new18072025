package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CKYCUploadSummaryDTO {
    private Integer ckycUploadId;
    private String uploadStatus;
    private String ckycReferenceNo;
    private LocalDateTime uploadDate;
    private String responseSummary;
    private Integer createdBy;
}

