package com.incede.nbfc.customer_management.Services;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationshipDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerRelationship;
import com.incede.nbfc.customer_management.Repositories.CustomerRelationshipRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerRelationshipService {

    private final CustomerRelationshipRepository  customerRelationshipRepository;

    public CustomerRelationshipService(CustomerRelationshipRepository customerRelationshipRepository) {
        this.customerRelationshipRepository = customerRelationshipRepository;
    }

    // 1. Create new relationship
    public CustomerRelationshipDto createRelationship(CustomerRelationshipDto dto) {
        validateDto(dto);
        CustomerRelationship rel = convertToEntity(dto);
        rel.setCreatedAt(LocalDateTime.now());
        rel.setUpdatedAt(LocalDateTime.now());
        rel.setIdentity(UUID.randomUUID());
        rel.setIsActive(true);
        rel.setIsDelete(false);
        CustomerRelationship saved = customerRelationshipRepository.save(rel);
        return convertToDto(saved);
    }


    // 2. Get active relationships for a specific customer
    public List<CustomerRelationshipDto> getByCustomerId(Integer customerId) {
        List<CustomerRelationship> rels = customerRelationshipRepository.findByCustomerIdAndIsDeleteFalseAndIsActiveTrue(customerId);
        return rels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 3. Close relationship: set assignedTo and deactivate
    public CustomerRelationshipDto closeRelationship(Integer relationshipId, LocalDate assignedTo, Integer updatedBy) {
        CustomerRelationship rel = customerRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new BusinessException("Relationship not found"));

        rel.setAssignedTo(assignedTo);
        rel.setIsActive(false);
        rel.setUpdatedBy(updatedBy);
        rel.setUpdatedAt(LocalDateTime.now());
        CustomerRelationship saved = customerRelationshipRepository.save(rel);
        return convertToDto(saved);
    }


    // 4. Update last contacted date
    public CustomerRelationshipDto updateLastContacted(Integer relationshipId, LocalDate lastContacted, Integer updatedBy) {
        if (relationshipId == null) {
            throw new BusinessException("Relationship ID is required.");
        }
        if (lastContacted == null) {
            throw new BusinessException("Last contacted date is required.");
        }
        if (updatedBy == null) {
            throw new BusinessException("Updated by is required.");
        }
        CustomerRelationship rel = customerRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new BusinessException("Relationship not found."));
        rel.setLastContacted(lastContacted);
        rel.setUpdatedBy(updatedBy);
        rel.setUpdatedAt(LocalDateTime.now());
        CustomerRelationship saved = customerRelationshipRepository.save(rel);
        return convertToDto(saved);
 }


    // 5. Soft delete relationship
    public void softDelete(Integer relationshipId, Integer updatedBy) {
        CustomerRelationship rel = customerRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new BusinessException("Relationship not found."));

        rel.setIsDelete(true);
        rel.setIsActive(false);
        rel.setUpdatedBy(updatedBy);
        rel.setUpdatedAt(LocalDateTime.now());

        customerRelationshipRepository.save(rel);
    }

    // 6. Get relationship by ID
    public CustomerRelationshipDto getById(Integer id) {
        CustomerRelationship rel = customerRelationshipRepository.findByRelationshipIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new BusinessException("Relationship not found or deleted."));
        return convertToDto(rel);
    }

    // ---------- Conversion Methods ----------

    private CustomerRelationshipDto convertToDto(CustomerRelationship entity) {
        CustomerRelationshipDto dto = new CustomerRelationshipDto();
        dto.setRelationshipId(entity.getRelationshipId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setStaffId(entity.getStaffId());
        dto.setAssignedFrom(entity.getAssignedFrom());
        dto.setAssignedTo(entity.getAssignedTo());
        dto.setLastContacted(entity.getLastContacted());
        dto.setIsActive(entity.getIsActive());
        dto.setIsDelete(entity.getIsDelete());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private CustomerRelationship convertToEntity(CustomerRelationshipDto dto) {
        CustomerRelationship entity = new CustomerRelationship();
        entity.setRelationshipId(dto.getRelationshipId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setStaffId(dto.getStaffId());
        entity.setAssignedFrom(dto.getAssignedFrom());
        entity.setAssignedTo(dto.getAssignedTo());
        entity.setLastContacted(dto.getLastContacted());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        entity.setIsDelete(dto.getIsDelete() != null ? dto.getIsDelete() : false);
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    // ---------- Validation ----------

    private void validateDto(CustomerRelationshipDto dto) {
        if (dto.getCustomerId() == null) {
            throw new BusinessException("Customer ID is required.");
        }
        if (dto.getStaffId() == null) {
            throw new BusinessException("Staff ID is required.");
        }
        if (dto.getAssignedFrom() == null) {
            throw new BusinessException("Assigned From date is required.");
        }
        if (dto.getCreatedBy() == null) {
            throw new BusinessException("Created By is required.");
        }
    }
}
