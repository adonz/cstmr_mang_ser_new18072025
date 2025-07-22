package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.incede.nbfc.customer_management.Models.CustomerAdditionalReferenceValue;

public interface CustomerAdditionalReferenceValueRepository extends JpaRepository<CustomerAdditionalReferenceValue, Integer> {
	
	CustomerAdditionalReferenceValue findByCustomerIdAndCustomerAdditionalReferenceValue(Integer customerId,
			String customerAdditionalReferenceValue);

	CustomerAdditionalReferenceValue findByCustomerAdditionalReferenceValueIdAndIsDeleteFalse(Integer additionalRefId);

	Page<CustomerAdditionalReferenceValue> findByIsDeleteFalse(Pageable pageble);


}
