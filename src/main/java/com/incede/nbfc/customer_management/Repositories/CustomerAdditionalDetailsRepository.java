package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incede.nbfc.customer_management.Models.CustomerAdditionalDetails;

import feign.Param;

public interface CustomerAdditionalDetailsRepository extends JpaRepository<CustomerAdditionalDetails , Integer> {

	boolean existsByCustomerId(Integer customerId);

	@Query("SELECT c FROM CustomerAdditionalDetails c WHERE c.customerId = :customerId AND c.isDelete = FALSE")
    Optional<CustomerAdditionalDetails> findByCustomerIdAndIsDeleteFalse(@Param("customerId") Integer customerId);

	List<CustomerAdditionalDetails> findByIsDeleteFalse();

}
