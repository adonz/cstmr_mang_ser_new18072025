package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.ProductAndServicesModel;

public interface ProductAndServicesRepository extends JpaRepository<ProductAndServicesModel, Integer>{

	ProductAndServicesModel findByTenentIdAndProductAndServiceName(Integer tenantId, String productServiceName);

	ProductAndServicesModel findByTenantIdAndProductAndServiceCode(Integer tenantId, String productServiceCode);

	ProductAndServicesModel findByTenentIdAndProductAndServiceNameAndIsDeleteFalse(Integer tenantId,
			String productServiceName);

	ProductAndServicesModel findByTenantIdAndProductAndServiceCodeAndIsDeleteFalse(Integer tenantId,
			String productServiceCode);

	ProductAndServicesModel findByProductServiceIdAndIsDeletedFalse(Integer productServiceId);

	ProductAndServicesModel findByProductAndServiceCodeAndIsDeleteFalse(String productServiceCode);

	ProductAndServicesModel findByProductAndServiceNameAndIsDeleteFalse(String productServiceCode);

	Page<ProductAndServicesModel> findByIsDeleteFalse(Pageable pageble);

}
