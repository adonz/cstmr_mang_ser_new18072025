package com.incede.nbfc.customer_management.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerAddressesModel;


public interface CustomerAddressesRepository extends JpaRepository<CustomerAddressesModel, Integer>{

	CustomerAddressesModel findByCustomerIdAndAddressId(Integer customerId, Integer addressId);

	CustomerAddressesModel findByCustomerIdAndAddressIdAndIsDeleteFalse(Integer customerid, Integer addressId);

	Page<CustomerAddressesModel> findByCustomerId(Integer customerId, Pageable pageable);

	Optional<CustomerAddressesModel> findByCustomerIdAndAddressTypeAndIsDeleteFalse(Integer customerId, String addressType);
}
