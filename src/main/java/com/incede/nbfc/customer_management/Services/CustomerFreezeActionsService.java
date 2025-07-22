package com.incede.nbfc.customer_management.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerFreezeActionsDto;
import com.incede.nbfc.customer_management.Models.CustomerFreezeActions;
import com.incede.nbfc.customer_management.Repositories.CustomerFreezeActionsRepository;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.*;

@Service
public class CustomerFreezeActionsService {

    private final CustomerFreezeActionsRepository freezeActionsRepository;

    public CustomerFreezeActionsService(CustomerFreezeActionsRepository freezeActionsRepository) {
        this.freezeActionsRepository = freezeActionsRepository;
    }

    // CREATE
    public CustomerFreezeActionsDto createCustomerFreezeAction(CustomerFreezeActionsDto dto) {
        CustomerFreezeActions entity = toEntity(dto);
        CustomerFreezeActions saved = freezeActionsRepository.save(entity);
        return toDto(saved);
    }

    // GET ALL
    public List<CustomerFreezeActionsDto> getAllByCustomerId(Integer customerId) {
        return freezeActionsRepository.findAllByCustomerId(customerId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }


    // GET BY ID
    public CustomerFreezeActionsDto getById(Integer id) {
        CustomerFreezeActions entity = freezeActionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Freeze action not found with ID: " + id));
        return toDto(entity);
    }

    // UPDATE
    public CustomerFreezeActionsDto updateFreezeAction(Integer id, CustomerFreezeActionsDto dto) {
        CustomerFreezeActions existing = freezeActionsRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Freeze Action not found with ID: " + id));

        existing.setCustomerId(dto.getCustomerId());
        existing.setFreezeType(dto.getFreezeType());
        existing.setReason(dto.getReason());
        existing.setEffectiveFrom(dto.getEffectiveFrom());
        existing.setEffectiveTo(dto.getEffectiveTo());
        existing.setStatus(dto.getStatus());
        existing.setIsDelete(dto.getIsDelete());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(dto.getUpdatedAt());

        CustomerFreezeActions updated = freezeActionsRepository.save(existing);
        return toDto(updated);
    }

    // DELETE (Soft Delete)
    public void deleteFreezeAction(Integer id) {
        CustomerFreezeActions entity = freezeActionsRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Freeze Action not found with ID: " + id));

        entity.setIsDelete(true);
        freezeActionsRepository.save(entity);
    }
    
    
    
    public CustomerFreezeActionsDto liftFreezeAction(Integer freezeId, LocalDate effectiveTo, Integer userId) {
        // Step 1: Fetch freeze record
        CustomerFreezeActions freeze = freezeActionsRepository.findById(freezeId)
                .orElseThrow(() -> new BusinessException("Freeze ID not found: " + freezeId));

        // Step 2: Validate it's ACTIVE and not deleted
        if (!"ACTIVE".equalsIgnoreCase(freeze.getStatus())) {
            throw new BusinessException("Freeze is not in ACTIVE status.");
        }
        if (Boolean.TRUE.equals(freeze.getIsDelete())) {
            throw new BusinessException("Freeze record is marked as deleted.");
        }

        // Step 3: Validate effective_to date
        LocalDate today = LocalDate.now();
        LocalDate finalEffectiveTo = effectiveTo != null ? effectiveTo : today;
        if (finalEffectiveTo.isBefore(today)) {
            throw new BusinessException("effective_to must be current or future date.");
        }

        // Step 4: Update status
        freeze.setStatus("LIFTED");
        freeze.setEffectiveTo(finalEffectiveTo);
        freeze.setUpdatedBy(userId);
        freeze.setUpdatedAt(LocalDateTime.now());

        // Step 5: Save and return
        CustomerFreezeActions updated = freezeActionsRepository.save(freeze);
        return toDto(updated);
    }
    
 // CHECK IF CUSTOMER IS UNDER ACTIVE FREEZE
    public List<CustomerFreezeActionsDto> checkIfCustomerIsUnderFreeze(Integer customerId) {
        LocalDate today = LocalDate.now();

        List<CustomerFreezeActions> activeFreezes = freezeActionsRepository.findByCustomerIdAndStatusAndIsDeleteFalseAndEffectiveFromLessThanEqualAndEffectiveToCondition(
                customerId, "ACTIVE", today);

        return activeFreezes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    
    

    
    
    

    // CONVERSION METHODS
    public CustomerFreezeActions toEntity(CustomerFreezeActionsDto dto) {
        CustomerFreezeActions entity = new CustomerFreezeActions();
        entity.setFreezeId(dto.getFreezeId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setFreezeType(dto.getFreezeType());
        entity.setReason(dto.getReason());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        entity.setStatus(dto.getStatus());
        entity.setIsDelete(dto.getIsDelete() != null ? dto.getIsDelete() : false);
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public CustomerFreezeActionsDto toDto(CustomerFreezeActions entity) {
        CustomerFreezeActionsDto dto = new CustomerFreezeActionsDto();
        dto.setFreezeId(entity.getFreezeId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setFreezeType(entity.getFreezeType());
        dto.setReason(entity.getReason());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());
        dto.setStatus(entity.getStatus());
        dto.setIsDelete(entity.getIsDelete());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
