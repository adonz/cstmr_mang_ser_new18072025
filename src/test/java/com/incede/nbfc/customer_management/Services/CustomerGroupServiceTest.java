package com.incede.nbfc.customer_management.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerGroupDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerGroup;
import com.incede.nbfc.customer_management.Repositories.CustomerGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerGroupServiceTest {

    @Mock
    private CustomerGroupRepository customerGroupRepository;

    @InjectMocks
    private CustomerGroupService customerGroupService;

    private CustomerGroupDto validDto;
    private CustomerGroup entity;

    @BeforeEach
    void setUp() {
        validDto = new CustomerGroupDto();
        validDto.setGroupId(1);
        validDto.setTenantId(1);
        validDto.setGroupName("Premium Customers");
        validDto.setDescription("High-value customers");
        validDto.setIsActive(true);
        validDto.setIsDelete(false);
        validDto.setCreatedBy(1);
        validDto.setUpdatedBy(1);

        entity = new CustomerGroup();
        entity.setGroupId(1);
        entity.setTenantId(1);
        entity.setGroupName("Premium Customers");
        entity.setDescription("High-value customers");
        entity.setIsActive(true);
        entity.setIsDelete(false);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedBy(1);
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createCustomerGroup_Success() {
        when(customerGroupRepository.existsByTenantIdAndGroupNameIgnoreCaseAndIsDeleteFalse(anyInt(), anyString())).thenReturn(false);
        when(customerGroupRepository.save(any(CustomerGroup.class))).thenReturn(entity);

        CustomerGroupDto result = customerGroupService.createCustomerGroup(validDto);

        assertNotNull(result);
        assertEquals(validDto.getGroupName(), result.getGroupName());
        assertEquals(validDto.getTenantId(), result.getTenantId());
        assertTrue(result.getIsActive());
        assertFalse(result.getIsDelete());
        verify(customerGroupRepository).existsByTenantIdAndGroupNameIgnoreCaseAndIsDeleteFalse(1, "Premium Customers");
        verify(customerGroupRepository).save(any(CustomerGroup.class));
    }

    @Test
    void createCustomerGroup_DuplicateName_ThrowsBusinessException() {
        when(customerGroupRepository.existsByTenantIdAndGroupNameIgnoreCaseAndIsDeleteFalse(anyInt(), anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.createCustomerGroup(validDto));

        assertEquals("Group name already exists within the tenant.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void createCustomerGroup_NullTenantId_ThrowsBusinessException() {
        validDto.setTenantId(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.createCustomerGroup(validDto));

        assertEquals("Tenant ID is required.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void getActiveGroupsByTenantId_Success() {
        when(customerGroupRepository.findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByGroupName(1))
            .thenReturn(List.of(entity));

        List<CustomerGroupDto> result = customerGroupService.getActiveGroupsByTenantId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getGroupName(), result.get(0).getGroupName());
        verify(customerGroupRepository).findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByGroupName(1);
    }

    @Test
    void getActiveGroupsByTenantId_NullTenantId_ThrowsBusinessException() {
        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.getActiveGroupsByTenantId(null));

        assertEquals("Tenant ID is required.", exception.getMessage());
        verify(customerGroupRepository, never()).findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByGroupName(anyInt());
    }

    @Test
    void updateCustomerGroup_Success() {
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerGroupRepository.existsByTenantIdAndGroupNameIgnoreCaseAndGroupIdNotAndIsDeleteFalse(anyInt(), anyString(), anyInt())).thenReturn(false);
        when(customerGroupRepository.save(any(CustomerGroup.class))).thenReturn(entity);

        validDto.setGroupName("Updated Group");
        validDto.setDescription("Updated description");
        CustomerGroupDto result = customerGroupService.updateCustomerGroup(validDto);

        assertNotNull(result);
        assertEquals("Updated Group", result.getGroupName());
        assertEquals("Updated description", result.getDescription());
        verify(customerGroupRepository).findById(1);
        verify(customerGroupRepository).existsByTenantIdAndGroupNameIgnoreCaseAndGroupIdNotAndIsDeleteFalse(anyInt(), eq("Updated Group"), anyInt());
        verify(customerGroupRepository).save(any(CustomerGroup.class));
    }

    @Test
    void updateCustomerGroup_DuplicateName_ThrowsBusinessException() {
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerGroupRepository.existsByTenantIdAndGroupNameIgnoreCaseAndGroupIdNotAndIsDeleteFalse(anyInt(), anyString(), anyInt())).thenReturn(true);

        validDto.setGroupName("Duplicate Group");
        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.updateCustomerGroup(validDto));

        assertEquals("Group name already exists within the tenant.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void updateCustomerGroup_DeletedGroup_ThrowsBusinessException() {
        entity.setIsDelete(true);
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.updateCustomerGroup(validDto));

        assertEquals("Cannot update a deleted customer group.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void softDeleteCustomerGroup_Success() {
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerGroupRepository.save(any(CustomerGroup.class))).thenReturn(entity);

        customerGroupService.softDeleteCustomerGroup(1, 1);

        verify(customerGroupRepository).findById(1);
        verify(customerGroupRepository).save(any(CustomerGroup.class));
    }

    @Test
    void softDeleteCustomerGroup_AlreadyDeleted_ThrowsBusinessException() {
        entity.setIsDelete(true);
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.softDeleteCustomerGroup(1, 1));

        assertEquals("Customer group is already deleted.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void getCustomerGroupById_Success() {
        when(customerGroupRepository.findByGroupIdAndIsDeleteFalse(10)).thenReturn(Optional.of(entity));

        CustomerGroupDto result = customerGroupService.getCustomerGroupById(10);

        assertNotNull(result);
        assertEquals(entity.getGroupId(), result.getGroupId());
        assertEquals(entity.getGroupName(), result.getGroupName());
        verify(customerGroupRepository).findByGroupIdAndIsDeleteFalse(10);
    }

    @Test
    void getCustomerGroupById_NotFound_ThrowsBusinessException() {
        when(customerGroupRepository.findByGroupIdAndIsDeleteFalse(10)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.getCustomerGroupById(10));

        assertEquals("Customer group not found with ID: 10", exception.getMessage());
        verify(customerGroupRepository).findByGroupIdAndIsDeleteFalse(10);
    }

    @Test
    void toggleActiveStatus_Success() {
        entity.setIsActive(true);
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));
        when(customerGroupRepository.save(any(CustomerGroup.class))).thenReturn(entity);

        CustomerGroupDto result = customerGroupService.toggleActiveStatus(1, 1);

        assertNotNull(result);
        assertFalse(result.getIsActive()); 
        verify(customerGroupRepository).findById(1);
        verify(customerGroupRepository).save(any(CustomerGroup.class));
    }

    @Test
    void toggleActiveStatus_DeletedGroup_ThrowsBusinessException() {
        entity.setIsDelete(true);
        when(customerGroupRepository.findById(1)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.toggleActiveStatus(1, 1));

        assertEquals("Cannot change active status of a deleted customer group.", exception.getMessage());
        verify(customerGroupRepository, never()).save(any());
    }

    @Test
    void toggleActiveStatus_NullParameters_ThrowsBusinessException() {
        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerGroupService.toggleActiveStatus(null, 1));

        assertEquals("groupId and updatedBy are required.", exception.getMessage());
        verify(customerGroupRepository, never()).findById(anyInt());
    }
}