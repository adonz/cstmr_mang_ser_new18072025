package com.incede.nbfc.customer_management.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerRiskProfileDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerRiskProfile;
import com.incede.nbfc.customer_management.Repositories.CustomerRiskProfileRepository;

@Service
public class CustomerRiskProfileService {

    @Autowired
    private CustomerRiskProfileRepository repository;

    public CustomerRiskProfileDTO create(CustomerRiskProfileDTO dto) {
        validate(dto);

        CustomerRiskProfile entity = toEntity(dto);
        CustomerRiskProfile saved = repository.save(entity);
        return toDTO(saved);
    }

    public CustomerRiskProfileDTO update(Integer id, CustomerRiskProfileDTO dto) {
        CustomerRiskProfile existing = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Risk profile not found"));

        existing.setRiskScore(dto.getRiskScore());
        existing.setRiskCategory(dto.getRiskCategory());
        existing.setRiskReason(dto.getRiskReason());
        existing.setAssessmentDate(dto.getAssessmentDate());
        existing.setUpdatedBy(dto.getUpdatedBy());

        CustomerRiskProfile updated = repository.save(existing);
        return toDTO(updated);
    }

    public void softDelete(Integer id, Integer userId) {
        CustomerRiskProfile existing = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Risk profile not found"));

        existing.setIsDelete(true);
        existing.setUpdatedBy(userId);
        repository.save(existing);
    }

    public List<CustomerRiskProfileDTO> getAll() {
        return repository.findByIsDeleteFalse().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void validate(CustomerRiskProfileDTO dto) {
        if (dto.getRiskScore() == null || dto.getRiskScore().compareTo(BigDecimal.ZERO) < 0 ||
            dto.getRiskScore().compareTo(new BigDecimal("100.00")) > 0) {
            throw new BadRequestException("Risk score must be between 0.00 and 100.00");
        }
        if (dto.getRiskCategory() == null || dto.getRiskCategory() < 1 || dto.getRiskCategory() > 4) {
            throw new BadRequestException("Risk category must be between 1 and 4");
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

