package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.LeadActivityTypeDto;
import com.incede.nbfc.customer_management.Models.LeadActivityType;
import com.incede.nbfc.customer_management.Repositories.LeadActivityTypeRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadActivityTypeServiceTest {

    @Mock
    private LeadActivityTypeRepository activityTypeRepository;

    @Mock
    private TenantConfig tenantConfig;

    @InjectMocks
    private LeadActivityTypeService leadActivityTypeService;

    private LeadActivityTypeDto validDto;
    private LeadActivityType entity;
    private Integer tenantId;
    private UUID identity;

    @BeforeEach
    void setUp() {
        tenantId = 1;
        identity = UUID.randomUUID();

        validDto = new LeadActivityTypeDto();
        validDto.setTypeName("MEETING");
        validDto.setCreatedBy(100);
        validDto.setTenantId(tenantId);
        validDto.setIdentity(identity);
        validDto.setIsDelete(false);
        validDto.setCreatedAt(LocalDateTime.now());
        validDto.setUpdatedAt(LocalDateTime.now());

        entity = new LeadActivityType();
        entity.setActivityTypeId(1);
        entity.setTenantId(tenantId);
        entity.setTypeName("MEETING");
        entity.setIdentity(identity);
        entity.setCreatedBy(100);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIsDelete(false);
    }

    // Tests for create method
    @Test
    void create_ValidDto_ReturnsDto() {
        when(tenantConfig.getTenantId()).thenReturn(tenantId);
        when(activityTypeRepository.existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, "MEETING")).thenReturn(false);
        when(activityTypeRepository.save(any(LeadActivityType.class))).thenReturn(entity);

        LeadActivityTypeDto result = leadActivityTypeService.create(validDto);

        assertNotNull(result);
        assertEquals("MEETING", result.getTypeName());
        assertEquals(tenantId, result.getTenantId());
        assertEquals(100, result.getCreatedBy());
        verify(activityTypeRepository, times(1)).save(any(LeadActivityType.class));
    }

    @Test
    void create_DuplicateName_ThrowsIllegalArgumentException() {
        when(tenantConfig.getTenantId()).thenReturn(tenantId);
        when(activityTypeRepository.existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, "MEETING")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.create(validDto);
        });

        assertEquals("Activity type name already exists for this tenant", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    @Test
    void create_NullTypeName_ThrowsIllegalArgumentException() {
        validDto.setTypeName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.create(validDto);
        });

        assertEquals("type_name is required", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    @Test
    void create_BlankTypeName_ThrowsIllegalArgumentException() {
        validDto.setTypeName("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.create(validDto);
        });

        assertEquals("type_name is required", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    // Tests for update method
    @Test
    void update_ValidInput_ReturnsUpdatedDto() {
        int activityTypeId = 1;
        String newName = "CALL";
        int updatedBy = 200;

        when(activityTypeRepository.findByActivityTypeIdAndTenantIdAndIsDeleteFalse(activityTypeId, tenantId))
                .thenReturn(Optional.of(entity));
        when(activityTypeRepository.existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, "CALL")).thenReturn(false);
        when(activityTypeRepository.save(any(LeadActivityType.class))).thenReturn(entity);

        LeadActivityTypeDto result = leadActivityTypeService.update(activityTypeId, tenantId, newName, updatedBy);

        assertNotNull(result);
        assertEquals("CALL", result.getTypeName());
        assertEquals(updatedBy, result.getUpdatedBy());
        verify(activityTypeRepository, times(1)).save(any(LeadActivityType.class));
    }

    @Test
    void update_DuplicateName_ThrowsIllegalArgumentException() {
        int activityTypeId = 1;
        String newName = "CALL";
        int updatedBy = 200;

        when(activityTypeRepository.findByActivityTypeIdAndTenantIdAndIsDeleteFalse(activityTypeId, tenantId))
                .thenReturn(Optional.of(entity));
        when(activityTypeRepository.existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, "CALL")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.update(activityTypeId, tenantId, newName, updatedBy);
        });

        assertEquals("Another activity type with same name already exists", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    @Test
    void update_EntityNotFound_ThrowsIllegalArgumentException() {
        int activityTypeId = 1;
        String newName = "CALL";
        int updatedBy = 200;

        when(activityTypeRepository.findByActivityTypeIdAndTenantIdAndIsDeleteFalse(activityTypeId, tenantId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.update(activityTypeId, tenantId, newName, updatedBy);
        });

        assertEquals("Entity not found", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    // Tests for softDelete method
    @Test
    void softDelete_ValidInput_SuccessfullyDeletes() {
        int activityTypeId = 1;
        int userId = 200;

        when(activityTypeRepository.findByActivityTypeIdAndTenantIdAndIsDeleteFalse(activityTypeId, tenantId))
                .thenReturn(Optional.of(entity));
        when(activityTypeRepository.save(any(LeadActivityType.class))).thenReturn(entity);

        leadActivityTypeService.softDelete(activityTypeId, tenantId, userId);

        assertTrue(entity.getIsDelete());
        assertEquals(userId, entity.getUpdatedBy());
        verify(activityTypeRepository, times(1)).save(any(LeadActivityType.class));
    }

    @Test
    void softDelete_EntityNotFound_ThrowsIllegalArgumentException() {
        int activityTypeId = 1;
        int userId = 200;

        when(activityTypeRepository.findByActivityTypeIdAndTenantIdAndIsDeleteFalse(activityTypeId, tenantId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leadActivityTypeService.softDelete(activityTypeId, tenantId, userId);
        });

        assertEquals("Activity Type not found or already deleted", exception.getMessage());
        verify(activityTypeRepository, never()).save(any(LeadActivityType.class));
    }

    // Tests for getAllActiveForTenant method
    @Test
    void getAllActiveForTenant_ReturnsActiveDtos() {
        List<LeadActivityType> entities = List.of(entity);
        when(activityTypeRepository.findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(tenantId)).thenReturn(entities);

        List<LeadActivityTypeDto> result = leadActivityTypeService.getAllActiveForTenant(tenantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MEETING", result.get(0).getTypeName());
        verify(activityTypeRepository, times(1)).findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(tenantId);
    }

    @Test
    void getAllActiveForTenant_NoActiveTypes_ReturnsEmptyList() {
        when(activityTypeRepository.findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(tenantId)).thenReturn(List.of());

        List<LeadActivityTypeDto> result = leadActivityTypeService.getAllActiveForTenant(tenantId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityTypeRepository, times(1)).findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(tenantId);
    }
}