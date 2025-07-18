package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerNotification;


public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Integer>{


    Optional<CustomerNotification> findByCustomerIdAndIsDeleteFalse(Integer customerId);
    
    boolean existsByCustomerIdAndIsDeleteFalse(Integer customerId);

    List<CustomerNotification> findAllByIsDeleteFalse();
    
}
