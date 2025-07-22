package com.incede.nbfc.customer_management.Services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalReferenceValueDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerAdditionalReferenceValue;
import com.incede.nbfc.customer_management.Repositories.CustomerAdditionalReferenceValueRepository;

@Service
public class CustomerAdditionalReferenceValueService {
	
private final CustomerAdditionalReferenceValueRepository additionalReferenceValueRepository;
	
	public CustomerAdditionalReferenceValueService(CustomerAdditionalReferenceValueRepository additionalReferenceValueRepository) {
		this.additionalReferenceValueRepository=additionalReferenceValueRepository;
	}
	
	@Transactional
	public Integer createCustomerAdditionalInformation(List<CustomerAdditionalReferenceValueDto> additionalReferenceDto) {
		 
		 try {
			 List<CustomerAdditionalReferenceValue> customerAdditionalInformationList = new ArrayList<>();
			 Integer count =0;
			 
			 for(CustomerAdditionalReferenceValueDto additionalDetails:additionalReferenceDto) {
				 if(additionalDetails.getCustomerId() == null ) {
					 throw new BusinessException("customer Id cannot be null");
				 }
				 if(additionalDetails.getCustomerAdditionalReferenceValue() == null) {
					 throw new BusinessException("customer additional reference value should not be null");
				 }
				 if(additionalDetails.getCreatedBy()==null) {
					 throw new BusinessException("created by should not be empty or null");
				 }
				 CustomerAdditionalReferenceValue additionalInfo = additionalReferenceValueRepository
						 .findByCustomerIdAndCustomerAdditionalReferenceValue(additionalDetails.getCustomerId(),additionalDetails.getCustomerAdditionalReferenceValue());
				 if(additionalInfo !=null) {
					 throw new BusinessException("Customer Id with reference value already exists, duplicate value");
				 }
				 
				 CustomerAdditionalReferenceValue customerDetails = new CustomerAdditionalReferenceValue();
				 customerDetails.setCustomerId(additionalDetails.getCustomerId());
				 customerDetails.setCustomerAdditionalReferenceName(additionalDetails.getCustomerAdditionalReferenceName());
				 customerDetails.setCustomerAdditionalReferenceValue(additionalDetails.getCustomerAdditionalReferenceValue());
				 customerDetails.setIsDelete(false);
				 customerDetails.setIdentity(UUID.randomUUID());
				 customerDetails.setCreatedBy(additionalDetails.getCreatedBy());
				 customerDetails.setCreatedAt(additionalDetails.getCreatedAt());
				 
				 customerAdditionalInformationList.add(customerDetails);
				 count+=1;
			 }
			 List<CustomerAdditionalReferenceValue> customerInfoSaved = additionalReferenceValueRepository.saveAll(customerAdditionalInformationList);
			 return count;
 		 }
		 
		 catch(Exception e) {
			 throw new BusinessException(" "+e);
		 }
	}
	
	@Transactional(readOnly = true)
	public CustomerAdditionalReferenceValueDto getCustomerAdditionalReferenceValueById(Integer AdditionalRefId) {
		try {
			CustomerAdditionalReferenceValue customerAdditionalRef = additionalReferenceValueRepository.findByCustomerAdditionalReferenceValueIdAndIsDeleteFalse(AdditionalRefId);
			if(customerAdditionalRef == null) {
				throw new BusinessException("Details not founf for id:"+AdditionalRefId);
			}
			return convertToDto(customerAdditionalRef);
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional
	public String SoftdeleteCustomerAdditionalReferenceValue(CustomerAdditionalReferenceValueDto additionalRefDto) {
		try {
			if(additionalRefDto.getUpdatedBy()==null) {
				throw new BusinessException("Updated by should not be null");
			}
			if(additionalRefDto.getCustomerAdditionalReferenceValueId() == null) {
				throw new BusinessException("Additional refrence is should not be null");
			}
			CustomerAdditionalReferenceValue customerAddrefModel = additionalReferenceValueRepository.findByCustomerAdditionalReferenceValueIdAndIsDeleteFalse(additionalRefDto.getCustomerAdditionalReferenceValueId());
			if(customerAddrefModel == null) {
				throw new BusinessException("Details not found for Id "+ additionalRefDto.getCustomerAdditionalReferenceValueId());
			}
			customerAddrefModel.setIsDelete(true);
			customerAddrefModel.setUpdatedBy(additionalRefDto.getUpdatedBy());
			customerAddrefModel.setUpdatedAt(LocalDateTime.now());
			
			additionalReferenceValueRepository.save(customerAddrefModel);
			
			return "customer additional details wit id "+additionalRefDto+"deleted successfully" ;
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public CustomerAdditionalReferenceValueDto updatedAdditionalReferenceDetails(CustomerAdditionalReferenceValueDto additionalRefDto) {
		try {
			if(additionalRefDto.getCustomerAdditionalReferenceValueId() ==null) {
				throw new BusinessException("Additional refrence id should not be null for updating");
			}
			if(additionalRefDto.getUpdatedBy() == null) {
				throw new BusinessException("Updated by should not be null or empty");
			}
			CustomerAdditionalReferenceValue updatingAdditionalRef = additionalReferenceValueRepository.findByCustomerAdditionalReferenceValueIdAndIsDeleteFalse(additionalRefDto.getCustomerAdditionalReferenceValueId());
			if(updatingAdditionalRef == null) {
				throw new BusinessException("Details not found for id :"+additionalRefDto.getCustomerAdditionalReferenceValueId());
			}
			
			updatingAdditionalRef.setCustomerAdditionalReferenceName(
					additionalRefDto.getCustomerAdditionalReferenceName()!=null?
					additionalRefDto.getCustomerAdditionalReferenceName():
					updatingAdditionalRef.getCustomerAdditionalReferenceName()	);
			
			updatingAdditionalRef.setCustomerAdditionalReferenceValue(
					additionalRefDto.getCustomerAdditionalReferenceValue() !=null?
					additionalRefDto.getCustomerAdditionalReferenceValue():
					updatingAdditionalRef.getCustomerAdditionalReferenceValue()	);

			updatingAdditionalRef.setCustomerId(additionalRefDto.getCustomerId() !=null?
					additionalRefDto.getCustomerId():updatingAdditionalRef.getCustomerId());
			
			updatingAdditionalRef.setUpdatedBy(additionalRefDto.getUpdatedBy());
			updatingAdditionalRef.setUpdatedAt(LocalDateTime.now());
			
			
			CustomerAdditionalReferenceValue additionalref = additionalReferenceValueRepository.save(updatingAdditionalRef);
			
			return convertToDto(additionalref);
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = true)
	public  Page<CustomerAdditionalReferenceValueDto> getAllCustomerAdditionalReferenceDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<CustomerAdditionalReferenceValue> pageResult = additionalReferenceValueRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::convertToDto);
	}
	
	
	private CustomerAdditionalReferenceValueDto convertToDto(CustomerAdditionalReferenceValue customerAdditionalRef) {
		CustomerAdditionalReferenceValueDto customerAdditionalRefDto = new CustomerAdditionalReferenceValueDto();
		customerAdditionalRefDto.setCustomerAdditionalReferenceValueId(customerAdditionalRef.getCustomerAdditionalReferenceValueId());
		customerAdditionalRefDto.setCustomerId(customerAdditionalRef.getCustomerId());
 		customerAdditionalRefDto.setCustomerAdditionalReferenceName(customerAdditionalRef.getCustomerAdditionalReferenceName());
		customerAdditionalRefDto.setCustomerAdditionalReferenceValue(customerAdditionalRef.getCustomerAdditionalReferenceValue());
		customerAdditionalRefDto.setIsDelete(customerAdditionalRef.getIsDelete());
		customerAdditionalRefDto.setCreatedBy(customerAdditionalRef.getCreatedBy());
		customerAdditionalRefDto.setCreatedAt(customerAdditionalRef.getCreatedAt());
		customerAdditionalRefDto.setUpdatedAt(customerAdditionalRef.getUpdatedAt() !=null?customerAdditionalRef.getUpdatedAt():customerAdditionalRef.getCreatedAt());
		customerAdditionalRefDto.setUpdatedBy(customerAdditionalRef.getUpdatedBy() !=null?customerAdditionalRef.getUpdatedBy():customerAdditionalRef.getCreatedBy());
		customerAdditionalRefDto.setIdentity(customerAdditionalRef.getIdentity());
		
 		return customerAdditionalRefDto;
	}

}
