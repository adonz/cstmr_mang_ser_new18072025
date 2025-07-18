package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerGroup;


@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Integer> {
	
	boolean existsByTenantIdAndGroupNameIgnoreCaseAndIsDeleteFalse(Integer tenantId, String groupName);

	List<CustomerGroup> findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByGroupName(Integer tenantId);
  
	boolean existsByTenantIdAndGroupNameIgnoreCaseAndGroupIdNotAndIsDeleteFalse(Integer tenantId, String groupName, Integer groupId);

	boolean existsByGroupIdAndIsDeleteFalse(Integer groupId);
	
	Optional<CustomerGroup> findByGroupIdAndIsDeleteFalse(Integer groupId);


}
