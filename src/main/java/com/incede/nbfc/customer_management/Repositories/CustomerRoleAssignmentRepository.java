package com.incede.nbfc.customer_management.Repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerRoleAssignment;

import feign.Param;


@Repository
public interface CustomerRoleAssignmentRepository extends JpaRepository<CustomerRoleAssignment, Integer>{


	@Query("""
		    SELECT r FROM CustomerRoleAssignment r
		    WHERE r.customerId = :customerId
		      AND r.isDelete = false
		      AND r.isActive = true
		      AND (r.expirationDate IS NULL OR r.expirationDate >= CURRENT_DATE)
		""")
		List<CustomerRoleAssignment> findActiveRolesByCustomer(@Param("customerId") Integer customerId);

	Optional<CustomerRoleAssignment> findByRoleAssignmentIdAndIsDeleteFalse(Integer roleAssignmentId);
	
	@Query("SELECT r FROM CustomerRoleAssignment r " +
		       "WHERE r.expirationDate IS NOT NULL " +
		       "AND r.expirationDate < :currentDate " +
		       "AND r.isActive = true " +
		       "AND r.isDelete = false")
		List<CustomerRoleAssignment> findExpiredActiveAssignments(@Param("currentDate") Date currentDate);

	List<CustomerRoleAssignment> findByCustomerId(Integer customerId);





}
