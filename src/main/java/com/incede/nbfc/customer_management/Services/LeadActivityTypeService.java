package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.incede.nbfc.customer_management.DTOs.LeadActivityTypeDto;
import com.incede.nbfc.customer_management.Models.LeadActivityType;
import com.incede.nbfc.customer_management.Repositories.LeadActivityTypeRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;

import jakarta.transaction.Transactional;

@Service
public class LeadActivityTypeService {

private final LeadActivityTypeRepository activityTypeRepository;
private final TenantConfig tenantConfig;

public LeadActivityTypeService(LeadActivityTypeRepository activityTypeRepository, TenantConfig tenantConfig) {
    this.activityTypeRepository = activityTypeRepository;
    this.tenantConfig = tenantConfig;
}

// User Story 1: Create
public LeadActivityTypeDto create(LeadActivityTypeDto dto) {
    if (dto.getTypeName() == null || dto.getTypeName().isBlank()) {
        throw new IllegalArgumentException("type_name is required");
    }

    Integer tenantId = tenantConfig.getTenantId(); // ✅ Get tenant from context/session
    String normalizedName = dto.getTypeName().trim().toUpperCase();

    boolean exists = activityTypeRepository.existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, normalizedName);
    if (exists) {
        throw new IllegalArgumentException("Activity type name already exists for this tenant");
    }

    LeadActivityType entity = new LeadActivityType();
    entity.setTenantId(tenantId); // ✅ Use only tenant from config
    entity.setTypeName(normalizedName);
    entity.setIdentity(UUID.randomUUID());
    entity.setCreatedBy(dto.getCreatedBy());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());
    entity.setIsDelete(false);

    entity = activityTypeRepository.save(entity);
    return mapToDto(entity);
}


// User Story 2: Update
@Transactional
public LeadActivityTypeDto update(Integer id, Integer tenantId, String newTypeName, Integer updatedBy) {
//    tenantConfig.validateTenant(tenantId);

    LeadActivityType entity = activityTypeRepository
            .findByActivityTypeIdAndTenantIdAndIsDeleteFalse(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Activity Type not found"));

    String normalizedName = newTypeName.trim().toUpperCase();
    boolean duplicateExists = activityTypeRepository
            .existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(tenantId, normalizedName);

    if (duplicateExists && !entity.getTypeName().equalsIgnoreCase(normalizedName)) {
        throw new IllegalArgumentException("Another activity type with same name already exists");
    }

    entity.setTypeName(normalizedName);
    entity.setUpdatedBy(updatedBy);
    entity.setUpdatedAt(LocalDateTime.now());

    return mapToDto(activityTypeRepository.save(entity));
}

// User Story 3: Soft Delete
@Transactional
public void softDelete(Integer id, Integer tenantId, Integer userId) {
//    tenantConfig.validateTenant(tenantId);

    LeadActivityType entity = activityTypeRepository
            .findByActivityTypeIdAndTenantIdAndIsDeleteFalse(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Activity Type not found or already deleted"));

    // Optionally check: if referenced in lead_followup, throw error

    entity.setIsDelete(true);
    entity.setUpdatedBy(userId);
    entity.setUpdatedAt(LocalDateTime.now());

    activityTypeRepository.save(entity);
}

// User Story 4: Fetch All Active
public List<LeadActivityTypeDto> getAllActiveForTenant(Integer tenantId) {
//    tenantConfig.validateTenant(tenantId);

    return activityTypeRepository.findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(tenantId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
}

// Utility mapper
private LeadActivityTypeDto mapToDto(LeadActivityType entity) {
    LeadActivityTypeDto dto = new LeadActivityTypeDto();
    dto.setActivityTypeId(entity.getActivityTypeId());
    dto.setTenantId(entity.getTenantId());
    dto.setTypeName(entity.getTypeName());
    dto.setCreatedBy(entity.getCreatedBy());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedBy(entity.getUpdatedBy());
    dto.setUpdatedAt(entity.getUpdatedAt());
    dto.setIsDelete(entity.getIsDelete());
    dto.setIdentity(entity.getIdentity());
    return dto;
}
}
	


