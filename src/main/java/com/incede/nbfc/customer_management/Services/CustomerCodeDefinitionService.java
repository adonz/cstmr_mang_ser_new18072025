package com.incede.nbfc.customer_management.Services;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerCodeDefinitionDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerCodeDefinition;
import com.incede.nbfc.customer_management.Repositories.CustomerCodeDefinitionRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;

import jakarta.transaction.Transactional;

@Service
public class CustomerCodeDefinitionService {

    private final CustomerCodeDefinitionRepository customerCodeDefinitionRepository;
    private final TenantConfig tenantConfig;

    public CustomerCodeDefinitionService(CustomerCodeDefinitionRepository customerCodeDefinitionRepository,TenantConfig tenantConfig) {
        this.customerCodeDefinitionRepository = customerCodeDefinitionRepository;
        this.tenantConfig = tenantConfig;
    }



    public CustomerCodeDefinitionDto createCustomerCodeDefinition(CustomerCodeDefinitionDto dto) {

        if (dto.getTenantId() == null) {
            dto.setTenantId(tenantConfig.getTenantId());
        }

        if (dto.getMaxLength() == null || dto.getSerialNoLength() == null ||
            dto.getLastSerialNo() == null || dto.getEffectiveFrom() == null || dto.getCreatedBy() == null) {
            throw new BusinessException("Missing mandatory fields.");
        }

        Date today = convertLocalDateToUtilDate(LocalDate.now());
        Date effectiveFrom = dto.getEffectiveFrom();
        Date effectiveTo = dto.getEffectiveTo();

        // ✅ Validation: EffectiveFrom must be today or future
        if (effectiveFrom.before(today)) {
            throw new BusinessException("Effective From must be a current or future date.");
        }

        // ✅ Validation: EffectiveTo must be after EffectiveFrom (if provided)
        if (effectiveTo != null && !effectiveTo.after(effectiveFrom)) {
            throw new BusinessException("Effective To must be after Effective From.");
        }

        boolean exists = customerCodeDefinitionRepository.existsByTenantIdAndIsActiveTrue(dto.getTenantId());
        if (exists && !dto.getEffectiveFrom().after(today)) {
            throw new BusinessException("An active configuration already exists for the tenant.");
        }

        int totalLength = 0;

        if (Boolean.TRUE.equals(dto.getIncludeBranchCode())) {
            if (dto.getBranchCodeLength() == null) {
                throw new BusinessException("Branch code length must be specified when branch code is included.");
            }
            totalLength += dto.getBranchCodeLength();
        }

        if (Boolean.TRUE.equals(dto.getIncludeProductType())) {
            if (dto.getProductTypeLength() == null) {
                throw new BusinessException("Product type length must be specified when product type is included.");
            }
            totalLength += dto.getProductTypeLength();
        }

        totalLength += dto.getSerialNoLength();

        if (dto.getSuffix() != null) {
            if (dto.getSuffix().length() > 3) {
                throw new BusinessException("Suffix length must not exceed 3 characters.");
            }
            totalLength += dto.getSuffix().length();
        }

        if (totalLength > dto.getMaxLength()) {
            throw new BusinessException("Defined code components exceed maximum code length.");
        }

        CustomerCodeDefinition entity = convertToEntity(dto);
        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());

        CustomerCodeDefinition saved = customerCodeDefinitionRepository.save(entity);
        return convertToDto(saved);
    }

    @Transactional
    public String generateCustomerCode(Integer tenantId, String branchCode, String productType, Integer userId) {
        Date today = convertLocalDateToUtilDate(LocalDate.now());

        CustomerCodeDefinition codeDef = customerCodeDefinitionRepository
            .findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
                tenantId, today, today
            )
            .orElseThrow(() -> new BusinessException("Active Customer Code Definition not found for tenantId: " + tenantId));

        StringBuilder codeBuilder = new StringBuilder();

        if (Boolean.TRUE.equals(codeDef.getIncludeBranchCode())) {
            if (branchCode == null) {
                throw new BusinessException("Branch code is required but not provided.");
            }
            codeBuilder.append(String.format("%" + codeDef.getBranchCodeLength() + "s", branchCode).replace(' ', '0'));
        }

        if (Boolean.TRUE.equals(codeDef.getIncludeProductType())) {
            if (productType == null) {
                throw new BusinessException("Product type is required but not provided.");
            }
            codeBuilder.append(String.format("%" + codeDef.getProductTypeLength() + "s", productType).replace(' ', '0'));
        }

        Integer newSerial = codeDef.getLastSerialNo() + 1;
        codeBuilder.append(String.format("%0" + codeDef.getSerialNoLength() + "d", newSerial));

        if (codeDef.getSuffix() != null) {
            codeBuilder.append(codeDef.getSuffix());
        }

        if (codeBuilder.length() > codeDef.getMaxLength()) {
            throw new BusinessException("Generated code exceeds maximum allowed length.");
        }

        String finalCode = codeBuilder.toString();
        codeDef.setLastSerialNo(newSerial);
        codeDef.setUpdatedBy(userId);
        codeDef.setUpdatedAt(LocalDateTime.now());

        customerCodeDefinitionRepository.save(codeDef);
        return finalCode;
    }
    private Date convertLocalDateToUtilDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    
    
    
    public List<CustomerCodeDefinitionDto> getActiveDefinitionsByTenant(Integer tenantId) {
        List<CustomerCodeDefinition> definitions = customerCodeDefinitionRepository
                .findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByCreatedAtDesc(tenantId);
        return definitions.stream().map(this::convertToDto).toList();
    }


    @Transactional
    public void deactivateCustomerCodeDefinition(Integer customerCodeDefinitionId, Integer adminId) {
        CustomerCodeDefinition codeDef = customerCodeDefinitionRepository
                .findById(customerCodeDefinitionId)
                .orElseThrow(() -> new BusinessException("Customer Code Definition not found for ID: " + customerCodeDefinitionId));

        if (!codeDef.getIsActive()) {
            throw new BusinessException("Customer Code Definition is already inactive.");
        }

        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        CustomerCodeDefinition newerDef = customerCodeDefinitionRepository
                .findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
                        codeDef.getTenantId(),
                        currentDate,
                        currentDate)
                .orElse(null);

        if (newerDef == null || !newerDef.getCreatedAt().isAfter(codeDef.getCreatedAt())) {
            throw new BusinessException("Cannot deactivate: No newer active definition exists or is effective for tenant ID: " + codeDef.getTenantId());
        }

        codeDef.setIsActive(false);
        codeDef.setUpdatedBy(adminId);
        codeDef.setUpdatedAt(LocalDateTime.now());

        customerCodeDefinitionRepository.save(codeDef);
    }

    private CustomerCodeDefinitionDto convertToDto(CustomerCodeDefinition entity) {
        CustomerCodeDefinitionDto dto = new CustomerCodeDefinitionDto();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setMaxLength(entity.getMaxLength());
        dto.setSuffix(entity.getSuffix());
        dto.setIncludeBranchCode(entity.getIncludeBranchCode());
        dto.setBranchCodeLength(entity.getBranchCodeLength());
        dto.setIncludeProductType(entity.getIncludeProductType());
        dto.setProductTypeLength(entity.getProductTypeLength());
        dto.setLastSerialNo(entity.getLastSerialNo());
        dto.setSerialNoLength(entity.getSerialNoLength());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());
        dto.setIsActive(entity.getIsActive());

        dto.setIsDelete(entity.getIsDelete());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private CustomerCodeDefinition convertToEntity(CustomerCodeDefinitionDto dto) {
        CustomerCodeDefinition entity = new CustomerCodeDefinition();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setMaxLength(dto.getMaxLength());
        entity.setSuffix(dto.getSuffix());
        entity.setIncludeBranchCode(dto.getIncludeBranchCode());
        entity.setBranchCodeLength(dto.getBranchCodeLength());
        entity.setIncludeProductType(dto.getIncludeProductType());
        entity.setProductTypeLength(dto.getProductTypeLength());
        entity.setLastSerialNo(dto.getLastSerialNo());
        entity.setSerialNoLength(dto.getSerialNoLength());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        entity.setIsDelete(dto.getIsDelete() != null ? dto.getIsDelete() : false);
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}
