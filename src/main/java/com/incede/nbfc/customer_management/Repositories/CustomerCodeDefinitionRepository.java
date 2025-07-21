package com.incede.nbfc.customer_management.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.*;

public interface CustomerCodeDefinitionRepository extends JpaRepository<CustomerCodeDefinition, Integer> {
 
	
	 Optional<CustomerCodeDefinition>
	    findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
	        Integer tenantId, LocalDate fromDate, LocalDate toDate);
	 
	 
	 List<CustomerCodeDefinition> findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByCreatedAtDesc(Integer tenantId);


}
