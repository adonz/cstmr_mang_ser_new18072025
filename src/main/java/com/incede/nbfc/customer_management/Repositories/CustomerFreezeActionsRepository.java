package com.incede.nbfc.customer_management.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incede.nbfc.customer_management.Models.CustomerFreezeActions;

import feign.Param;

public interface CustomerFreezeActionsRepository extends JpaRepository<CustomerFreezeActions, Integer>{

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
		       "FROM CustomerFreezeActions c " +
		       "WHERE c.customerId = :customerId AND c.status = 'ACTIVE'")
		boolean existsActiveFreezeByCustomerId(@Param("customerId") Integer customerId);
	
	
	@Query("SELECT c FROM CustomerFreezeActions c WHERE c.customerId = :customerId AND c.status = :status")
	List<CustomerFreezeActions> findByCustomerIdAndStatus(@Param("customerId") Integer customerId,
	                                                      @Param("status") String status);
	
	
	List<CustomerFreezeActions> findAllByCustomerId(Integer customerId);

	
	
	@Query("""
		    SELECT c FROM CustomerFreezeActions c
		    WHERE c.customerId = :customerId
		      AND c.status = :status
		      AND c.isDelete = false
		      AND c.effectiveFrom <= :today
		      AND (c.effectiveTo IS NULL OR :today <= c.effectiveTo)
		""")
		List<CustomerFreezeActions> findByCustomerIdAndStatusAndIsDeleteFalseAndEffectiveFromLessThanEqualAndEffectiveToCondition(
		        @Param("customerId") Integer customerId,
		        @Param("status") String status,
		        @Param("today") LocalDate today);

}
