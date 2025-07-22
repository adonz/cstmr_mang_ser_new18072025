package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.LeadStageMasterManagement;

public interface LeadStageMasterManagementRepository extends JpaRepository<LeadStageMasterManagement, Integer>{

	LeadStageMasterManagement findByStageId(Integer leadStageId);

	LeadStageMasterManagement findByStageIdAndIsDeleteFalse(Integer leadStageId);

	Page<LeadStageMasterManagement> findByIsDeleteFalse(Pageable pageble);

}
