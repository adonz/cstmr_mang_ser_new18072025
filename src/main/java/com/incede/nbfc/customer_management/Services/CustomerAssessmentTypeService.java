package com.incede.nbfc.customer_management.Services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerAssessmentTypeDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerAssessmentType;
import com.incede.nbfc.customer_management.Repositories.CustomerAssessmentTypeRepository;

@Service
public class CustomerAssessmentTypeService {

    @Autowired
    private CustomerAssessmentTypeRepository customerAssessmentTypeRepository;

    public CustomerAssessmentTypeDTO create(CustomerAssessmentTypeDTO dto) {
        try {
            validateCreateDTO(dto);
            CustomerAssessmentType entity = toEntity(dto);
            entity.setIsActive(Boolean.TRUE);
            entity.setIsDelete(Boolean.FALSE);
            entity.setIdentity(UUID.randomUUID());
            CustomerAssessmentType saved = customerAssessmentTypeRepository.save(entity);
            return toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Duplicate assessment type or constraint violation.");
        } catch (BadRequestException e) {
            throw e; // ✅ Let it bubble up
        } catch (Exception e) {
            throw new BusinessException("Unable to create assessment type.");
        }
    }


    public CustomerAssessmentTypeDTO update(Integer id, CustomerAssessmentTypeDTO dto) {
        CustomerAssessmentType existing = customerAssessmentTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Assessment type not found."));

        try {
            validateUpdateDTO(dto);
            existing.setAssessmentName(dto.getAssessmentName());
            existing.setDescription(dto.getDescription());
            existing.setUpdatedBy(dto.getUpdatedBy());
            CustomerAssessmentType updated = customerAssessmentTypeRepository.save(existing);
            return toDTO(updated);
        } catch (BadRequestException e) {
            throw e; // ✅ Preserve intended exception
        } catch (Exception e) {
            throw new BusinessException("Unable to update assessment type.");
        }
    }


    public void toggleStatus(Integer id) {
        CustomerAssessmentType entity = customerAssessmentTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Assessment type not found."));
        try {
            entity.setIsActive(Boolean.FALSE.equals(entity.getIsActive()));
            customerAssessmentTypeRepository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Unable to toggle assessment type status.");
        }
    }

    public void softDelete(Integer id) {
        CustomerAssessmentType entity = customerAssessmentTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Assessment type not found."));
        try {
            entity.setIsDelete(true);
            customerAssessmentTypeRepository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Unable to soft delete assessment type.");
        }
    }

    public List<CustomerAssessmentTypeDTO> getAllActive() {
        try {
            List<CustomerAssessmentType> entities = customerAssessmentTypeRepository.findByIsDeleteFalse();
            return entities.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Failed to fetch assessment types.");
        }
    }

    // 🛡️ Validation Helpers
    private void validateCreateDTO(CustomerAssessmentTypeDTO dto) {
        if (dto.getAssessmentName() == null || dto.getAssessmentName().trim().isEmpty()) {
            throw new BadRequestException("Assessment name is required.");
        }
    }

    private void validateUpdateDTO(CustomerAssessmentTypeDTO dto) {
        if (dto.getAssessmentName() == null || dto.getAssessmentName().trim().isEmpty()) {
            throw new BadRequestException("Assessment name cannot be empty.");
        }
    }

    // 🔄 DTO <-> Entity Mapping
    private CustomerAssessmentType toEntity(CustomerAssessmentTypeDTO dto) {
    	CustomerAssessmentType entity = new CustomerAssessmentType();
        entity.setAssessmentTypeId(dto.getAssessmentTypeId());
        entity.setAssessmentName(dto.getAssessmentName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        return entity;
    }

    private CustomerAssessmentTypeDTO toDTO(CustomerAssessmentType entity) {
        return new CustomerAssessmentTypeDTO(
                entity.getAssessmentTypeId(),
                entity.getAssessmentName(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedBy(),
                entity.getUpdatedAt(),
                entity.getIdentity()
        );
    }
}

