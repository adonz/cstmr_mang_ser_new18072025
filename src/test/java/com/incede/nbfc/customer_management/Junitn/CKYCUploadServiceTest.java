package com.incede.nbfc.customer_management.Junitn;

import com.incede.nbfc.customer_management.DTOs.CKYCUploadSummaryDTO;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerKYCCKYCUpload;
import com.incede.nbfc.customer_management.Repositories.CustomerKYCCKYCUploadRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerKYCRepository;
import com.incede.nbfc.customer_management.Services.CKYCUploadService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CKYCUploadServiceTest {

    @Mock private CustomerKYCCKYCUploadRepository uploadRepository;
    @Mock private CustomerKYCRepository kycRepository;

    @InjectMocks private CKYCUploadService service;

    private CustomerKYCCKYCUpload upload;

    @BeforeEach
    void setup() {
        upload = new CustomerKYCCKYCUpload();
        upload.setCkycUploadId(1);
        upload.setCustomerId(1001);
        upload.setKycId(2002);
        upload.setUploadStatus("FAILED");
        upload.setCreatedBy(500);
        upload.setUploadDate(LocalDateTime.now());
        upload.setIsDelete(false);
        upload.setResponsePayload("X".repeat(250)); // simulate long CKYC response
    }

    @Test
    void testInitiateCKYCUploadSuccess() {
        when(uploadRepository.existsByCustomerIdAndUploadStatusAndIsDeleteFalse(1001, "PENDING"))
            .thenReturn(false);
        when(kycRepository.existsByKycIdAndCustomerIdAndIsActiveTrueAndIsDeleteFalse(2002, 1001))
            .thenReturn(true);

        assertDoesNotThrow(() -> service.initiateCKYCUpload(1001, 2002, 500));
        verify(uploadRepository).save(any());
    }

    @Test
    void testInitiateCKYCUploadFailsIfPendingExists() {
        when(uploadRepository.existsByCustomerIdAndUploadStatusAndIsDeleteFalse(1001, "PENDING"))
            .thenReturn(true);

        assertThrows(BusinessException.class, () -> service.initiateCKYCUpload(1001, 2002, 500));
    }

    @Test
    void testInitiateCKYCUploadFailsIfInvalidKYC() {
        when(uploadRepository.existsByCustomerIdAndUploadStatusAndIsDeleteFalse(1001, "PENDING"))
            .thenReturn(false);
        when(kycRepository.existsByKycIdAndCustomerIdAndIsActiveTrueAndIsDeleteFalse(2002, 1001))
            .thenReturn(false);

        assertThrows(BusinessException.class, () -> service.initiateCKYCUpload(1001, 2002, 500));
    }

    @Test
    void testRecordCKYCResponseSuccess() {
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(1)).thenReturn(Optional.of(upload));

        assertDoesNotThrow(() ->
            service.recordCKYCResponse(1, "SUCCESS", "CKYC123", "valid response", 777));
        assertEquals("CKYC123", upload.getCkycReferenceNo());
        assertEquals("SUCCESS", upload.getUploadStatus());
        verify(uploadRepository).save(upload);
    }

    @Test
    void testRecordCKYCResponseFailsWithInvalidStatus() {
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(1)).thenReturn(Optional.of(upload));

        assertThrows(BusinessException.class,
            () -> service.recordCKYCResponse(1, "IN_PROGRESS", null, "payload", 777));
    }

    @Test
    void testRecordCKYCResponseFailsIfUploadMissing() {
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(99)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class,
            () -> service.recordCKYCResponse(99, "SUCCESS", "CKYC123", "payload", 777));
    }

    @Test
    void testGetUploadHistoryWithSummarizedPayload() {
        when(uploadRepository.findByCustomerIdAndIsDeleteFalseOrderByUploadDateDesc(1001))
            .thenReturn(List.of(upload));

        List<CKYCUploadSummaryDTO> result = service.getUploadHistoryByCustomerId(1001);

        assertEquals(1, result.size());
        String payload = result.get(0).getResponsePayload();
        assertTrue(payload.endsWith("..."));
        assertTrue(payload.length() > 200);
    }

    @Test
    void testSoftDeleteFailedUploadSuccess() {
        upload.setUploadStatus("FAILED");
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(1)).thenReturn(Optional.of(upload));

        assertDoesNotThrow(() -> service.softDeleteFailedUpload(1, 888));
        assertTrue(upload.getIsDelete());
        assertEquals(888, upload.getUpdatedBy());
    }

    @Test
    void testSoftDeleteFailedUploadFailsIfNotFailedStatus() {
        upload.setUploadStatus("SUCCESS");
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(1)).thenReturn(Optional.of(upload));

        assertThrows(BusinessException.class, () -> service.softDeleteFailedUpload(1, 888));
    }

    @Test
    void testSoftDeleteFailedUploadFailsIfMissing() {
        when(uploadRepository.findByCkycUploadIdAndIsDeleteFalse(99)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.softDeleteFailedUpload(99, 888));
    }

    @Test
    void testRetryFailedUploadSuccess() {
        when(uploadRepository.findByCkycUploadIdAndUploadStatusAndIsDeleteFalse(1, "FAILED"))
            .thenReturn(Optional.of(upload));

        assertDoesNotThrow(() -> service.retryFailedUpload(1, 500));
        verify(uploadRepository).save(any());
    }

    @Test
    void testRetryFailedUploadFailsIfMissing() {
        when(uploadRepository.findByCkycUploadIdAndUploadStatusAndIsDeleteFalse(999, "FAILED"))
            .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.retryFailedUpload(999, 500));
    }
}
