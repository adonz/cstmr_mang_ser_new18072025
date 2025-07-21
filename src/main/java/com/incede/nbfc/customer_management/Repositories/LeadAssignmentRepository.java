package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incede.nbfc.customer_management.Models.LeadAssignment;

import feign.Param;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignment, Integer> {

	List<LeadAssignment> findByLeadIdAndDeletedFlagFalse(Long leadId);
	
	@Query("SELECT l FROM LeadAssignmentEntity l WHERE l.leadId = :leadId ORDER BY l.assignedOn DESC")
    List<LeadAssignment> findAllByLeadIdOrderByAssignedOnDesc(@Param("leadId") Long leadId);

	@Query("SELECT COUNT(l) FROM LeadAssignmentEntity l WHERE l.leadId = :leadId AND l.deletedFlag = false")
	long countByLeadIdAndDeletedFlagFalse(@Param("leadId") Long leadId);

	Optional<LeadAssignment> findById(Long assignmentId);

	
}
