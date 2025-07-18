package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	boolean existsByCustomerCodeAndTenantIdAndIsDeleteFalse(String customerCode, Integer tenantId);

	Optional<Customer> findByCustomerIdAndTenantIdAndIsDeleteFalse(Integer customerId, Integer tenantId);

	List<Customer> findAllByTenantIdAndIsDeleteFalse(Integer tenantId);

	boolean existsByCustomerIdAndIsDeleteFalse(Integer customerId);


}
