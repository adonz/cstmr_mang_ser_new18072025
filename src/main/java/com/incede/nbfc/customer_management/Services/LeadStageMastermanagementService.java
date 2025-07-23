package com.incede.nbfc.customer_management.Services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.LeadStageMasterManagementDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
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
	@Transactional(readOnly = true)
	public LeadStageMasterManagementDto getByLeadStageMatserId(Integer leadStageId) {
		 
		 try {
			 LeadStageMasterManagement existingData = leadStageRepository.findByStageIdAndIsDeleteFalse(leadStageId);
			 if(existingData == null) {
				 throw new DataNotFoundException("data not found for id:"+leadStageId);
			 }
 			 return convertToDto(existingData);
		 }
		 catch(BusinessException e) {
			 throw e;
		 }
	}
	
	// update lead stage master management
    @Transactional
	public Integer updateByLeadStageMatserId(LeadStageMasterManagementDto leadStageDto) {
		 try {
			 if(leadStageDto.getStageId() == null) {
				 throw new BusinessException("please provide Stegr id for updating");
			 }
			 if(leadStageDto.getUpdatedBy() == null) throw new BusinessException("updated by should not be empty");
			 LeadStageMasterManagement existingData = leadStageRepository.findByStageIdAndIsDeleteFalse(leadStageDto.getStageId());
			 if(existingData == null ) throw new DataNotFoundException( " Data not found for id :"+leadStageDto.getStageId());
			 
			 existingData.setStageName(leadStageDto.getStageName() !=null? leadStageDto.getStageName():existingData.getStageName());
			 existingData.setTenentId(leadStageDto.getTenentId() !=null? leadStageDto.getTenentId():existingData.getTenentId());
			 existingData.setUpdatedBy(leadStageDto.getUpdatedBy());
			 
			 leadStageRepository.save(existingData);
			 
			 return convertToDto(existingData).getStageId();
		 }
		 catch(BusinessException e) {
			 throw e ;
		 }
		 
	}

	// delete lead stage master management
    @Transactional
	public void softDeleteLeadStageMaster(Integer leadStageId, Integer updatedBy) {
		 try {
			 LeadStageMasterManagement deletingData = leadStageRepository.findByStageIdAndIsDeleteFalse(leadStageId);
			 if(deletingData == null) throw new DataNotFoundException("data not found for id :"+leadStageId);
			 deletingData.setIsDelete(true);
			 deletingData.setUpdatedBy(updatedBy);
			 
			 leadStageRepository.save(deletingData);
		 }
		 catch(BusinessException e) {
			 throw e;
		 }
		
	}
    
	@Transactional(readOnly = true)
	public  Page<LeadStageMasterManagementDto> getAllLeadStageMasterDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<LeadStageMasterManagement> pageResult = leadStageRepository.findByIsDeleteFalse(pageble);
		if(pageResult ==null || pageResult.isEmpty()) return Page.empty(pageble);
		 return pageResult.map(this::convertToDto);
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
