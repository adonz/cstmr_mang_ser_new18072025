package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerKYCCKYCUpload;


public interface CustomerKYCCKYCUploadRepository extends JpaRepository<CustomerKYCCKYCUpload, Integer> {
	
    boolean existsByCustomerIdAndUploadStatusAndIsDeleteFalse(Integer customerId, String uploadStatus);

    Optional<CustomerKYCCKYCUpload> findByCkycUploadIdAndIsDeleteFalse(Integer ckycUploadId);

	List<CustomerKYCCKYCUpload> findByCustomerIdAndIsDeleteFalseOrderByUploadDateDesc(Integer customerId);

	Optional<CustomerKYCCKYCUpload> findByCkycUploadIdAndUploadStatusAndIsDeleteFalse(Integer CkycUploadId, String string);

    
}
    


