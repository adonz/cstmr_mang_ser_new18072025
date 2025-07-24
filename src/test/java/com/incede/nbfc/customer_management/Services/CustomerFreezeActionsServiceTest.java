package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.CustomerFreezeActionsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.*;
import com.incede.nbfc.customer_management.Models.CustomerFreezeActions;
import com.incede.nbfc.customer_management.Repositories.CustomerFreezeActionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFreezeActionsServiceTest {

    @Mock
    private CustomerFreezeActionsRepository freezeActionsRepository;

    @InjectMocks
    private CustomerFreezeActionsService service;

    private CustomerFreezeActionsDto validDto;
    private CustomerFreezeActions entity;

    @BeforeEach
    void setUp() {
        validDto = new CustomerFreezeActionsDto();
        validDto.setCustomerId(1);
        validDto.setFreezeType("PARTIAL");
        validDto.setReason("Test reason");
        validDto.setEffectiveFrom(LocalDate.now());
        validDto.setStatus("ACTIVE");
        validDto.setCreatedBy(1);
        validDto.setCreatedAt(LocalDateTime.now());

        entity = new CustomerFreezeActions();
        entity.setFreezeId(1);
        entity.setCustomerId(1);
        entity.setFreezeType("PARTIAL");
        entity.setReason("Test reason");
        entity.setEffectiveFrom(LocalDate.now());
        entity.setStatus("ACTIVE");
        entity.setCreatedBy(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsDelete(false);
    }

    @Test
    void createCustomerFreezeAction_ValidInput_ReturnsDto() {
        when(freezeActionsRepository.existsByCustomerIdAndStatusAndIsDeleteFalse(1, "ACTIVE")).thenReturn(false);
        when(freezeActionsRepository.save(any(CustomerFreezeActions.class))).thenReturn(entity);

        CustomerFreezeActionsDto result = service.createCustomerFreezeAction(validDto);

        assertNotNull(result);
        assertEquals(validDto.getCustomerId(), result.getCustomerId());
        assertEquals("ACTIVE", result.getStatus());
        verify(freezeActionsRepository).save(any(CustomerFreezeActions.class));
    }

    @Test
    void createCustomerFreezeAction_InvalidFreezeType_ThrowsException() {
        validDto.setFreezeType("INVALID");

        assertThrows(IllegalArgumentException.class, () -> service.createCustomerFreezeAction(validDto));
        verify(freezeActionsRepository, never()).save(any());
    }

    @Test
    void createCustomerFreezeAction_ExistingActiveFreeze_ThrowsException() {
        when(freezeActionsRepository.existsByCustomerIdAndStatusAndIsDeleteFalse(1, "ACTIVE")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.createCustomerFreezeAction(validDto));
        verify(freezeActionsRepository, never()).save(any());
    }

    @Test
    void getAllByCustomerId_ReturnsList() {
        when(freezeActionsRepository.findAllByCustomerId(1)).thenReturn(Arrays.asList(entity));

        List<CustomerFreezeActionsDto> result = service.getAllByCustomerId(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(entity.getCustomerId(), result.get(0).getCustomerId());
        verify(freezeActionsRepository).findAllByCustomerId(1);
    }

    @Test
    void getFreezeHistoryByCustomerId_NoRecords_ThrowsException() {
        when(freezeActionsRepository.findByCustomerIdAndIsDeleteFalseOrderByEffectiveFromDesc(1))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> service.getFreezeHistoryByCustomerId(1));
    }

    @Test
    void updateFreezeAction_ValidInput_ReturnsUpdatedDto() {
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.of(entity));
        when(freezeActionsRepository.save(any(CustomerFreezeActions.class))).thenReturn(entity);

        CustomerFreezeActionsDto result = service.updateFreezeAction(1, validDto);

        assertNotNull(result);
        assertEquals(validDto.getCustomerId(), result.getCustomerId());
        verify(freezeActionsRepository).save(any(CustomerFreezeActions.class));
    }

    @Test
    void updateFreezeAction_NonExistentId_ThrowsException() {
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.updateFreezeAction(1, validDto));
    }

    @Test
    void deleteFreezeAction_LiftedStatus_Success() {
        entity.setStatus("LIFTED");
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.of(entity));
        when(freezeActionsRepository.save(any(CustomerFreezeActions.class))).thenReturn(entity);

        service.deleteFreezeAction(1, 2);

        verify(freezeActionsRepository).save(any(CustomerFreezeActions.class));
        assertTrue(entity.getIsDelete());
        assertEquals(2, entity.getUpdatedBy());
    }

    @Test
    void deleteFreezeAction_NonLiftedStatus_ThrowsException() {
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThrows(BusinessException.class, () -> service.deleteFreezeAction(1, 2));
    }

    @Test
    void liftFreezeAction_ValidInput_Success() {
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.of(entity));
        when(freezeActionsRepository.save(any(CustomerFreezeActions.class))).thenReturn(entity);

        CustomerFreezeActionsDto result = service.liftFreezeAction(1, LocalDate.now(), 2);

        assertNotNull(result);
        assertEquals("LIFTED", result.getStatus());
        verify(freezeActionsRepository).save(any(CustomerFreezeActions.class));
    }

    @Test
    void liftFreezeAction_PastEffectiveTo_ThrowsException() {
        when(freezeActionsRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThrows(BusinessException.class, () -> 
            service.liftFreezeAction(1, LocalDate.now().minusDays(1), 2));
    }

    @Test
    void checkIfCustomerIsUnderFreeze_ActiveFreeze_ReturnsList() {
        when(freezeActionsRepository.findByCustomerIdAndStatusAndIsDeleteFalseAndEffectiveFromLessThanEqualAndEffectiveToCondition(
                eq(1), eq("ACTIVE"), any(LocalDate.class)))
                .thenReturn(Arrays.asList(entity));

        List<CustomerFreezeActionsDto> result = service.checkIfCustomerIsUnderFreeze(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(freezeActionsRepository).findByCustomerIdAndStatusAndIsDeleteFalseAndEffectiveFromLessThanEqualAndEffectiveToCondition(
                eq(1), eq("ACTIVE"), any(LocalDate.class));
    }

    @Test
    void checkIfCustomerIsUnderFreeze_NoActiveFreeze_ThrowsException() {
        when(freezeActionsRepository.findByCustomerIdAndStatusAndIsDeleteFalseAndEffectiveFromLessThanEqualAndEffectiveToCondition(
                eq(1), eq("ACTIVE"), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> service.checkIfCustomerIsUnderFreeze(1));
    }
}