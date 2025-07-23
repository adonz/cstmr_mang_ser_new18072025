package com.incede.nbfc.customer_management.Services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerRiskProfileDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerRiskProfile;
import com.incede.nbfc.customer_management.Repositories.CustomerRiskProfileRepository;

@Service
public class CustomerRiskProfileService {

    @Autowired
    private CustomerRiskProfileRepository repository;

    public CustomerRiskProfileDTO create(CustomerRiskProfileDTO dto) {
        try {
            validate(dto);

            CustomerRiskProfile entity = toEntity(dto);
            entity.setIdentity(UUID.randomUUID());
            entity.setCreatedBy(dto.getCreatedBy());

            if (dto.getAssessmentDate() == null) {
                entity.setAssessmentDate(new Date());
            }

            CustomerRiskProfile saved = repository.save(entity);
            return toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Duplicate or invalid risk profile record.");
        } catch (Exception e) {
            throw new BusinessException("Unable to create customer risk profile.");
        }
    }

    public CustomerRiskProfileDTO update(Integer id, CustomerRiskProfileDTO dto) {
        CustomerRiskProfile entity = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Risk profile not found."));

        validate(dto);

        entity.setRiskScore(dto.getRiskScore());
        entity.setRiskCategory(dto.getRiskCategory());
        entity.setRiskReason(dto.getRiskReason());
        entity.setAssessmentDate(dto.getAssessmentDate() != null ? dto.getAssessmentDate() : new Date());
        entity.setUpdatedBy(dto.getUpdatedBy());

        CustomerRiskProfile updated = repository.save(entity);
        return toDTO(updated);
    }

    public void softDelete(Integer id, Integer userId) {
        CustomerRiskProfile entity = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Risk profile not found."));

        entity.setIsDelete(true);
        entity.setUpdatedBy(userId);

        repository.save(entity);
    }

    public List<CustomerRiskProfileDTO> getAll() {
        return repository.findByIsDeleteFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void validate(CustomerRiskProfileDTO dto) {
        if (dto.getRiskScore() == null || 
            dto.getRiskScore().compareTo(BigDecimal.ZERO) < 0 || 
            dto.getRiskScore().compareTo(new BigDecimal("100.00")) > 0) {
            throw new BadRequestException("Risk score must be between 0.00 and 100.00");
        }

        if (dto.getRiskCategory() == null || dto.getRiskCategory() < 1 || dto.getRiskCategory() > 4) {
            throw new BadRequestException("Risk category must be between 1 and 4");
        }

        if (dto.getCustomerId() == null || dto.getAssessmentTypeId() == null || dto.getCreatedBy() == null) {
            throw new BadRequestException("Customer ID, Assessment Type, and Created By are mandatory.");
        }
    }

    private CustomerRiskProfile toEntity(CustomerRiskProfileDTO dto) {
        CustomerRiskProfile entity = new CustomerRiskProfile();
        entity.setCustomerId(dto.getCustomerId());
        entity.setAssessmentTypeId(dto.getAssessmentTypeId());
        entity.setRiskScore(dto.getRiskScore());
        entity.setRiskCategory(dto.getRiskCategory());
        entity.setRiskReason(dto.getRiskReason());
        entity.setAssessmentDate(dto.getAssessmentDate());
        entity.setCreatedBy(dto.getCreatedBy());
        return entity;
    }

    private CustomerRiskProfileDTO toDTO(CustomerRiskProfile entity) {
        return new CustomerRiskProfileDTO(
                entity.getRiskId(),
                entity.getCustomerId(),
                entity.getAssessmentTypeId(),
                entity.getRiskScore(),
                entity.getRiskCategory(),
                entity.getAssessmentDate(),
                entity.getRiskReason(),
                entity.getIsDelete(),
                entity.getIdentity(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedBy(),
                entity.getUpdatedAt()
        );
    }
}
