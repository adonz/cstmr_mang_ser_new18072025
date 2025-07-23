package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerRiskProfile;

@Repository
public interface CustomerRiskProfileRepository extends JpaRepository<CustomerRiskProfile, Integer> {

    List<CustomerRiskProfile> findByCustomerIdAndIsDeleteFalse(Integer customerId);

    List<CustomerRiskProfile> findByAssessmentTypeIdAndIsDeleteFalse(Integer assessmentTypeId);

	Optional<CustomerRiskProfile> findByIsDeleteFalse();

	Optional<CustomerRiskProfile> findByCustomerIdAndAssessmentTypeIdAndIsDeleteFalse(Integer customerId,
			Integer assessmentTypeId);

}

