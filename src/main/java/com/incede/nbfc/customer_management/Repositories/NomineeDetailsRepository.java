package com.incede.nbfc.customer_management.Repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incede.nbfc.customer_management.Models.NomineeDetails;

import feign.Param;

public interface NomineeDetailsRepository extends JpaRepository<NomineeDetails, Integer>{

	BigDecimal getTotalShareByCustomerId(Integer customerId);

	Optional<NomineeDetails> findByNomineeIdAndIsDeleteFalse(Integer nomineeId);

	@Query("""
		    SELECT COALESCE(SUM(n.percentageShare), 0)
		    FROM NomineeDetails n
		    WHERE n.customerId = :customerId
		      AND n.isDelete = false
		      AND n.nomineeId != :nomineeId
		""")
		BigDecimal getTotalShareExcludingNominee(@Param("customerId") Integer customerId,
		                                         @Param("nomineeId") Integer nomineeId);


	List<NomineeDetails> findByCustomerIdAndIsDeleteFalse(Integer customerId);

}
