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

	Optional<LeadSourceMaster> findByTenantIdAndIdentityAndIsDeleteFalse(Integer tenantId, UUID identity);

//    // ✅ Or add this if you want to fetch by ID only if not deleted
//    Optional<LeadSourceMaster> findByIdAndIsDeleteFalse(Integer sourceId);
	
	List<LeadSourceMaster> findAllByTenantIdAndIsDeleteFalse(Integer tenantId);


}
