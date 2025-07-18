package com.incede.nbfc.customer_management.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationDetailsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerRelationDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerRelationDetailsRepository;

@Service
public class CustomerRelationDetailsService {
	
	private final CustomerRelationDetailsRepository customerRelationDetailsRepository;

    public CustomerRelationDetailsService(CustomerRelationDetailsRepository customerRelationDetailsRepository) {
        this.customerRelationDetailsRepository = customerRelationDetailsRepository;
    }
	
	
	public CustomerRelationDetailsDto createCustomerRelationDetails(CustomerRelationDetailsDto dto) {

	    boolean isMinor = Period.between(dto.getDob(), LocalDate.now()).getYears() < 18;
	    CustomerRelationDetails entity = convertToEntity(dto);
	    entity.setIsMinor(isMinor);
	    entity.setCreatedAt(LocalDateTime.now());
	    entity.setUpdatedAt(LocalDateTime.now());
	    entity.setLoyaltyPoints(dto.getLoyaltyPoints() != null ? dto.getLoyaltyPoints() : 0);
	    entity.setIsDelete(dto.getIsDelete() != null ? dto.getIsDelete() : false);
	    CustomerRelationDetails saved = customerRelationDetailsRepository.save(entity);
	    return convertToDto(saved);
	}


	public CustomerRelationDetailsDto getCustomerRelationDetailsById(Integer id) {
		CustomerRelationDetails entity = customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(id)
				.orElseThrow(() -> new BusinessException("Customer relation details you are looking for is removed/not exists"));
		return convertToDto(entity);
	}
	
	  public void deleteMapping(Integer id) {
		  CustomerRelationDetails entity = customerRelationDetailsRepository.findById(id).orElseThrow(() -> new BusinessException("Customer relation not found with ID: " + id));
	        entity.setIsDelete(true);
	        entity.setUpdatedAt(LocalDateTime.now());
	        customerRelationDetailsRepository.save(entity);
	   }
	  
	  
//	  public CustomerRelationDetailsDto updateCustomerRelationDetails(Integer customerId, CustomerRelationDetailsDto dto) {
//		    CustomerRelationDetails existing = customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)
//		            .orElseThrow(() -> new BusinessException("Customer relation details not found with ID: " + customerId));
//		    existing.setMaritalStatusId(dto.getMaritalStatusId());
//		    existing.setGenderId(dto.getGenderId());
//
//		    if (dto.getDob() != null && !dto.getDob().equals(existing.getDob())) {
//		        existing.setDob(dto.getDob());
//		        boolean isMinor = Period.between(dto.getDob(), LocalDate.now()).getYears() < 18;
//		        existing.setIsMinor(true); 
//		    }
//
//		    if (dto.getAnnualIncome() != null && !dto.getAnnualIncome().equals(existing.getAnnualIncome())) {
//		        existing.setAnnualIncome(dto.getAnnualIncome());
//		        // Example rule: if income > 10L, value score = 90, else 70
//		        int newScore = dto.getAnnualIncome() > 1000000 ? 90 : 70;
//		        existing.setCustomerValueScore(newScore);
//		    }
//
//		    if (dto.getCustomerListType() != null && !dto.getCustomerListType().equals(existing.getCustomerListType())) {
//		        existing.setCustomerListType(dto.getCustomerListType());
//
//		        if ("blacklisted".equalsIgnoreCase(dto.getCustomerListType())) {
//		            // TODO: Trigger alert to compliance module (log or future integration point)
//		            System.out.println("⚠️ Alert: Customer ID " + customerId + " marked as blacklisted. Notify compliance.");
//		        }
//		    }
//
//		    existing.setCustomerValueScore(dto.getCustomerValueScore() != null 
//		        ? dto.getCustomerValueScore() 
//		        : existing.getCustomerValueScore());
//
//		    existing.setUpdatedBy(dto.getUpdatedBy());
//		    existing.setUpdatedAt(LocalDateTime.now());
//
//		    CustomerRelationDetails updated = customerRelationDetailsRepository.save(existing);
//		    return convertToDto(updated);
//		}
	  public CustomerRelationDetailsDto updateCustomerRelationDetails(Integer customerId, CustomerRelationDetailsDto dto) {
		    CustomerRelationDetails existing = customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)
		            .orElseThrow(() -> new BusinessException("Customer relation details not found with ID: " + customerId));
		    existing.setMaritalStatusId(dto.getMaritalStatusId());
		    existing.setGenderId(dto.getGenderId());

		    if (dto.getDob() != null && !dto.getDob().equals(existing.getDob())) {
		        existing.setDob(dto.getDob());
		        // isMinor will be updated automatically by @PreUpdate
		    }

		    if (dto.getAnnualIncome() != null && !dto.getAnnualIncome().equals(existing.getAnnualIncome())) {
		        existing.setAnnualIncome(dto.getAnnualIncome());
		        int newScore = dto.getAnnualIncome() > 1000000 ? 90 : 70;
		        existing.setCustomerValueScore(newScore);
		    }

		    if (dto.getCustomerListType() != null && !dto.getCustomerListType().equals(existing.getCustomerListType())) {
		        existing.setCustomerListType(dto.getCustomerListType());

		        if ("blacklisted".equalsIgnoreCase(dto.getCustomerListType())) {
		            System.out.println("⚠️ Alert: Customer ID " + customerId + " marked as blacklisted. Notify compliance.");
		        }
		    }

		    existing.setCustomerValueScore(dto.getCustomerValueScore() != null
		            ? dto.getCustomerValueScore()
		            : existing.getCustomerValueScore());

		    existing.setUpdatedBy(dto.getUpdatedBy());
		    existing.setUpdatedAt(LocalDateTime.now());

		    CustomerRelationDetails updated = customerRelationDetailsRepository.save(existing);
		    return convertToDto(updated);
		}
	
	

	 private CustomerRelationDetails convertToEntity(CustomerRelationDetailsDto dto) {
		 CustomerRelationDetails entity = new CustomerRelationDetails();
	        entity.setCustomerId(dto.getCustomerId());
	        entity.setMaritalStatusId(dto.getMaritalStatusId());
	        entity.setGenderId(dto.getGenderId());
	        entity.setDob(dto.getDob());
	        entity.setFatherName(dto.getFatherName());
	        entity.setMotherName(dto.getMotherName());
	        entity.setAnnualIncome(dto.getAnnualIncome());
	        entity.setCustomerListType(dto.getCustomerListType());
	        entity.setCustomerValueScore(dto.getCustomerValueScore());
	        entity.setCreatedBy(dto.getCreatedBy());
	        entity.setUpdatedBy(dto.getUpdatedBy());
	        entity.setUpdatedAt(LocalDateTime.now());
	        return entity;
	    }

	    private CustomerRelationDetailsDto convertToDto(CustomerRelationDetails entity) {
	        CustomerRelationDetailsDto dto = new CustomerRelationDetailsDto();
	        dto.setCustomerId(entity.getCustomerId());
	        dto.setMaritalStatusId(entity.getMaritalStatusId());
	        dto.setGenderId(entity.getGenderId());
	        dto.setDob(entity.getDob());
	        dto.setIsMinor(entity.getIsMinor());
	        dto.setFatherName(entity.getFatherName());
	        dto.setMotherName(entity.getMotherName());
	        dto.setAnnualIncome(entity.getAnnualIncome());
	        dto.setCustomerListType(entity.getCustomerListType());
	        dto.setCustomerValueScore(entity.getCustomerValueScore());
	        dto.setLoyaltyPoints(entity.getLoyaltyPoints());
	        dto.setIsDelete(entity.getIsDelete());
	        dto.setCreatedBy(entity.getCreatedBy());
	        dto.setCreatedAt(entity.getCreatedAt());
	        dto.setUpdatedBy(entity.getUpdatedBy());
	        dto.setUpdatedAt(entity.getUpdatedAt());
	        return dto;
	    }
}
