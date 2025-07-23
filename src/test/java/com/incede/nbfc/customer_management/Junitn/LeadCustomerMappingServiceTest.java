package com.incede.nbfc.customer_management.Junitn;

import com.incede.nbfc.customer_management.DTOs.LeadCustomerMappingDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.LeadCustomerMapping;
import com.incede.nbfc.customer_management.Repositories.LeadCustomerMappingRepository;
import com.incede.nbfc.customer_management.Repositories.LeadMasterRepository;
import com.incede.nbfc.customer_management.Services.LeadCustomerMappingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadCustomerMappingServiceTest {

    @Mock private LeadCustomerMappingRepository repository;
    @Mock private LeadMasterRepository leadRepository;

    @InjectMocks private LeadCustomerMappingService service;

    private LeadCustomerMappingDTO dto;
    private LeadCustomerMapping entity;

    @BeforeEach
    void setUp() {
        dto = new LeadCustomerMappingDTO();
        dto.setLeadId(10);
        dto.setCustomerId(100);
        dto.setCreatedBy(999);
        dto.setUpdatedBy(888);

        entity = new LeadCustomerMapping();
        entity.setMappingId(1);
        entity.setLeadId(10);
        entity.setCustomerId(100);
        entity.setIsDelete(false);
        entity.setCreatedBy(999);
        entity.setUpdatedBy(888);
    }

    @Test
    void testCreateSuccess() {
        when(leadRepository.existsById(10)).thenReturn(true);
        when(repository.existsByLeadIdAndCustomerIdAndIsDeleteFalse(10, 100)).thenReturn(false);
        when(repository.save(any())).thenReturn(entity);

        LeadCustomerMappingDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(10, result.getLeadId());
        verify(repository).save(any());
    }

    @Test
    void testCreateMissingFields() {
        dto.setLeadId(null);
        dto.setCustomerId(null);
        dto.setCreatedBy(null);

        assertThrows(BadRequestException.class, () -> service.create(dto));
    }

    @Test
    void testCreateLeadNotFound() {
        when(leadRepository.existsById(10)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void testCreateDuplicateMapping() {
        when(leadRepository.existsById(10)).thenReturn(true);
        when(repository.existsByLeadIdAndCustomerIdAndIsDeleteFalse(10, 100)).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(dto));
    }

    @Test
    void testUpdateSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        LeadCustomerMappingDTO result = service.update(1, dto);

        assertEquals(100, result.getCustomerId());
        verify(repository).save(entity);
    }

    @Test
    void testUpdateMappingNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.update(99, dto));
    }

    @Test
    void testSoftDeleteSuccess() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));

        service.softDelete(1, 777);

        assertTrue(entity.getIsDelete());
        assertEquals(777, entity.getUpdatedBy());
        verify(repository).save(entity);
    }

    @Test
    void testSoftDeleteNotFound() {
        when(repository.findById(888)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.softDelete(888, 123));
    }
    @Test
    void testGetAllActive() {
        when(repository.findByIsDeleteFalse()).thenReturn(Optional.of(entity));

        List<LeadCustomerMappingDTO> result = service.getAllActive();

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getCustomerId());
    }
}