package com.incede.nbfc.customer_management.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.LeadStageMasterManagementDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.LeadStageMasterManagement;
import com.incede.nbfc.customer_management.Repositories.LeadStageMasterManagementRepository;

@Service
public class LeadStageMastermanagementService {
  
	private final LeadStageMasterManagementRepository leadStageRepository;
	
	public LeadStageMastermanagementService(LeadStageMasterManagementRepository leadStageRepository ) {
		this.leadStageRepository=leadStageRepository;
	}

	// create lead stage master management
	@Transactional
	public Integer addLeadStageMaster( LeadStageMasterManagementDto leadStageDto) {
		 try { 
			 if(leadStageDto.getCreatedBy() == null) {
				 throw new BusinessException("created by should not be null for lead stage master");
			 }
			 LeadStageMasterManagement leadStageModel = new LeadStageMasterManagement();
			 leadStageModel.setTenentId(leadStageDto.getTenentId());
			 leadStageModel.setStageName(leadStageDto.getStageName());
			 leadStageModel.setCreatedBy(leadStageDto.getCreatedBy());
			 leadStageModel.setIdentity(UUID.randomUUID());
			 LeadStageMasterManagement leadStage = leadStageRepository.save(leadStageModel);
			 return convertToDto(leadStage).getStageId();
		 }
		 catch(Exception e){
			 throw new BusinessException("error :"+e);
		 }
	}

	// get lead stage master management
	@Transactional
	public List<LeadStageMasterManagementDto> getByLeadStageMatserId(Integer leadStageId) {
		 
		return null;
	}
	
	// update lead stage master management
	@Transactional
	public Integer updateByLeadStageMatserId(LeadStageMasterManagementDto leadStageDto) {
		 
		return null;
	}

	// delete lead stage master management
	@Transactional
	public void softDeleteLeadStageMaster(Integer leadStageId, Integer updatedBy) {
		 
		
	}
	
	
	public LeadStageMasterManagementDto convertToDto(LeadStageMasterManagement entity) {

		LeadStageMasterManagementDto dto = new LeadStageMasterManagementDto();
	    dto.setStageId(entity.getStageId());
	    dto.setTenentId(entity.getTenentId());
	    dto.setStageName(entity.getStageName());
	    dto.setIdentity(entity.getIdentity());
	    dto.setIsDelete(entity.getIsDelete());
	    dto.setCreatedBy(entity.getCreatedBy());
	    dto.setUpdatedBy(entity.getUpdatedBy());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedAt(entity.getUpdatedAt());

	    return dto;
	}

 
}
