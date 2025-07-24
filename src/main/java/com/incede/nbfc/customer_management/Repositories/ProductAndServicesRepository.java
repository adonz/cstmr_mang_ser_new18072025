package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.ProductAndServicesModel;

public interface ProductAndServicesRepository extends JpaRepository<ProductAndServicesModel, Integer>{

 
	Page<ProductAndServicesModel> findByIsDeleteFalse(Pageable pageble);

	ProductAndServicesModel findByProductServiceIdAndIsDeleteFalse(Integer productServiceId);

	ProductAndServicesModel findByProductServiceCodeAndIsDeleteFalse(String productServiceCode);

	ProductAndServicesModel findByTenantIdAndProductServiceNameAndIsDeleteFalse(Integer tenantId,
			String productServiceName);

	ProductAndServicesModel findByTenantIdAndProductServiceCodeAndIsDeleteFalse(Integer tenantId,
			String productServiceCode);

	ProductAndServicesModel findByProductServiceNameAndIsDeleteFalse(String productServiceCode);

}
