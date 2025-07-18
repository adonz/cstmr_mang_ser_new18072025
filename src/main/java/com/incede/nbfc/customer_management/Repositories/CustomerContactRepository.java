package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerContact;


@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContact, Integer> {

	
	List<CustomerContact> findByCustomerIdAndIsActiveTrueAndIsDeleteFalse(Integer customerId);
	
	Optional<CustomerContact> findByContactIdAndCustomerIdAndIsDeleteFalse(Integer contactId, Integer customerId);

	boolean existsByContactValueIgnoreCase(String contactValue);

	boolean existsByCustomerIdAndContactTypeAndIsPrimaryTrueAndIsDeleteFalse(
	    Integer customerId, Integer contactType);

}
