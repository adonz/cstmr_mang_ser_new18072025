package com.incede.nbfc.customer_management.Services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.LeadCustomerMappingDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.LeadCustomerMapping;
import com.incede.nbfc.customer_management.Repositories.LeadCustomerMappingRepository;
import com.incede.nbfc.customer_management.Repositories.LeadMasterRepository;

@Service
public class LeadCustomerMappingService {

    @Autowired
    private LeadCustomerMappingRepository repository;
    
    @Autowired
    private LeadMasterRepository leadRepository;

    public LeadCustomerMappingDTO create(LeadCustomerMappingDTO dto) {
        if (dto.getLeadId() == null || dto.getCustomerId() == null || dto.getCreatedBy() == null) {
        	System.out.println(dto.getCreatedBy());
            throw new BadRequestException("Lead ID, Customer ID, and Created By are mandatory.");
        }

        // 1. Verify lead exists
        if (!leadRepository.existsById(dto.getLeadId())) {
            throw new DataNotFoundException("Lead does not exist.");
        }

        // 2. Prevent duplicate mapping
        boolean mappingExists = repository.existsByLeadIdAndCustomerIdAndIsDeleteFalse(dto.getLeadId(), dto.getCustomerId());
        if (mappingExists) {
            throw new ConflictException("This lead is already mapped to the customer.");
        }

        LeadCustomerMapping entity = toEntity(dto);
        entity.setConversionDate(new Date());
        LeadCustomerMapping saved = repository.save(entity);
        return toDTO(saved);
    }


    public LeadCustomerMappingDTO update(Integer id, LeadCustomerMappingDTO dto) {
        LeadCustomerMapping existing = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Mapping not found"));

        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setCustomerId(dto.getCustomerId());
        existing.setLeadId(dto.getLeadId());
        LeadCustomerMapping updated = repository.save(existing);
        return toDTO(updated);
    }

    public void softDelete(Integer id, Integer userId) {
        LeadCustomerMapping mapping = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Mapping not found"));
        mapping.setIsDelete(true);
        mapping.setUpdatedBy(userId);
        repository.save(mapping);
    }

    public List<LeadCustomerMappingDTO> getAllActive() {
        return repository.findByIsDeleteFalse().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private LeadCustomerMapping toEntity(LeadCustomerMappingDTO dto) {
        LeadCustomerMapping entity = new LeadCustomerMapping();
        entity.setLeadId(dto.getLeadId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCreatedBy(dto.getCreatedBy());
        return entity;
    }

    private LeadCustomerMappingDTO toDTO(LeadCustomerMapping entity) {
        return new LeadCustomerMappingDTO(
                entity.getMappingId(),
                entity.getLeadId(),
                entity.getCustomerId(),
                entity.getConversionDate(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedBy(),
                entity.getUpdatedAt(),
                entity.getIsDelete(),
                entity.getIdentity()
        );
    }
}

