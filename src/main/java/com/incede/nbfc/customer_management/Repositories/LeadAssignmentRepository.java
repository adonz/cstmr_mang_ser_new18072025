package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incede.nbfc.customer_management.Models.LeadAssignment;

import feign.Param;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignment, Integer> {

	List<LeadAssignment> findByLeadIdAndIsDeleteFalse(Integer leadId);

	@Query("SELECT l FROM LeadAssignment l WHERE l.leadId = :leadId ORDER BY l.assignedOn DESC")
	List<LeadAssignment> findAllByLeadIdOrderByAssignedOnDesc(@Param("leadId") Integer leadId);

	@Query("SELECT COUNT(l) FROM LeadAssignment l WHERE l.leadId = :leadId AND l.isDelete = false")
	Integer countByLeadIdAndDeletedFlagFalse(@Param("leadId") Integer leadId);

	Optional<LeadAssignment> findById(Integer assignmentId);

}
