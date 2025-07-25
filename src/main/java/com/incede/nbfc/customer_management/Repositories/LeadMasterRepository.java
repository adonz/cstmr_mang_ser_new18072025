package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.incede.nbfc.customer_management.Models.LeadMasterModel;

public interface LeadMasterRepository extends JpaRepository<LeadMasterModel, Integer>{


	LeadMasterModel findByLeadId(Integer var_leadId);

	LeadMasterModel findByContactNumberAndIsDeleteFalse(String contactNumber);

	LeadMasterModel findByEmailIdAndIsDeleteFalse(String emailId);

 
	LeadMasterModel findByTenantIdAndLeadCodeStartingWith(Integer tenantId, String prefix);

	Integer countByTenantIdAndLeadCodeStartingWith(Integer tenantId, String prefix);

	LeadMasterModel findByLeadIdAndIsDeleteFalse(Integer leadMasterId);

	Page<LeadMasterModel> findByIsDeleteFalse(Pageable pageble);

	LeadMasterModel findByLeadCodeAndIsDeleteFalse(String ver_leadCode);

	// here year is extracted from created at , want to change it, from lead code it should extract year 
	   @Query("SELECT COUNT(l) FROM LeadMasterModel l " +
	           "WHERE EXTRACT(YEAR FROM l.createdAt) = :year")  
      Integer countByCreationYear(@Param("year") int year);
 
	boolean existsByLeadIdAndIsDeleteFalse(Integer leadId);
 
	
 


	
}
