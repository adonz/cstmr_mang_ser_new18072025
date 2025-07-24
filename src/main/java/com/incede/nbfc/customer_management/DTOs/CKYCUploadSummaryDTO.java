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
    private String responsePayload;
    private LocalDateTime uploadDate;
    private String responseSummary;
    private Integer createdBy;
    
    public CKYCUploadSummaryDTO(Integer ckycUploadId, String uploadStatus, String ckycReferenceNo,
            LocalDateTime uploadDate, String responsePayload, Integer createdBy) {
this.ckycUploadId = ckycUploadId;
this.uploadStatus = uploadStatus;
this.ckycReferenceNo = ckycReferenceNo;
this.uploadDate = uploadDate;
this.responsePayload = responsePayload;
this.createdBy = createdBy;
}

}

