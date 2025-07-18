package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CKYCUploadSummaryDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CKYCUploadService;
import com.incede.nbfc.customer_management.Services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/ckyc")
public class CKYCUploadController {

    private final CKYCUploadService ckycUploadService;

    private final CustomerService customerService;
    
    public CKYCUploadController(CKYCUploadService ckycUploadService , CustomerService customerService) {
        this.ckycUploadService = ckycUploadService;
        this.customerService = customerService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<ResponseWrapper<String>> initiateUpload(
    		@Valid
            @RequestParam Integer customerId,
            @RequestParam Integer kycId,
            @RequestParam Integer createdBy
    ) {
        ckycUploadService.initiateCKYCUpload(customerId, kycId, createdBy);
        return ResponseEntity.ok(ResponseWrapper.success(null, "CKYC upload initiated successfully"));
    }
    
    @PutMapping("/record-response")
    public ResponseEntity<ResponseWrapper<String>> recordUploadResponse(
            @RequestParam Integer ckycUploadId,
            @RequestParam String uploadStatus, // SUCCESS or FAILED
            @RequestParam(required = false) String ckycReferenceNo,
            @RequestParam String responsePayload,
            @RequestParam Integer updatedBy
    ) {
        ckycUploadService.recordCKYCResponse(
            ckycUploadId, uploadStatus, ckycReferenceNo, responsePayload, updatedBy
        );

        return ResponseEntity.ok(ResponseWrapper.success(null, "CKYC response recorded successfully"));

    }

    @GetMapping("/history")
    public ResponseEntity<ResponseWrapper<List<CKYCUploadSummaryDTO>>> getUploadHistory(
       @RequestParam Integer customerId) {
       List<CKYCUploadSummaryDTO> history = ckycUploadService.getUploadHistoryByCustomerId(customerId);
       return ResponseEntity.ok(ResponseWrapper.success(history, "CKYC upload history fetched"));
    }
    
    @DeleteMapping("/soft-delete")
    public ResponseEntity<ResponseWrapper<String>> softDeleteFailedUpload(
            @RequestParam Integer ckycUploadId,
            @RequestParam Integer userId
    ) {
        ckycUploadService.softDeleteFailedUpload(ckycUploadId, userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "CKYC upload successfully soft-deleted."));
    }
    
    @PostMapping("/retry")
    public ResponseEntity<ResponseWrapper<String>> retryUpload(
            @RequestParam Integer ckycUploadId,
            @RequestParam Integer createdBy
    ) {
        ckycUploadService.retryFailedUpload(ckycUploadId, createdBy);
        return ResponseEntity.ok(ResponseWrapper.success(null, "CKYC retry initiated."));
    }


}

