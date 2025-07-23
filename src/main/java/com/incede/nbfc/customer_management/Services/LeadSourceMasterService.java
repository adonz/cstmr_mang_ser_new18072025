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

    public LeadSourceMasterDto create(LeadSourceMasterDto dto) {
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
            throw new IllegalArgumentException("Lead Source name already exists for this tenant");
        }

        LeadSourceMaster entity = new LeadSourceMaster();
        entity.setTenantId(tenantId);
        entity.setSourceName(trimmedName);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIsDelete(false);

        entity = leadSourceMasterRepository.save(entity);
        return convertToDto(entity);
    }


    
    
    public LeadSourceMasterDto updateSourceName(UUID identity, String newSourceName, Integer updatedBy) {
        Integer tenantId = tenantConfig.getTenantId();

        LeadSourceMaster existing = leadSourceMasterRepository
                .findByTenantIdAndIdentityAndIsDeleteFalse(tenantId, identity)
                .orElseThrow(() -> new BusinessException("Lead source not found."));

        if (newSourceName == null || newSourceName.trim().isEmpty()) {
            throw new BusinessException("New source name is required.");
        }
        existing.setSourceName(newSourceName.trim());
        existing.setUpdatedBy(updatedBy);
        existing.setUpdatedAt(LocalDateTime.now());
        LeadSourceMaster updated = leadSourceMasterRepository.save(existing);
        return convertToDto(updated);
    }
    
    public void softDeleteByIdentity(UUID identity, Integer deletedBy) {
        Integer tenantId = tenantConfig.getTenantId();

        LeadSourceMaster existing = leadSourceMasterRepository
                .findByTenantIdAndIdentityAndIsDeleteFalse(tenantId, identity)
                .orElseThrow(() -> new BusinessException("Lead source not found."));

        existing.setIsDelete(true);
        existing.setUpdatedBy(deletedBy);
        existing.setUpdatedAt(LocalDateTime.now());

        leadSourceMasterRepository.save(existing);
    }
    public List<LeadSourceMasterDto> getAllByTenant() {
        Integer tenantId = tenantConfig.getTenantId();

        return leadSourceMasterRepository
                .findAllByTenantIdAndIsDeleteFalse(tenantId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public LeadSourceMasterDto getByIdentity(UUID identity) {
        Integer tenantId = tenantConfig.getTenantId();

        LeadSourceMaster entity = leadSourceMasterRepository
                .findByTenantIdAndIdentityAndIsDeleteFalse(tenantId, identity)
                .orElseThrow(() -> new BusinessException("Lead source not found."));

        return convertToDto(entity);
    }




    private LeadSourceMaster convertToEntity(LeadSourceMasterDto dto) {
        LeadSourceMaster entity = new LeadSourceMaster();
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setIdentity(dto.getIdentity());
        entity.setIsDelete(dto.getIsDelete());
        entity.setSourceId(dto.getSourceId());
        entity.setSourceName(dto.getSourceName());
        // sourceName, createdAt, isDelete, tenantId, identity will be set in create()
        return entity;
    }

    private LeadSourceMasterDto convertToDto(LeadSourceMaster entity) {
        LeadSourceMasterDto dto = new LeadSourceMasterDto();
        dto.setSourceId(entity.getSourceId());
        dto.setSourceName(entity.getSourceName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setIsDelete(entity.getIsDelete());
        dto.setTenantId(entity.getTenantId());
        dto.setIdentity(entity.getIdentity());
        return dto;
    }
}