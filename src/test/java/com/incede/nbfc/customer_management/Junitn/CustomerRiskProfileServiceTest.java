package com.incede.nbfc.customer_management.Junitn;

import com.incede.nbfc.customer_management.DTOs.CustomerRiskProfileDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerRiskProfile;
import com.incede.nbfc.customer_management.Repositories.CustomerRiskProfileRepository;
import com.incede.nbfc.customer_management.Services.CustomerRiskProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerRiskProfileServiceTest {

    @Mock
    private CustomerRiskProfileRepository repository;

    @InjectMocks
    private CustomerRiskProfileService service;

    private CustomerRiskProfileDTO sampleDTO;
    private CustomerRiskProfile sampleEntity;

    @BeforeEach
    void setUp() {
        sampleDTO = new CustomerRiskProfileDTO();
        sampleDTO.setCustomerId(1);
        sampleDTO.setAssessmentTypeId(2);
        sampleDTO.setRiskScore(BigDecimal.valueOf(88.75));
        sampleDTO.setRiskCategory(3);
        sampleDTO.setRiskReason("Credit history flagged");
        sampleDTO.setCreatedBy(101);
        sampleDTO.setAssessmentDate(new Date());

        sampleEntity = new CustomerRiskProfile();
        sampleEntity.setRiskId(1);
        sampleEntity.setCustomerId(1);
        sampleEntity.setAssessmentTypeId(2);
        sampleEntity.setRiskScore(BigDecimal.valueOf(88.75));
        sampleEntity.setRiskCategory(3);
        sampleEntity.setRiskReason("Credit history flagged");
        sampleEntity.setCreatedBy(101);
        sampleEntity.setAssessmentDate(new Date());
        sampleEntity.setIdentity(UUID.randomUUID());
        sampleEntity.setIsDelete(false);
    }

    @Test
    void testCreateSuccess() {
        when(repository.save(any(CustomerRiskProfile.class))).thenReturn(sampleEntity);

        CustomerRiskProfileDTO result = service.create(sampleDTO);

        assertNotNull(result);
        assertEquals(3, result.getRiskCategory());
        verify(repository).save(any(CustomerRiskProfile.class));
    }

    @Test
    void testCreateValidationFailure() {
        sampleDTO.setRiskScore(new BigDecimal("101.00")); // invalid

        Exception exception = assertThrows(BadRequestException.class, () -> service.create(sampleDTO));
        assertTrue(exception.getMessage().contains("Risk score must be"));
    }

    @Test
    void testUpdateSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleEntity));
        when(repository.save(any(CustomerRiskProfile.class))).thenReturn(sampleEntity);

        sampleDTO.setRiskReason("Updated Reason");
        CustomerRiskProfileDTO result = service.update(1, sampleDTO);

        assertEquals("Updated Reason", result.getRiskReason());
        verify(repository).save(any(CustomerRiskProfile.class));
    }

    @Test
    void testUpdateNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DataNotFoundException.class, () -> service.update(99, sampleDTO));
        assertEquals("Risk profile not found.", exception.getMessage());
    }

    @Test
    void testSoftDeleteSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleEntity));

        service.softDelete(1, 102);

        assertTrue(sampleEntity.getIsDelete());
        assertEquals(102, sampleEntity.getUpdatedBy());
        verify(repository).save(sampleEntity);
    }

    @Test
    void testSoftDeleteNotFound() {
        when(repository.findById(88)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DataNotFoundException.class, () -> service.softDelete(88, 999));
        assertEquals("Risk profile not found.", exception.getMessage());
    }

    @Test
    void testGetAll() {
        sampleEntity.setIsDelete(false); // ✅ Ensure the record is not marked deleted
        when(repository.findByIsDeleteFalse()).thenReturn(Optional.of(sampleEntity));

        List<CustomerRiskProfileDTO> result = service.getAll();

        assertEquals(1, result.size()); // ✅ Should pass now
        assertEquals(sampleEntity.getCustomerId(), result.get(0).getCustomerId());
    }

}
