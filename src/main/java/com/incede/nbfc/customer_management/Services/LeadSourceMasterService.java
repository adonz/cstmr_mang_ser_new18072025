package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.incede.nbfc.customer_management.DTOs.LeadSourceMasterDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.LeadSourceMaster;
import com.incede.nbfc.customer_management.Repositories.LeadSourceMasterRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;


@Service
public class LeadSourceMasterService {

    private final LeadSourceMasterRepository leadSourceMasterRepository;
    private final TenantConfig tenantConfig;

    public LeadSourceMasterService(LeadSourceMasterRepository leadSourceMasterRepository, TenantConfig tenantConfig) {
        this.leadSourceMasterRepository = leadSourceMasterRepository;
        this.tenantConfig = tenantConfig;
    }


    // User Story 1: Add new lead source
    public LeadSourceMasterDto create(LeadSourceMasterDto dto) {
    	System.out.println("Received DTO: " + dto); // Log the DTO
        System.out.println("SourceName: " + dto.getSourceName());
        if (dto.getSourceName() == null || dto.getSourceName().isBlank()) {
            throw new IllegalArgumentException("sourceName is required");
        }
        if (dto.getCreatedBy() == null) {
            throw new IllegalArgumentException("createdBy is required");
        }

        Integer tenantId = tenantConfig.getTenantId();
        String trimmedName = dto.getSourceName().trim();

        boolean exists = leadSourceMasterRepository
                .existsByTenantIdAndSourceNameIgnoreCaseAndIsDeleteFalse(tenantId, trimmedName);

        if (exists) {
            throw new BusinessException("Lead Source name already exists for this tenant");
        }

        LeadSourceMaster entity = new LeadSourceMaster();
        entity.setTenantId(tenantId);
        entity.setSourceName(trimmedName);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIsDelete(false);

        LeadSourceMaster saved = leadSourceMasterRepository.save(entity);
        return convertToDto(saved);
    }

    // User Story 2: Update source name
    public LeadSourceMasterDto update(Integer sourceId, LeadSourceMasterDto dto) {
        LeadSourceMaster existing = leadSourceMasterRepository
                .findBySourceIdAndIsDeleteFalse(sourceId)
                .orElseThrow(() -> new BusinessException("Lead Source not found"));

        if (dto.getSourceName() == null || dto.getSourceName().isBlank()) {
            throw new IllegalArgumentException("sourceName is required for update");
        }

        existing.setSourceName(dto.getSourceName().trim());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        LeadSourceMaster updated = leadSourceMasterRepository.save(existing);
        return convertToDto(updated);
    }

    // User Story 3: Soft delete
    public void softDelete(Integer sourceId, Integer updatedBy) {
        LeadSourceMaster existing = leadSourceMasterRepository
                .findBySourceIdAndIsDeleteFalse(sourceId)
                .orElseThrow(() -> new BusinessException("Lead Source not found"));

        existing.setIsDelete(true);
        existing.setUpdatedBy(updatedBy);
        existing.setUpdatedAt(LocalDateTime.now());

        leadSourceMasterRepository.save(existing);
    }

    // User Story 4: View all by tenant
    public List<LeadSourceMasterDto> getAllByTenant() {
        Integer tenantId = tenantConfig.getTenantId();
        List<LeadSourceMaster> list = leadSourceMasterRepository
                .findAllByTenantIdAndIsDeleteFalse(tenantId);
        return list.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // User Story 5: Get by UUID identity
    public LeadSourceMasterDto getByIdentity(UUID identity) {
        LeadSourceMaster entity = leadSourceMasterRepository
                .findByIdentityAndIsDeleteFalse(identity)
                .orElseThrow(() -> new BusinessException("Lead Source not found with given identity"));
        return convertToDto(entity);
    }

    // ---- Conversion methods ----

    private LeadSourceMasterDto convertToDto(LeadSourceMaster entity) {
        LeadSourceMasterDto dto = new LeadSourceMasterDto();
        dto.setSourceId(entity.getSourceId());
        dto.setTenantId(entity.getTenantId());
        dto.setSourceName(entity.getSourceName());
        dto.setIdentity(entity.getIdentity());
        dto.setIsDelete(entity.getIsDelete());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }

    public LeadSourceMaster convertToEntity(LeadSourceMasterDto dto) {
        LeadSourceMaster entity = new LeadSourceMaster();
        entity.setSourceId(dto.getSourceId());
        entity.setTenantId(dto.getTenantId());
        entity.setSourceName(dto.getSourceName());
        entity.setIdentity(dto.getIdentity());
        entity.setIsDelete(dto.getIsDelete());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        return entity;
    }
}