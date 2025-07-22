package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.Customer;
import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;
import com.incede.nbfc.customer_management.Repositories.CustomerOptionalDetailsRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;

@Service
public class CustomerOptionalDetailsService {
	
	private final CustomerOptionalDetailsRepository optionalDetailsRepository;
	private final CustomerRepository customerRepository;
	public CustomerOptionalDetailsService(CustomerOptionalDetailsRepository optionalDetailsRepository,
			CustomerRepository customerRepository) {
		this.optionalDetailsRepository=optionalDetailsRepository;
		this.customerRepository=customerRepository;
	}

	@Transactional
	public Integer createCustomerOptionalDetails(CustomerOptionalDetailsDto dto) {
	    try {
	        Integer customerId = ValidateFieldInteger(dto.getCustomerId(), "Customer ID");
	        CustomerOptionalDetailsModel existingModel = optionalDetailsRepository.findByCustomerId(customerId);
	        if (existingModel != null) {
	            throw new BusinessException("Customer with ID " + customerId + " already has optional details.");
	        }

	        Customer customerModel = customerRepository.findByCustomerIdAndIsDeleteFalse(customerId);
	        if (customerModel == null) {
	            throw new BusinessException("Customer does not exist in customer table: " + customerId);
	        }

	        Integer loanPurposeId = ValidateFieldInteger(dto.getLoanPurposeId(), "Loan purpose ID");
	        String residentialStatus = ValidateFieldString(dto.getResidentialStatus(), "Residential status");
	        Integer createdBy = ValidateFieldInteger(dto.getCreatedBy(), "Created by");

	        CustomerOptionalDetailsModel newModel = new CustomerOptionalDetailsModel();
	        newModel.setCustomerId(customerId);
	        newModel.setLoanPurposeId(loanPurposeId);
	        newModel.setResidentialStatus(residentialStatus);
	        newModel.setEducationalLevel(dto.getEducationalLevel());
	        newModel.setHasHomeLoan(dto.getHasHomeLoan());

	        if (dto.getHasHomeLoan()) {
	            if (dto.getHomeLoanAmount() == null || dto.getHomeLoanAmount() <= 0) {
	                throw new BusinessException("If customer has a home loan, loan amount must not be null or zero");
	            }
	            if (dto.getHomeLoanCompany() == null) {
	                throw new BusinessException("If customer has a home loan, loan company must not be null");
	            }
	            newModel.setHomeLoanAmount(dto.getHomeLoanAmount());
	            newModel.setHomeLoanCompany(dto.getHomeLoanCompany());
	        }

	        newModel.setLanguageId(dto.getLanguageId());
	        newModel.setCreatedBy(createdBy);

	        CustomerOptionalDetailsModel saved = optionalDetailsRepository.save(newModel);
	        return saved.getCustomerId();
	    } catch ( Exception e) {
	    	
	    	throw new BusinessException("Error occurred: " + e.getMessage());
	    }
	}
	
	@Transactional
	public Integer updateCustomerOptionalDetails(CustomerOptionalDetailsDto dto) {
		try {
			Integer ver_customerid = ValidateFieldInteger(dto.getCustomerId(), "customer Id");
			CustomerOptionalDetailsModel optionalDetailsModel = optionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(ver_customerid);
			if(optionalDetailsModel == null) {
				throw new BusinessException("Details not forun for id:"+dto.getCustomerId());
			}
			Integer loanPurposeId = ValidateFieldInteger(dto.getLoanPurposeId(), "Loan purpose ID");
	        String residentialStatus = ValidateFieldString(dto.getResidentialStatus(), "Residential status");
	        Integer updatedBy = ValidateFieldInteger(dto.getUpdatedBy(), "updated by");
	        
	        optionalDetailsModel.setLoanPurposeId(loanPurposeId);
	        optionalDetailsModel.setResidentialStatus(residentialStatus);
	        optionalDetailsModel.setUpdatedBy(updatedBy);
	        optionalDetailsModel.setUpdatedAt(LocalDateTime.now());
	        optionalDetailsModel.setEducationalLevel(dto.getEducationalLevel());
	        optionalDetailsModel.setLanguageId(dto.getLanguageId());
	        optionalDetailsModel.setHasHomeLoan(dto.getHasHomeLoan());
	        
	        if (dto.getHasHomeLoan()) {
	            if (dto.getHomeLoanAmount() == null || dto.getHomeLoanAmount() <= 0) {
	                throw new BusinessException("If customer has a home loan, loan amount must not be null or zero");
	            }
	            if (dto.getHomeLoanCompany() == null) {
	                throw new BusinessException("If customer has a home loan, loan company must not be null");
	            }
	            optionalDetailsModel.setHomeLoanAmount(dto.getHomeLoanAmount());
	            optionalDetailsModel.setHomeLoanCompany(dto.getHomeLoanCompany());
	        } else {
	            optionalDetailsModel.setHomeLoanAmount(null);
	            optionalDetailsModel.setHomeLoanCompany(null);
	        }
	        
	        CustomerOptionalDetailsModel saved = optionalDetailsRepository.save(optionalDetailsModel);
	        return saved.getCustomerId();
		}
		catch(Exception e) {
			
			throw new BusinessException("Error occurred: " + e.getMessage());
		}
	}

	
	@Transactional(readOnly = true)
	public CustomerOptionalDetailsDto getCustomerOptionalDetailsById(Integer customerId) {
		try {
			CustomerOptionalDetailsModel existingCustomer = optionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId);
			if(existingCustomer ==null) {
				throw new BusinessException("no data found");
			}
			return converToDto(existingCustomer);
	}catch(BusinessException e) {
		throw e;
		}
	}

	
	@Transactional
	public String softDeleteCustomerOptionalDetails(CustomerOptionalDetailsDto optionalDetailsDto) {
	    try {
 	         if(optionalDetailsDto.getCustomerId() ==null) {
 	        	 throw new BusinessException("customer id should not be null when deleting");
 	         }
 	         if(optionalDetailsDto.getUpdatedBy()==null) {
 	        	 throw new BusinessException("updated by (ID) should not be empty");
 	         }
	        CustomerOptionalDetailsModel detailsModel = optionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(optionalDetailsDto.getCustomerId());
	        if (detailsModel == null) {
	            throw new BusinessException("Optional details not found for ID: " + optionalDetailsDto.getCustomerId());
	        }

	        detailsModel.setIsDelete(true);  
	        detailsModel.setUpdatedBy(optionalDetailsDto.getUpdatedBy());
	        detailsModel.setUpdatedAt(LocalDateTime.now());  

	        optionalDetailsRepository.save(detailsModel);

	        return "Customer optional details with ID " + optionalDetailsDto.getCustomerId() + " deleted successfully";

	    } catch (BusinessException e) {
	        throw e;
	    }
	}
	
	
	@Transactional(readOnly = true)
	public  Page<CustomerOptionalDetailsDto> getAllCustomerOptionalDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<CustomerOptionalDetailsModel> pageResult = optionalDetailsRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::converToDto);
	}
	
	
	private CustomerOptionalDetailsDto converToDto(CustomerOptionalDetailsModel model) {
	 
	    CustomerOptionalDetailsDto dto = new CustomerOptionalDetailsDto();
	    dto.setCustomerId(model.getCustomerId());
	    dto.setLoanPurposeId(model.getLoanPurposeId());
	    dto.setResidentialStatus(model.getResidentialStatus());
	    dto.setEducationalLevel(model.getEducationalLevel());
	    dto.setHasHomeLoan(model.getHasHomeLoan());
	    dto.setHomeLoanAmount(model.getHomeLoanAmount());
	    dto.setHomeLoanCompany(model.getHomeLoanCompany());
	    dto.setLanguageId(model.getLanguageId());
	    dto.setCreatedBy(model.getCreatedBy());
	    dto.setUpdatedBy(model.getUpdatedBy());
	    dto.setCreatedAt(model.getCreatedAt());
	    dto.setUpdatedAt(model.getUpdatedAt());

	    return dto;
	}
	
	
	public Integer ValidateFieldInteger(Integer field, String name) {
	    if (field == null) {
	        throw new BusinessException(name + " cannot be null or empty");
	    }
	    return field;
	}
	

	public  String ValidateFieldString(String stringField, String name) {
		if(stringField == null || stringField.trim().isEmpty()) {
			throw new BusinessException(name +" cannot be null or empty");
		}
		stringField= stringField.trim().replaceAll("\\s{2,}", " ");
		return  stringField;
	}


 

}
