package com.incede.nbfc.customer_management.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.CustomerAssessmentType;

public interface CustomerAssessmentTypeRepository extends JpaRepository<CustomerAssessmentType, Integer>{

	List<CustomerAssessmentType> findByIsDeleteFalse();

}
