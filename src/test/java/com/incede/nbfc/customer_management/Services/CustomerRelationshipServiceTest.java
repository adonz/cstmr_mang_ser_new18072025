package com.incede.nbfc.customer_management.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationshipDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerRelationship;
import com.incede.nbfc.customer_management.Repositories.CustomerRelationshipRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRelationshipServiceTest {

    @Mock
    private CustomerRelationshipRepository repository;

    @InjectMocks
    private CustomerRelationshipService service;

    private CustomerRelationshipDto dto;
    private CustomerRelationship entity;

    @BeforeEach
    void setup() {
        dto = new CustomerRelationshipDto();
        dto.setCustomerId(1);
        dto.setStaffId(101);
        dto.setAssignedFrom(LocalDate.now());
        dto.setCreatedBy(1001);

        entity = new CustomerRelationship();
        entity.setRelationshipId(99);
        entity.setCustomerId(1);
        entity.setStaffId(101);
        entity.setAssignedFrom(LocalDate.now());
        entity.setIsActive(true);
        entity.setIsDelete(false);
        entity.setCreatedBy(1001);
        entity.setIdentity(UUID.randomUUID());
    }

    @Test
    void testCreateRelationshipReturnsDto() {
        when(repository.save(any(CustomerRelationship.class))).thenReturn(entity);

        CustomerRelationshipDto result = service.createRelationship(dto);

        assertNotNull(result);
        assertEquals(99, result.getRelationshipId());
        assertEquals(dto.getCustomerId(), result.getCustomerId());
        verify(repository, times(1)).save(any(CustomerRelationship.class));
    }

    @Test
    void testGetByIdReturnsDto() {
        when(repository.findByRelationshipIdAndIsDeleteFalse(99)).thenReturn(Optional.of(entity));

        CustomerRelationshipDto result = service.getById(99);

        assertEquals(1, result.getCustomerId());
    }

    @Test
    void testGetByIdThrowsExceptionWhenNotFound() {
        when(repository.findByRelationshipIdAndIsDeleteFalse(100)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.getById(100));
    }

    @Test
    void testSoftDeleteMarksAsInactive() {
        when(repository.findById(99)).thenReturn(Optional.of(entity));

        service.softDelete(99, 501);

        assertFalse(entity.getIsActive());
        assertTrue(entity.getIsDelete());
        assertEquals(501, entity.getUpdatedBy());
        verify(repository).save(entity);
    }

    @Test
    void testCloseRelationshipUpdatesFields() {
//        when(repository.findByRelationshipIdAndIsDeleteFalse(99)).thenReturn(Optional.of(entity));
        when(repository.findById(2)).thenReturn(Optional.of(entity)); // Add for safety
        when(repository.save(any(CustomerRelationship.class))).thenReturn(entity);

        LocalDate toDate = LocalDate.now().plusDays(5);
        CustomerRelationshipDto closed = service.closeRelationship(2, toDate, 777);

        assertNotNull(closed);
        assertEquals(toDate, closed.getAssignedTo());
        assertFalse(closed.getIsActive());
        verify(repository).save(any(CustomerRelationship.class));
    }

    @Test
    void testValidationThrowsOnMissingFields() {
        CustomerRelationshipDto badDto = new CustomerRelationshipDto(); // all fields null

        BusinessException ex = assertThrows(BusinessException.class, () -> service.createRelationship(badDto));
        assertEquals("Customer ID is required.", ex.getMessage());
    }
}
