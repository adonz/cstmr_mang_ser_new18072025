package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerGroupDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerGroup;
import com.incede.nbfc.customer_management.Repositories.CustomerGroupRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerGroupService {

    private final CustomerGroupRepository customerGroupRepository;

    public CustomerGroupService(CustomerGroupRepository customerGroupRepository) {
        this.customerGroupRepository = customerGroupRepository;
    }

    @Transactional
    public CustomerGroupDto createCustomerGroup(CustomerGroupDto dto) {
        if (dto.getTenantId() == null) {
            throw new BusinessException("Tenant ID is required.");
        }
        if (dto.getGroupName() == null || dto.getGroupName().trim().isEmpty()) {
            throw new BusinessException("Group name is required.");
        }
        if (dto.getCreatedBy() == null) {
            throw new BusinessException("createdBy is required.");
        }
        boolean exists = customerGroupRepository.existsByTenantIdAndGroupNameIgnoreCaseAndIsDeleteFalse(
                dto.getTenantId(), dto.getGroupName().trim()
        );
        if (exists) {
            throw new BusinessException("Group name already exists within the tenant.");
        }

        CustomerGroup group = new CustomerGroup();
        group.setTenantId(dto.getTenantId());
        group.setGroupName(dto.getGroupName().trim());
        group.setDescription(dto.getDescription());
        group.setIsActive(true);  // default
        group.setIsDelete(false);    // default
        group.setIdentity(UUID.randomUUID());
        group.setCreatedBy(dto.getCreatedBy());
        group.setCreatedAt(LocalDateTime.now());

        // Save and return DTO
        CustomerGroup saved = customerGroupRepository.save(group);
        return convertToDto(saved);
    }
    
    public List<CustomerGroupDto> getActiveGroupsByTenantId(Integer tenantId) {
        if (tenantId == null) {
            throw new BusinessException("Tenant ID is required.");
        }
        List<CustomerGroup> groups = customerGroupRepository
            .findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByGroupName(tenantId);
        return groups.stream()
                     .map(this::convertToDto)
                     .collect(Collectors.toList());
    }
    
    
    @Transactional
    public CustomerGroupDto updateCustomerGroup(CustomerGroupDto dto) {
        if (dto.getGroupId() == null) {
            throw new BusinessException("Group ID is required for update.");
        }

        // Fetch existing group
        CustomerGroup existing = customerGroupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new BusinessException("Customer group not found with ID: " + dto.getGroupId()));

        // 1. Cannot update a soft-deleted group
        if (Boolean.TRUE.equals(existing.getIsDelete())) {
            throw new BusinessException("Cannot update a deleted customer group.");
        }

        // 2. Validate uniqueness of group name (case-insensitive), excluding self
        boolean nameExists = customerGroupRepository
                .existsByTenantIdAndGroupNameIgnoreCaseAndGroupIdNotAndIsDeleteFalse(
                        existing.getTenantId(),
                        dto.getGroupName().trim(),
                        dto.getGroupId()
                );
        if (nameExists) {
            throw new BusinessException("Group name already exists within the tenant.");
        }

        // 3. Update fields
        existing.setGroupName(dto.getGroupName().trim());
        existing.setDescription(dto.getDescription());
        existing.setIsActive(dto.getIsActive());

        // 4. Set audit fields
        if (dto.getUpdatedBy() == null) {
            throw new BusinessException("updatedBy is required for updating the group.");
        }

        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        // 5. Save and return updated DTO
        CustomerGroup updated = customerGroupRepository.save(existing);
        return convertToDto(updated);
    }

    
    @Transactional
    public void softDeleteCustomerGroup(Integer groupId, Integer userId) {
        if (groupId == null || userId == null) {
            throw new BusinessException("groupId and userId are required.");
        }

//        // Optional strict rule: Prevent deletion if group is assigned to active customers
//        boolean isAssignedToActiveCustomer = customerGroupRepository.existsByGroupIdAndIsDeleteFalse(groupId);
//        if (isAssignedToActiveCustomer) {
//            throw new BusinessException("Cannot delete group assigned to active customers.");
//        }
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException("Customer group not found with ID: " + groupId));

        // Prevent redundant soft deletion
        if (Boolean.TRUE.equals(group.getIsDelete())) {
            throw new BusinessException("Customer group is already deleted.");
        }
        // Apply soft delete
        group.setIsDelete(true);
        group.setIsActive(false);
        group.setUpdatedBy(userId);
        group.setUpdatedAt(LocalDateTime.now());

        customerGroupRepository.save(group);
    }

    
    public CustomerGroupDto getCustomerGroupById(Integer groupId) {
        if (groupId == null) {
            throw new BusinessException("Group ID is required.");
        }
        CustomerGroup group = customerGroupRepository.findByGroupIdAndIsDeleteFalse(groupId)
                .orElseThrow(() -> new BusinessException("Customer group not found with ID: " + groupId));
        return convertToDto(group);
    }
    
    
    
    //EXTRA END POINT REQUIRED FOR FRONTEND   
    @Transactional
    public CustomerGroupDto toggleActiveStatus(Integer groupId, Integer updatedBy) {
        if (groupId == null || updatedBy == null) {
            throw new BusinessException("groupId and updatedBy are required.");
        }
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException("Customer group not found with ID: " + groupId));
        if (Boolean.TRUE.equals(group.getIsDelete())) {
            throw new BusinessException("Cannot change active status of a deleted customer group.");
        }
        // Toggle active status
        boolean currentStatus = Boolean.TRUE.equals(group.getIsActive());
        group.setIsActive(!currentStatus);
        group.setUpdatedBy(updatedBy);
        group.setUpdatedAt(LocalDateTime.now());
        CustomerGroup updated = customerGroupRepository.save(group);
        return convertToDto(updated);
    }




    private CustomerGroupDto convertToDto(CustomerGroup entity) {
        CustomerGroupDto dto = new CustomerGroupDto();
        dto.setGroupId(entity.getGroupId());
        dto.setTenantId(entity.getTenantId());
        dto.setGroupName(entity.getGroupName());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());
        dto.setIsDelete(entity.getIsDelete());
        dto.setIdentity(entity.getIdentity());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
