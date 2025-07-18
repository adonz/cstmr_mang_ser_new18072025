package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;


public interface CustomerOptionalDetailsRepository extends JpaRepository<CustomerOptionalDetailsModel, Integer>{

	CustomerOptionalDetailsModel  findByCustomerId(Integer customerId);

 
}
