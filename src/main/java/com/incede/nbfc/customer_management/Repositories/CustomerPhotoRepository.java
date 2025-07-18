package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerPhotoModel;

public interface CustomerPhotoRepository extends JpaRepository<CustomerPhotoModel, Integer>{

 
	Optional<CustomerPhotoModel> findTopByCustomerIdOrderByCaptureTimeDesc(Integer customerId);

 	List<CustomerPhotoModel> findByCustomerIdAndIsDeleteFalse(Integer customerId);

	CustomerPhotoModel findByPhotoIdAndIsDeleteFalseAndIsVerifiedFalse(Integer photoId);

	CustomerPhotoModel findByPhotoIdAndIsDeleteFalse(Integer photoId);

 
}
