package com.incede.nbfc.customer_management.Junitn;


import com.incede.nbfc.customer_management.DTOs.CustomerAssessmentTypeDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerAssessmentType;
import com.incede.nbfc.customer_management.Repositories.CustomerAssessmentTypeRepository;
import com.incede.nbfc.customer_management.Services.CustomerAssessmentTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CustomerAssessmentTypeServiceTest {

    @Mock private CustomerAssessmentTypeRepository repository;

    @InjectMocks private CustomerAssessmentTypeService service;

    private CustomerAssessmentTypeDTO dto;
    private CustomerAssessmentType entity;

    @BeforeEach
    void setup() {
        dto = new CustomerAssessmentTypeDTO();
        dto.setAssessmentTypeId(1);
        dto.setAssessmentName("Risk Evaluation");
        dto.setDescription("Initial credit risk analysis");
        dto.setCreatedBy(101);
        dto.setUpdatedBy(202);

        entity = new CustomerAssessmentType();
        entity.setAssessmentTypeId(1);
        entity.setAssessmentName("Risk Evaluation");
        entity.setDescription("Initial credit risk analysis");
        entity.setCreatedBy(101);
        entity.setUpdatedBy(202);
        entity.setIsActive(true);
        entity.setIsDelete(false);
        entity.setIdentity(UUID.randomUUID());
    }

    @Test
    void testCreateSuccess() {
        when(repository.save(any())).thenReturn(entity);

        CustomerAssessmentTypeDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("Risk Evaluation", result.getAssessmentName());
        verify(repository).save(any());
    }

    @Test
    void testCreateValidationFails() {
        dto.setAssessmentName("  ");
        assertThrows(BadRequestException.class, () -> service.create(dto));
    }

    @Test
    void testCreateConstraintViolation() {
        when(repository.save(any())).thenThrow(new DataIntegrityViolationException("Constraint failed"));
        assertThrows(ConflictException.class, () -> service.create(dto));
    }

    @Test
    void testCreateUnexpectedError() {
        when(repository.save(any())).thenThrow(new RuntimeException("Unexpected"));
        assertThrows(BusinessException.class, () -> service.create(dto));
    }

    @Test
    void testUpdateSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        dto.setAssessmentName("Updated Name");
        CustomerAssessmentTypeDTO result = service.update(1, dto);

        assertEquals("Updated Name", result.getAssessmentName());
        verify(repository).save(entity);
    }

    @Test
    void testUpdateNotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.update(999, dto));
    }

    @Test
    void testUpdateValidationFails() {
        dto.setAssessmentName("");
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        assertThrows(BadRequestException.class, () -> service.update(1, dto));
    }

    @Test
    void testToggleStatusSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));

        service.toggleStatus(1);

        assertFalse(entity.getIsActive());
        verify(repository).save(entity);
    }

    @Test
    void testToggleStatusNotFound() {
        when(repository.findById(777)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.toggleStatus(777));
    }

    @Test
    void testSoftDeleteSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        service.softDelete(1);

        assertTrue(entity.getIsDelete());
        verify(repository).save(entity);
    }

    @Test
    void testSoftDeleteNotFound() {
        when(repository.findById(888)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.softDelete(888));
    }

    @Test
    void testGetAllActiveSuccess() {
        when(repository.findByIsDeleteFalse()).thenReturn(List.of(entity));

        List<CustomerAssessmentTypeDTO> result = service.getAllActive();

        assertEquals(1, result.size());
        assertEquals("Risk Evaluation", result.get(0).getAssessmentName());
    }

    @Test
    void testGetAllActiveFails() {
        when(repository.findByIsDeleteFalse()).thenThrow(new RuntimeException("DB error"));
        assertThrows(BusinessException.class, () -> service.getAllActive());
    }
}

