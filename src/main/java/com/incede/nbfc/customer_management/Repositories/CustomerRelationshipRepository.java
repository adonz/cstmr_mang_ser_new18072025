package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerRelationship;



@Repository
public interface CustomerRelationshipRepository extends JpaRepository<CustomerRelationship, Integer>{
	
	Optional<CustomerRelationship> findByRelationshipIdAndIsDeleteFalse(Integer relationshipId);

    // Get all active and non-deleted relationships
    List<CustomerRelationship> findByIsDeleteFalseAndIsActiveTrue();

    // (Optional) Check if a staff is already assigned to the customer
    boolean existsByCustomerIdAndStaffIdAndIsDeleteFalse(Integer customerId, Integer staffId);
    
    // (Optional) Get all relationships for a given customer
    List<CustomerRelationship> findByCustomerIdAndIsDeleteFalse(Integer customerId);
    
    
    List<CustomerRelationship> findByCustomerIdAndIsDeleteFalseAndIsActiveTrue(Integer customerId);
	
	
}
