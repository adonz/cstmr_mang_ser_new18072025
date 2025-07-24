package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.ProductAndServicesModel;

public interface ProductAndServicesRepository extends JpaRepository<ProductAndServicesModel, Integer>{

	ProductAndServicesModel findByTenantIdAndProductAndServiceName(Integer tenantId, String productServiceName);

	ProductAndServicesModel findByTenantIdAndProductAndServiceCode(Integer tenantId, String productServiceCode);

	ProductAndServicesModel findByTenntIdAndProductAndServiceNameAndIsDeleteFalse(Integer tenantId,
			String productServiceName);

	ProductAndServicesModel findByTenantIdAndProductAndServiceCodeAndIsDeleteFalse(Integer tenantId,
			String productServiceCode);

	ProductAndServicesModel findByProductServiceIdAndIsDeleteFalse(Integer productServiceId);

	ProductAndServicesModel findByProductAndServiceCodeAndIsDeleteFalse(String productServiceCode);

	ProductAndServicesModel findByProductAndServiceNameAndIsDeleteFalse(String productServiceCode);

	Page<ProductAndServicesModel> findByIsDeleteFalse(Pageable pageble);

}
