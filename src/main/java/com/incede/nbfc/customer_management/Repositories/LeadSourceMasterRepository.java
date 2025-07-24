package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.LeadSourceMaster;


@Repository
public interface LeadSourceMasterRepository extends JpaRepository<LeadSourceMaster, Integer> {
	
	 boolean existsByTenantIdAndSourceNameIgnoreCaseAndIsDeleteFalse(Integer tenantId, String sourceName);

	    // Get all lead sources by tenant
	    List<LeadSourceMaster> findAllByTenantIdAndIsDeleteFalse(Integer tenantId);

	    // Used in update() and softDelete()
	    Optional<LeadSourceMaster> findBySourceIdAndIsDeleteFalse(Integer sourceId);

	    // Used in getByIdentity(UUID)
	    Optional<LeadSourceMaster> findByIdentityAndIsDeleteFalse(UUID identity);

	    // If you need identity lookup with tenant context too
	    Optional<LeadSourceMaster> findByTenantIdAndIdentityAndIsDeleteFalse(Integer tenantId, UUID identity);


}
