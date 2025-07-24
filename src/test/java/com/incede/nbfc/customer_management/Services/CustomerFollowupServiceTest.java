package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.CustomerFollowupDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerFollowup;
import com.incede.nbfc.customer_management.Repositories.CustomerFollowupRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFollowupServiceTest {

    @Mock
    private CustomerFollowupRepository customerFollowupRepository;

    @InjectMocks
    private CustomerFollowupService service;

    private CustomerFollowupDto validDto;
    private CustomerFollowup entity;

    @BeforeEach
    void setUp() {
        validDto = new CustomerFollowupDto();
        validDto.setCustomerId(1);
        validDto.setStaffId(2);
        validDto.setFollowupDate(LocalDate.now());
        validDto.setFollowupType(1);
        validDto.setFollowupNotes("Test follow-up");
        validDto.setNextFollowupDate(LocalDate.now().plusDays(3));
        validDto.setStatus(1);
        validDto.setCreatedBy(1);
        validDto.setCreatedAt(LocalDateTime.now());

        entity = new CustomerFollowup();
        entity.setFollowupId(1);
        entity.setCustomerId(1);
        entity.setStaffId(2);
        entity.setFollowupDate(LocalDate.now());
        entity.setFollowupType(1);
        entity.setFollowupNotes("Test follow-up");
        entity.setNextFollowupDate(LocalDate.now().plusDays(3));
        entity.setStatus(1);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsDelete(false);
    }

    @Test
    void createOrUpdate_CreateValidInput_ReturnsDto() {
        when(customerFollowupRepository.save(any(CustomerFollowup.class))).thenReturn(entity);

        CustomerFollowupDto result = service.createOrUpdate(validDto);

        assertNotNull(result);
        assertEquals(validDto.getCustomerId(), result.getCustomerId());
        assertNotNull(result.getIdentity());
        assertFalse(result.getIsDelete());
        verify(customerFollowupRepository).save(any(CustomerFollowup.class));
    }

    @Test
    void createOrUpdate_CreateMissingCreatedBy_ThrowsException() {
        validDto.setCreatedBy(null);

        assertThrows(BusinessException.class, () -> service.createOrUpdate(validDto));
        verify(customerFollowupRepository, never()).save(any());
    }

    @Test
    void createOrUpdate_UpdateValidInput_ReturnsDto() {
        validDto.setFollowupId(1);
        validDto.setUpdatedBy(2);
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerFollowupRepository.save(any(CustomerFollowup.class))).thenReturn(entity);

        CustomerFollowupDto result = service.createOrUpdate(validDto);

        assertNotNull(result);
        assertEquals(validDto.getCustomerId(), result.getCustomerId());
        verify(customerFollowupRepository).save(any(CustomerFollowup.class));
    }

    @Test
    void createOrUpdate_UpdateNonExistentId_ThrowsException() {
        validDto.setFollowupId(1);
        validDto.setUpdatedBy(2);
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.createOrUpdate(validDto));
    }

    @Test
    void getFollowupsByCustomerId_HasRecords_ReturnsList() {
        when(customerFollowupRepository.findByCustomerIdAndIsDeleteFalseOrderByFollowupDateDesc(1))
                .thenReturn(Arrays.asList(entity));

        List<CustomerFollowupDto> result = service.getFollowupsByCustomerId(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(entity.getCustomerId(), result.get(0).getCustomerId());
        verify(customerFollowupRepository).findByCustomerIdAndIsDeleteFalseOrderByFollowupDateDesc(1);
    }

    @Test
    void getFollowupsByCustomerId_NoRecords_ThrowsException() {
        when(customerFollowupRepository.findByCustomerIdAndIsDeleteFalseOrderByFollowupDateDesc(1))
                .thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> service.getFollowupsByCustomerId(1));
    }

    @Test
    void updateFollowup_ValidInput_ReturnsUpdatedDto() {
        validDto.setFollowupId(1);
        validDto.setUpdatedBy(2);
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerFollowupRepository.save(any(CustomerFollowup.class))).thenReturn(entity);

        CustomerFollowupDto result = service.updateFollowup(validDto);

        assertNotNull(result);
        assertEquals(validDto.getStatus(), result.getStatus());
        verify(customerFollowupRepository).save(any(CustomerFollowup.class));
    }

    @Test
    void updateFollowup_InvalidStatus_ThrowsException() {
        validDto.setFollowupId(1);
        validDto.setUpdatedBy(2);
        validDto.setStatus(4);
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThrows(BusinessException.class, () -> service.updateFollowup(validDto));
    }

    @Test
    void softDeleteFollowup_ValidInput_Success() {
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerFollowupRepository.save(any(CustomerFollowup.class))).thenReturn(entity);

        service.softDeleteFollowup(1, 2);

        verify(customerFollowupRepository).save(any(CustomerFollowup.class));
        assertTrue(entity.getIsDelete());
        assertEquals(2, entity.getUpdatedBy());
    }

    @Test
    void softDeleteFollowup_AlreadyDeleted_ThrowsException() {
        entity.setIsDelete(true);
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThrows(BusinessException.class, () -> service.softDeleteFollowup(1, 2));
    }

    @Test
    void getUpcomingFollowupsForStaffiWithinSevenDays_ReturnsList() {
        when(customerFollowupRepository.findByStaffIdAndStatusAndIsDeleteFalseAndNextFollowupDateBetweenOrderByNextFollowupDateAsc(
                eq(2), eq(1), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(entity));

        List<CustomerFollowupDto> result = service.getUpcomingFollowupsForStaffiWithinSevenDays(2);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(customerFollowupRepository).findByStaffIdAndStatusAndIsDeleteFalseAndNextFollowupDateBetweenOrderByNextFollowupDateAsc(
                eq(2), eq(1), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getFollowupById_ValidId_ReturnsDto() {
        when(customerFollowupRepository.findById(1)).thenReturn(Optional.of(entity));

        CustomerFollowupDto result = service.getFollowupById(1);

        assertNotNull(result);
        assertEquals(entity.getFollowupId(), result.getFollowupId());
        verify(customerFollowupRepository).findById(1);
    }

    @Test
    void getAllActiveFollowups_HasRecords_ReturnsList() {
        when(customerFollowupRepository.findByIsDeleteFalseOrderByFollowupDateDesc())
                .thenReturn(Arrays.asList(entity));

        List<CustomerFollowupDto> result = service.getAllActiveFollowups();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(customerFollowupRepository).findByIsDeleteFalseOrderByFollowupDateDesc();
    }

    @Test
    void getAllActiveFollowups_NoRecords_ThrowsException() {
        when(customerFollowupRepository.findByIsDeleteFalseOrderByFollowupDateDesc())
                .thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> service.getAllActiveFollowups());
    }
}