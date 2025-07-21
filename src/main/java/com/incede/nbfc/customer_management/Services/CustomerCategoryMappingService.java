package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerCategoryMappingDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerCategoryMappingModel;
import com.incede.nbfc.customer_management.Repositories.CustomerCategoryMappingRepository;

@Service
public class CustomerCategoryMappingService {

private final CustomerCategoryMappingRepository categoryMappingRepository;
	
	public CustomerCategoryMappingService(CustomerCategoryMappingRepository categoryMappingRepository) {
		this.categoryMappingRepository=categoryMappingRepository;
	}

	@Transactional
	public Integer createOrUpdateCustomerCategoryMapping(CustomerCategoryMappingDto categoryDto) {
 		 try {
 			CustomerCategoryMappingModel resultCategory;
 			 CustomerCategoryMappingModel customerCategory = categoryMappingRepository.findByCategoryMappingId(categoryDto.getCategoryMappingId());
 			 if(customerCategory ==null) {
 				 Integer ver_customerId = ValidateFieldInteger(categoryDto.getCustomerId(),"customer ID");
 				 Integer ver_categoryId = ValidateFieldInteger(categoryDto.getCategoryId(),"category ID");
 				 Integer ver_createdBy = ValidateFieldInteger(categoryDto.getCreatedBy(),"created by");
 				if (categoryDto.getAssignedDate() == null) {
 				    throw new BusinessException("Assigned date cannot be null");
 				}
 				if (categoryDto.getAssignedDate().isAfter(LocalDateTime.now())) {
 				    throw new BusinessException("Assigned date must be today or earlier");
 				}
 				CustomerCategoryMappingModel existingCategory = categoryMappingRepository.findByCustomerIdAndCategoryId(categoryDto.getCustomerId(),categoryDto.getCategoryId());
 	 			 if(existingCategory == null) {
 				CustomerCategoryMappingModel categoryModel = new CustomerCategoryMappingModel();
 				categoryModel.setCustomerId(ver_customerId);
 				categoryModel.setCategoryId(ver_categoryId);
 				categoryModel.setAssignedDate(categoryDto.getAssignedDate());
 				categoryModel.setIsActive(true);
 				categoryModel.setIsDelete(false);
 				categoryModel.setIdentity(UUID.randomUUID());
 				categoryModel.setCreatedBy(ver_createdBy);
 				categoryModel.setCreatedAt(LocalDateTime.now());
 				
 				resultCategory=categoryMappingRepository.save(categoryModel);
 	 			 }else {
 	 				 throw new BusinessException("customer id and category id already exist, duplicate data not allowed");
 	 			 }
 				 
 			 }else {
 			     	Integer ver_customerId = ValidateFieldInteger(categoryDto.getCustomerId(), "Customer ID");
 			     	Integer ver_categoryId = ValidateFieldInteger(categoryDto.getCategoryId(), "Category ID");
 			     	if (categoryDto.getAssignedDate() == null) {
 		            throw new BusinessException("Assigned date cannot be null");
 			     	}
 			     	if (categoryDto.getAssignedDate().isAfter(LocalDateTime.now())) {
 		            throw new BusinessException("Assigned date must be today or earlier");
 			     	}
 				customerCategory.setCategoryId(ver_categoryId);
 				customerCategory.setCustomerId(ver_customerId);
 				customerCategory.setIsActive(categoryDto.getIsActive());
 				customerCategory.setAssignedDate(categoryDto.getAssignedDate());
 				customerCategory.setUpdatedAt(LocalDateTime.now());
 				customerCategory.setUpdatedBy(ValidateFieldInteger(categoryDto.getUpdatedBy(),"Updated by"));
 				
 				resultCategory=categoryMappingRepository.save(customerCategory);
 			 }
 			 
 			 return convertToDto(resultCategory).getCategoryMappingId();
 					 
 		 }catch( BusinessException e) {
 			 throw e;
 		 }
	}
	
	
	@Transactional(readOnly = true)
	public CustomerCategoryMappingDto getCustomerCategoryById(Integer  categoryMappingId) {
		try {
			CustomerCategoryMappingModel customerCategory = categoryMappingRepository.findByCategoryMappingIdAndIsDeleteFalse(categoryMappingId);
			if(customerCategory == null) {
				throw new BusinessException("Details not founf for id:"+categoryMappingId);
			}
			return convertToDto(customerCategory);
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = true)
	public  Page<CustomerCategoryMappingDto> getAllCustomerCategoryDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<CustomerCategoryMappingModel> pageResult = categoryMappingRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::convertToDto);
	}
	
	
	@Transactional
	public String SoftdeleteCustomerCategoryDetailsById(CustomerCategoryMappingDto  categoryDto) {
		try {
			if(categoryDto.getUpdatedBy()==null) {
				throw new BusinessException("Updated by should not be null");
			}
			if(categoryDto.getCategoryMappingId() == null) {
				throw new BusinessException("category mapping ID should not be null");
			}
			CustomerCategoryMappingModel customercategoryModel = categoryMappingRepository.findByCategoryMappingIdAndIsDeleteFalse(categoryDto.getCategoryMappingId());
			if(customercategoryModel == null) {
				throw new BusinessException("Details not found for Id "+ categoryDto.getCategoryMappingId() );
			}
			customercategoryModel.setIsDelete(true);
			customercategoryModel.setUpdatedBy(categoryDto.getUpdatedBy());
			customercategoryModel.setUpdatedAt(LocalDateTime.now());
			
			categoryMappingRepository.save(customercategoryModel);
			
			return "customer additional details wit id "+categoryDto.getCategoryMappingId()+"deleted successfully" ;
		}catch(Exception e) {
			throw e;
		}
	}


	public Integer ValidateFieldInteger(Integer field, String name) {
	    if (field == null) {
	        throw new BusinessException(name + " cannot be null or empty");
	    }
	    return field;
	}
	
	
	private  CustomerCategoryMappingDto convertToDto(CustomerCategoryMappingModel Category) {
		CustomerCategoryMappingDto categoryDto = new CustomerCategoryMappingDto();
		
		categoryDto.setCategoryMappingId(Category.getCategoryMappingId());
		categoryDto.setCustomerId(Category.getCustomerId());
		categoryDto.setCategoryId(Category.getCategoryId());
		categoryDto.setAssignedDate(Category.getAssignedDate());
		categoryDto.setIsActive(Category.getIsActive());
		categoryDto.setIsDelete(Category.getIsDelete());
		categoryDto.setIdentity(Category.getIdentity());
		categoryDto.setCreatedBy(Category.getCreatedBy());
		categoryDto.setCreatedAt(Category.getCreatedAt());
		categoryDto.setUpdatedBy(Category.getUpdatedBy());
		categoryDto.setUpdatedAt(Category.getUpdatedAt());
		
 		return categoryDto;
	}
}
