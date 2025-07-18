package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.incede.nbfc.customer_management.Models.CustomerKYC;


public interface CustomerKYCRepository extends JpaRepository<CustomerKYC, Integer> {

	boolean existsByCustomerIdAndDocumentIdAndIdNumber(Integer customerId, Integer documentId, String idNumber);

	@Query("""
		    SELECT k FROM CustomerKYC k
		    WHERE k.customerId = :customerId
		      AND k.isDelete = false
		      AND k.isActive = true
		      AND (:documentId IS NULL OR k.documentId = :documentId)
		      AND (:isVerified IS NULL OR k.isVerified = :isVerified)
		""")
		List<CustomerKYC> findFilteredKYCs(
		    @Param("customerId") Integer customerId,
		    @Param("documentId") Integer documentId,
		    @Param("isVerified") Boolean isVerified
		);

	@Query("SELECT k FROM CustomerKYC k WHERE k.kycId = :kycId AND k.isActive = TRUE AND k.isDelete = FALSE")
	Optional<CustomerKYC> findActiveByKycId(@Param("kycId") Integer kycId);

	Optional<CustomerKYC> findByKycIdAndIsDeleteFalse(Integer kycId);

	Object findByIsDeleteFalse();

	boolean existsByKycIdAndCustomerIdAndIsActiveTrueAndIsDeleteFalse(Integer kycId, Integer customerId);

}
