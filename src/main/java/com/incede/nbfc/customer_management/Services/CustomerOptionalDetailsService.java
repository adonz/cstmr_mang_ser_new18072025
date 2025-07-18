package com.incede.nbfc.customer_management.Services;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;
import com.incede.nbfc.customer_management.Repositories.CustomerOptionalDetailsRepository;

@Service
public class CustomerOptionalDetailsService {
	
	private final CustomerOptionalDetailsRepository optionalDetailsRepository;
	
	public CustomerOptionalDetailsService(CustomerOptionalDetailsRepository optionalDetailsRepository) {
		this.optionalDetailsRepository=optionalDetailsRepository;
	}

	// note the lone purpose id and customer is fk and should be validated from masters
	public Integer createCustomerOptionalDetails(CustomerOptionalDetailsDto dto) {
	    if (dto.getCustomerId() == null) {
	        throw new BusinessException("Customer ID should not be null");
	    }
	    if (dto.getLoanPurposeId() == null) {
	        throw new BusinessException("Loan purpose ID should not be null");
	    }
	    if (dto.getResidentialStatus() == null) {
	        throw new BusinessException("Residential status should not be null");
	    }

	    CustomerOptionalDetailsModel model = optionalDetailsRepository.findByCustomerId(dto.getCustomerId());

	    if (model != null) {
	        
	        model.setLoanPurposeId(dto.getLoanPurposeId());
	        model.setResidentialStatus(dto.getResidentialStatus());
	        model.setEducationalLevel(dto.getEducationalLevel());
	        model.setHasHomeLoan(dto.getHasHomeLoan());
	        
	        if (dto.getHasHomeLoan()) {
	            if (dto.getHomeLoanAmount() == null) {
	                throw new BusinessException("If customer has a home loan, loan amount must not be null");
	            }
	            if (dto.getHomeLoanCompany() == null) {
	                throw new BusinessException("If customer has a home loan, loan company must not be null");
	            }
	            model.setHomeLoanAmount(dto.getHomeLoanAmount());
	            model.setHomeLoanCompany(dto.getHomeLoanCompany());
	        } else {
	            model.setHomeLoanAmount(null);
	            model.setHomeLoanCompany(null);
	        }

	        model.setLanguageId(dto.getLanguageId());
	        model.setUpdatedBy(dto.getUpdatedBy());

	        CustomerOptionalDetailsModel saved = optionalDetailsRepository.save(model);
	        return saved.getCustomerId();  
	    } else {
	        
	        if (dto.getCreatedBy() == null) {
	            throw new BusinessException("Created by should not be null");
	        }

	        CustomerOptionalDetailsModel newModel = new CustomerOptionalDetailsModel();
	        newModel.setCustomerId(dto.getCustomerId());
	        newModel.setLoanPurposeId(dto.getLoanPurposeId());
	        newModel.setResidentialStatus(dto.getResidentialStatus());
	        newModel.setEducationalLevel(dto.getEducationalLevel());
	        newModel.setHasHomeLoan(dto.getHasHomeLoan());

	        if (dto.getHasHomeLoan()) {
	            if (dto.getHomeLoanAmount() == null) {
	                throw new BusinessException("If customer has a home loan, loan amount must not be null");
	            }
	            if (dto.getHomeLoanCompany() == null) {
	                throw new BusinessException("If customer has a home loan, loan company must not be null");
	            }
	            newModel.setHomeLoanAmount(dto.getHomeLoanAmount());
	            newModel.setHomeLoanCompany(dto.getHomeLoanCompany());
	        }

	        newModel.setLanguageId(dto.getLanguageId());
	        newModel.setCreatedBy(dto.getCreatedBy());

	        CustomerOptionalDetailsModel saved = optionalDetailsRepository.save(newModel);
	        return saved.getCustomerId();  
	    }
	}


 

}
