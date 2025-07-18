package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CKYCUploadSummaryDTO;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerKYCCKYCUpload;
import com.incede.nbfc.customer_management.Repositories.CustomerKYCCKYCUploadRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerKYCRepository;

import jakarta.transaction.Transactional;

@Service
public class CKYCUploadService {

    @Autowired
    private CustomerKYCCKYCUploadRepository ckycUploadRepository;

    @Autowired
    private CustomerKYCRepository kycRepository;

    @Transactional
    public void initiateCKYCUpload(Integer customerId, Integer kycId, Integer createdBy) {
        // 🔍 Check for existing PENDING upload
        boolean pendingExists = ckycUploadRepository
            .existsByCustomerIdAndUploadStatusAndIsDeleteFalse(customerId, "PENDING");

        if (pendingExists) {
            throw new BusinessException("A CKYC upload is already in progress for this customer.");
        }

        // 🔒 Verify valid KYC reference
        boolean validKYC = kycRepository
            .existsByKycIdAndCustomerIdAndIsActiveTrueAndIsDeleteFalse(kycId, customerId);

        if (!validKYC) {
            throw new BusinessException("Invalid or inactive KYC document reference.");
        }

        // 📝 Insert CKYC upload record
        CustomerKYCCKYCUpload upload = new CustomerKYCCKYCUpload();
        upload.setCustomerId(customerId);
        upload.setKycId(kycId);
        upload.setUploadStatus("PENDING");
        upload.setCreatedBy(createdBy);
        upload.setUploadDate(LocalDateTime.now());

        ckycUploadRepository.save(upload);
    }
    
    @Transactional
    public void recordCKYCResponse(Integer uploadId, String uploadStatus,
                                   String ckycReferenceNo, String payload,
                                   Integer updatedBy) {

        CustomerKYCCKYCUpload upload = ckycUploadRepository.findByCkycUploadIdAndIsDeleteFalse(uploadId)
            .orElseThrow(() -> new BusinessException("CKYC upload record not found or deleted."));

        if (!"SUCCESS".equalsIgnoreCase(uploadStatus) && !"FAILED".equalsIgnoreCase(uploadStatus)) {
            throw new BusinessException("Invalid upload status. Must be either SUCCESS or FAILED.");
        }

        upload.setUploadStatus(uploadStatus.toUpperCase());
        upload.setResponsePayload(payload);
        upload.setUpdatedBy(updatedBy);
        upload.setUpdatedAt(LocalDateTime.now());

        // Only set reference number if status is SUCCESS
        upload.setCkycReferenceNo("SUCCESS".equalsIgnoreCase(uploadStatus) ? ckycReferenceNo : null);

        ckycUploadRepository.save(upload);
    }
    
    @Transactional
    public List<CKYCUploadSummaryDTO> getUploadHistoryByCustomerId(Integer customerId) {
        List<CustomerKYCCKYCUpload> uploads = ckycUploadRepository
            .findByCustomerIdAndIsDeleteFalseOrderByUploadDateDesc(customerId);

        return uploads.stream()
            .map(upload -> new CKYCUploadSummaryDTO(
                upload.getCkycUploadId(),
                upload.getUploadStatus(),
                upload.getCkycReferenceNo(),
                upload.getUploadDate(),
                summarizePayload(upload.getResponsePayload()),
                upload.getCreatedBy()
            ))
            .collect(Collectors.toList());
    }

    // Helper to trim response to 200 chars
    private String summarizePayload(String payload) {
        return (payload != null && payload.length() > 200)
            ? payload.substring(0, 200) + "..."
            : payload;
    }
    
    @Transactional
    public void softDeleteFailedUpload(Integer uploadId, Integer userId) {
        CustomerKYCCKYCUpload upload = ckycUploadRepository.findByCkycUploadIdAndIsDeleteFalse(uploadId)
            .orElseThrow(() -> new BusinessException("Upload record not found or already deleted."));

        if (!"FAILED".equalsIgnoreCase(upload.getUploadStatus())) {
            throw new BusinessException("Only CKYC uploads with status FAILED can be soft-deleted.");
        }

        upload.setIsDelete(true);
        upload.setUpdatedBy(userId);
        upload.setUpdatedAt(LocalDateTime.now());

        ckycUploadRepository.save(upload);
    }
    
    @Transactional
    public void retryFailedUpload(Integer uploadId, Integer createdBy) {
        CustomerKYCCKYCUpload failedUpload = ckycUploadRepository
            .findByCkycUploadIdAndUploadStatusAndIsDeleteFalse(uploadId, "FAILED")
            .orElseThrow(() -> new BusinessException("Retry not allowed. Record must exist, not be deleted, and have status FAILED."));

        CustomerKYCCKYCUpload retryRecord = new CustomerKYCCKYCUpload();
        retryRecord.setCustomerId(failedUpload.getCustomerId());
        retryRecord.setKycId(failedUpload.getKycId());
        retryRecord.setUploadStatus("PENDING");
        retryRecord.setCreatedBy(createdBy);
        retryRecord.setUploadDate(LocalDateTime.now());
        retryRecord.setIsDelete(false);

        ckycUploadRepository.save(retryRecord);
    }


}
