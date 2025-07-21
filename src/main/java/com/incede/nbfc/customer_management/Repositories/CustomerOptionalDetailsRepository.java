package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerOptionalDetailsRepository extends JpaRepository<CustomerOptionalDetailsModel, Integer>{

	CustomerOptionalDetailsModel  findByCustomerId(Integer customerId);

	CustomerOptionalDetailsModel findByCustomerIdAndIsDeleteFalse(Integer customerId);

	Page<CustomerOptionalDetailsModel> findByIsDeleteFalse(Pageable pageble);

 
 
}
