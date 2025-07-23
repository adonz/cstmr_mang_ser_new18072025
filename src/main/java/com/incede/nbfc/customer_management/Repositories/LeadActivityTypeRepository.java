package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.LeadActivityType;


@Repository
public interface LeadActivityTypeRepository extends JpaRepository<LeadActivityType, Integer>{
	
	boolean existsByTenantIdAndTypeNameIgnoreCaseAndIsDeleteFalse(Integer tenantId, String typeName);

    Optional<LeadActivityType> findByActivityTypeIdAndTenantIdAndIsDeleteFalse(Integer activityTypeId, Integer tenantId);

    List<LeadActivityType> findByTenantIdAndIsDeleteFalseOrderByTypeNameAsc(Integer tenantId);

}
