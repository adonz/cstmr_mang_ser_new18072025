package com.incede.nbfc.customer_management.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerFollowup;


@Repository
public interface CustomerFollowupRepository  extends JpaRepository<CustomerFollowup, Integer>{
	
	
	 List<CustomerFollowup> findByCustomerIdAndIsDeleteFalseOrderByFollowupDateDesc(Integer customerId);
	 
//	 
//	 List<CustomerFollowup> findByStaffIdAndStatusAndIsDeleteFalseAndFollowupDateBetweenOrderByFollowupDateAsc(
//			    Integer staffId, Integer status, LocalDate startDate, LocalDate endDate);

	 
	 List<CustomerFollowup> findByStaffIdAndStatusAndIsDeleteFalseAndNextFollowupDateBetweenOrderByNextFollowupDateAsc(
			    Integer staffId, Integer status, LocalDate startDate, LocalDate endDate);

	 List<CustomerFollowup> findByIsDeleteFalseOrderByFollowupDateDesc();


	
}
