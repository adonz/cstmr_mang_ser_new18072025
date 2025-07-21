package com.incede.nbfc.customer_management.Repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerGroupMappingModel;

public interface CustomerGroupMappingRepository extends JpaRepository<CustomerGroupMappingModel, Integer> {

    CustomerGroupMappingModel findByGroupMappingId(Integer groupMappingId);

	CustomerGroupMappingModel findByCustomerIdAndGroupId(Integer customerId, Integer groupId);

	CustomerGroupMappingModel findByGroupMappingIdAndIsDeleteFalse(Integer groupMappingId);

	Page<CustomerGroupMappingModel> findByIsDeleteFalse(Pageable pageble);

 
}
