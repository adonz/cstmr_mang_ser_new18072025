package com.incede.nbfc.customer_management.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerFollowupDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerFollowup;
import com.incede.nbfc.customer_management.Repositories.CustomerFollowupRepository;

@Service
public class CustomerFollowupService {
	
	
	private final CustomerFollowupRepository customerFollowupRepository;
	
	public CustomerFollowupService(CustomerFollowupRepository customerFollowupRepository){
        this.customerFollowupRepository = customerFollowupRepository;
	}
	
	
	
	
    public CustomerFollowupDto createOrUpdate(CustomerFollowupDto dto) {
        boolean isCreate = dto.getFollowupId() == null;
        CustomerFollowup entity;
        if (isCreate) {
            if (dto.getCreatedBy() == null) {
                throw new BusinessException("createdBy is required for creating a follow-up.");
            }
            dto.setIdentity(UUID.randomUUID());
            dto.setIsDelete(false);
            dto.setCreatedAt(LocalDateTime.now());
            entity = convertToEntity(dto);
        } else {
            entity = customerFollowupRepository.findById(dto.getFollowupId())
                    .orElseThrow(() -> new BusinessException("Follow-up not found with ID: " + dto.getFollowupId()));
            if (Boolean.TRUE.equals(entity.getIsDelete())) {
                throw new BusinessException("Cannot update a deleted follow-up.");
            }
            if (dto.getUpdatedBy() == null) {
                throw new BusinessException("updatedBy is required for updating a follow-up.");
            }
            entity.setCustomerId(dto.getCustomerId());
            entity.setStaffId(dto.getStaffId());
            entity.setFollowupDate(dto.getFollowupDate());
            entity.setFollowupType(dto.getFollowupType());
            entity.setFollowupNotes(dto.getFollowupNotes());
            entity.setNextFollowupDate(dto.getNextFollowupDate());
            entity.setStatus(dto.getStatus());
            entity.setUpdatedBy(dto.getUpdatedBy());
            entity.setUpdatedAt(LocalDateTime.now());
        }
        CustomerFollowup saved = customerFollowupRepository.save(entity);
        return convertToDto(saved);
    }
    
    
    
    public List<CustomerFollowupDto> getFollowupsByCustomerId(Integer customerId) {
        List<CustomerFollowup> followups = customerFollowupRepository
            .findByCustomerIdAndIsDeleteFalseOrderByFollowupDateDesc(customerId);
        if (followups == null || followups.isEmpty()) {
            throw new BusinessException("No follow-up history found for customer ID: " + customerId);
        }
        return followups.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    
    public CustomerFollowupDto updateFollowup(CustomerFollowupDto dto) {
        if (dto.getFollowupId() == null) {
            throw new BusinessException("Follow-up ID is required for update.");
        }
        
        CustomerFollowup existing = customerFollowupRepository.findById(dto.getFollowupId())
                .orElseThrow(() -> new BusinessException("Follow-up not found with ID: " + dto.getFollowupId()));
        if (Boolean.TRUE.equals(existing.getIsDelete())) {
            throw new BusinessException("Cannot update a deleted follow-up record.");
        }
        if (dto.getStatus() == null || dto.getStatus() < 1 || dto.getStatus() > 3) {
            throw new BusinessException("Invalid status value.");
        }
        if (dto.getCustomerId() != null && !existing.getCustomerId().equals(dto.getCustomerId())) {
            throw new BusinessException("Cannot change customer in follow-up record.");
        }
        if (dto.getStaffId() != null && !existing.getStaffId().equals(dto.getStaffId())) {
            throw new BusinessException("Cannot change staff in follow-up record.");
        }
        existing.setStatus(dto.getStatus());
        existing.setFollowupNotes(dto.getFollowupNotes());
        existing.setNextFollowupDate(dto.getNextFollowupDate());

        if (dto.getUpdatedBy() == null) {
            throw new BusinessException("updatedBy is required for update.");
        }
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        CustomerFollowup saved = customerFollowupRepository.save(existing);
        return convertToDto(saved);
    }
    
    
    
    public void softDeleteFollowup(Integer followupId, Integer updatedBy) {
        if (followupId == null) {
            throw new BusinessException("Follow-up ID is required for deletion.");
        }
        if (updatedBy == null) {
            throw new BusinessException("updatedBy is required for deletion.");
        }
        CustomerFollowup existing = customerFollowupRepository.findById(followupId)
                .orElseThrow(() -> new BusinessException("Follow-up not found with ID: " + followupId));

        if (Boolean.TRUE.equals(existing.getIsDelete())) {
            throw new BusinessException("Follow-up is already deleted.");
        }
        existing.setIsDelete(true);
        existing.setUpdatedBy(updatedBy);
        existing.setUpdatedAt(LocalDateTime.now());
        customerFollowupRepository.save(existing);
    }


    public List<CustomerFollowupDto> getUpcomingFollowupsForStaffiWithinSevenDays(Integer staffId) {
        if (staffId == null) {
            throw new BusinessException("Staff ID is required.");
        }
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<CustomerFollowup> followups = customerFollowupRepository
                .findByStaffIdAndStatusAndIsDeleteFalseAndNextFollowupDateBetweenOrderByNextFollowupDateAsc(
                    staffId, 1, today, nextWeek);
        return followups.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    
    public CustomerFollowupDto getFollowupById(Integer followupId) {
        if (followupId == null) {
            throw new BusinessException("Follow-up ID is required.");
        }
        CustomerFollowup followup = customerFollowupRepository.findById(followupId)
                .orElseThrow(() -> new BusinessException("Follow-up not found with ID: " + followupId));
        if (Boolean.TRUE.equals(followup.getIsDelete())) {
            throw new BusinessException("Follow-up record is deleted.");
        }
        return convertToDto(followup);
    }

    
    public List<CustomerFollowupDto> getAllActiveFollowups() {
        List<CustomerFollowup> followups = customerFollowupRepository.findByIsDeleteFalseOrderByFollowupDateDesc();
        if (followups.isEmpty()) {
            throw new BusinessException("No active follow-ups found.");
        }
        return followups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



	
	private CustomerFollowupDto convertToDto(CustomerFollowup entity) {
	    CustomerFollowupDto dto = new CustomerFollowupDto();
	    dto.setFollowupId(entity.getFollowupId());
	    dto.setCustomerId(entity.getCustomerId());
	    dto.setStaffId(entity.getStaffId());
	    dto.setFollowupDate(entity.getFollowupDate());
	    dto.setFollowupType(entity.getFollowupType());
	    dto.setFollowupNotes(entity.getFollowupNotes());
	    dto.setNextFollowupDate(entity.getNextFollowupDate());
	    dto.setStatus(entity.getStatus());
	    dto.setIdentity(entity.getIdentity());
	    dto.setCreatedBy(entity.getCreatedBy());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedBy(entity.getUpdatedBy());
	    dto.setUpdatedAt(entity.getUpdatedAt());
	    dto.setIsDelete(entity.getIsDelete());
	    return dto;
	}
	private CustomerFollowup convertToEntity(CustomerFollowupDto dto) {
	    CustomerFollowup entity = new CustomerFollowup();
	    entity.setFollowupId(dto.getFollowupId());
	    entity.setCustomerId(dto.getCustomerId());
	    entity.setStaffId(dto.getStaffId());
	    entity.setFollowupDate(dto.getFollowupDate());
	    entity.setFollowupType(dto.getFollowupType());
	    entity.setFollowupNotes(dto.getFollowupNotes());
	    entity.setNextFollowupDate(dto.getNextFollowupDate());
	    entity.setStatus(dto.getStatus());
	    entity.setIdentity(dto.getIdentity());
	    entity.setCreatedBy(dto.getCreatedBy());
	    entity.setCreatedAt(dto.getCreatedAt());
	    entity.setUpdatedBy(dto.getUpdatedBy());
	    entity.setUpdatedAt(dto.getUpdatedAt());
	    entity.setIsDelete(dto.getIsDelete());
	    return entity;
	}

}
