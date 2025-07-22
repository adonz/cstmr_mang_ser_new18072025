package com.incede.nbfc.customer_management.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.LeadCustomerMapping;

public interface LeadCustomerMappingRepository extends JpaRepository<LeadCustomerMapping, Integer> {

	Optional<LeadCustomerMapping> findByIsDeleteFalse();

	boolean existsByLeadIdAndCustomerIdAndIsDeleteFalse(Integer leadId, Integer customerId);

}
