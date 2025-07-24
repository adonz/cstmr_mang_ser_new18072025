package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.CustomerCodeDefinitionDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerCodeDefinition;
import com.incede.nbfc.customer_management.Repositories.CustomerCodeDefinitionRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerCodeDefinitionServiceTest {

    @InjectMocks
    private CustomerCodeDefinitionService service;

    @Mock
    private CustomerCodeDefinitionRepository repository;

    @Mock
    private TenantConfig tenantConfig;

    private CustomerCodeDefinitionDto dto;
    private CustomerCodeDefinition entity;
    private java.util.Date today;

    @BeforeEach
    void setup() {
        today = java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        dto = new CustomerCodeDefinitionDto();
        dto.setTenantId(1);
        dto.setMaxLength(15);
        dto.setSuffix("X1");
        dto.setIncludeBranchCode(true);
        dto.setBranchCodeLength(3);
        dto.setIncludeProductType(true);
        dto.setProductTypeLength(3);
        dto.setSerialNoLength(5);
        dto.setLastSerialNo(100);
        dto.setEffectiveFrom(Date.valueOf(LocalDate.now()));
        dto.setEffectiveTo(Date.valueOf(LocalDate.now().plusDays(10)));
        dto.setCreatedBy(999);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setIsActive(true);
        dto.setIsDelete(false);

        entity = new CustomerCodeDefinition();
        entity.setId(1);
        entity.setTenantId(1);
        entity.setMaxLength(15);
        entity.setSuffix("X1");
        entity.setIncludeBranchCode(true);
        entity.setBranchCodeLength(3);
        entity.setIncludeProductType(true);
        entity.setProductTypeLength(3);
        entity.setSerialNoLength(5);
        entity.setLastSerialNo(100);
        entity.setEffectiveFrom(Date.valueOf(LocalDate.now()));
        entity.setEffectiveTo(Date.valueOf(LocalDate.now().plusDays(10)));
        entity.setCreatedBy(999);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);
        entity.setIsDelete(false);
    }

    @Test
    void testCreateCustomerCodeDefinition_Success() {
        when(repository.existsByTenantIdAndIsActiveTrue(1)).thenReturn(false);
        when(repository.save(any(CustomerCodeDefinition.class))).thenReturn(entity);

        CustomerCodeDefinitionDto result = service.createCustomerCodeDefinition(dto);

        assertNotNull(result);
        assertEquals(dto.getTenantId(), result.getTenantId());
        assertEquals(dto.getMaxLength(), result.getMaxLength());
        assertEquals(dto.getSuffix(), result.getSuffix());
        assertTrue(result.getIsActive());
        assertFalse(result.getIsDelete());
        verify(repository, times(1)).save(any(CustomerCodeDefinition.class));
        verify(tenantConfig, never()).getTenantId();
    }

    @Test
    void testCreateCustomerCodeDefinition_NullTenantId_SetsFromTenantConfig() {
        dto.setTenantId(null);
        when(tenantConfig.getTenantId()).thenReturn(1);
        when(repository.existsByTenantIdAndIsActiveTrue(1)).thenReturn(false);
        when(repository.save(any(CustomerCodeDefinition.class))).thenReturn(entity);

        CustomerCodeDefinitionDto result = service.createCustomerCodeDefinition(dto);

        assertNotNull(result);
        assertEquals(1, result.getTenantId());
        assertEquals(dto.getMaxLength(), result.getMaxLength());
        assertEquals(dto.getSuffix(), result.getSuffix());
        verify(repository, times(1)).save(any(CustomerCodeDefinition.class));
        verify(tenantConfig, times(1)).getTenantId();
    }

    @Test
    void testCreateCustomerCodeDefinition_InvalidEffectiveFrom() {
        dto.setEffectiveFrom(Date.valueOf(LocalDate.now().minusDays(1)));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.createCustomerCodeDefinition(dto);
        });
        assertEquals("Effective From must be a current or future date.", exception.getMessage());
        verify(repository, never()).save(any());
        verify(tenantConfig, never()).getTenantId();
    }

    @Test
    void testCreateCustomerCodeDefinition_MaxLengthExceeded() {
        dto.setSuffix("XYZ");
        dto.setSerialNoLength(10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.createCustomerCodeDefinition(dto);
        });
        assertEquals("Defined code components exceed maximum code length.", exception.getMessage());
        verify(repository, never()).save(any());
        verify(tenantConfig, never()).getTenantId();
    }

    @Test
    void testGenerateCustomerCode_Success() {
        when(repository.findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
                eq(1), eq(today), eq(today))).thenReturn(Optional.of(entity));
        when(repository.save(any(CustomerCodeDefinition.class))).thenReturn(entity);

        String code = service.generateCustomerCode(1, "B1", "P2", 1001);

        assertNotNull(code);
        assertEquals("0B10P200101X1", code);
        verify(repository, times(1)).save(any(CustomerCodeDefinition.class));
        verify(tenantConfig, never()).getTenantId();
    }

    @Test
    void testGenerateCustomerCode_NoDefinitionFound() {
        when(repository.findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
                eq(1), eq(today), eq(today))).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.generateCustomerCode(1, "BR", "PT", 1001);
        });

        assertTrue(exception.getMessage().contains("Active Customer Code Definition not found"));
        verify(repository, never()).save(any());
        verify(tenantConfig, never()).getTenantId();
    }

    @Test
    void testGenerateCustomerCode_MissingBranchCode_ThrowsException() {
        entity.setIncludeBranchCode(true);
        when(repository.findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
                eq(1), eq(today), eq(today)))
                .thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.generateCustomerCode(1, null, "PT", 1001);
        });

        assertTrue(exception.getMessage().contains("Branch code is required"));
        verify(repository, never()).save(any());
        verify(tenantConfig, never()).getTenantId();
    }
}