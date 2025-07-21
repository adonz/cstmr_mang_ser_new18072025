package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerCategoryMappingModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCategoryMappingRepository extends JpaRepository<CustomerCategoryMappingModel, Integer>{
	
	CustomerCategoryMappingModel findByCategoryMappingId(Integer categoryMappingId);

	CustomerCategoryMappingModel findByCustomerIdAndCategoryId(Integer customerId, Integer categoryId);

	CustomerCategoryMappingModel findByCategoryMappingIdAndIsDeleteFalse(Integer categoryMappingId);

	Page<CustomerCategoryMappingModel> findByIsDeleteFalse(Pageable pageble);

}
