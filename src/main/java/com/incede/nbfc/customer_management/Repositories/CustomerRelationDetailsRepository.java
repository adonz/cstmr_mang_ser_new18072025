package com.incede.nbfc.customer_management.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerRelationDetails;



@Repository
public interface CustomerRelationDetailsRepository extends JpaRepository<CustomerRelationDetails, Integer>{
	
	 Optional<CustomerRelationDetails> findByCustomerIdAndIsDeleteFalse(Integer roleId);

}
