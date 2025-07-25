package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerGroupMappingDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerGroupMappingModel;
import com.incede.nbfc.customer_management.Repositories.CustomerGroupMappingRepository;

@Service
public class CustomerGroupMappingService {
	
private final CustomerGroupMappingRepository groupMappingRepository;
	
	public CustomerGroupMappingService(CustomerGroupMappingRepository groupMappingRepository) {
		this.groupMappingRepository=groupMappingRepository;
	}

	@Transactional
	public Integer createOrUpdateCustomerGroupMapping(CustomerGroupMappingDto groupDto) {
		 try {
			CustomerGroupMappingModel resultGroup;
			CustomerGroupMappingModel customerGroup =  groupMappingRepository.findByGroupMappingId(groupDto.getGroupMappingId());
			System.out.println(groupDto.getGroupMappingId()+"group mapping id");
			 if(customerGroup == null) {
				 Integer ver_customerId = ValidateFieldInteger(groupDto.getCustomerId(),"customer ID");
				 Integer ver_groupId = ValidateFieldInteger(groupDto.getGroupId(),"group ID");
				 Integer ver_createdBy = ValidateFieldInteger(groupDto.getCreatedBy(),"created by");
				if (groupDto.getAssignedDate() == null) {
				    throw new BusinessException("Assigned date cannot be null");
				}
				if (groupDto.getAssignedDate().isAfter(LocalDateTime.now())) {
				    throw new BusinessException("Assigned date must be today or earlier");
				}
				CustomerGroupMappingModel existingGroup = groupMappingRepository.findByCustomerIdAndGroupId(groupDto.getCustomerId(),groupDto.getGroupId());
	 			 if(existingGroup == null) {
				CustomerGroupMappingModel groupModel = new CustomerGroupMappingModel();
				groupModel.setCustomerId(ver_customerId);
				groupModel.setGroupId(ver_groupId);
				groupModel.setAssignedDate(groupDto.getAssignedDate());
				groupModel.setIsActive(true);
				groupModel.setIsDelete(false);
				groupModel.setIdentity(UUID.randomUUID());
				groupModel.setCreatedBy(ver_createdBy);
				groupModel.setCreatedAt(LocalDateTime.now());
				
				resultGroup=groupMappingRepository.save(groupModel);
	 			 }else {
	 				 throw new BusinessException("customer id and group id already exist, duplicate data not allowed");
	 			 }
				 
			 }else {
			     	Integer ver_customerId = ValidateFieldInteger(groupDto.getCustomerId(), "Customer ID");
			     	Integer ver_GroupId = ValidateFieldInteger(groupDto.getGroupId(), "Group ID");
			     	if (groupDto.getAssignedDate() == null) {
		            throw new BusinessException("Assigned date cannot be null");
			     	}
			     	if (groupDto.getAssignedDate().isAfter(LocalDateTime.now())) {
		            throw new BusinessException("Assigned date must be today or earlier");
			     	}
			     	customerGroup.setGroupId(ver_GroupId);
			     	customerGroup.setCustomerId(ver_customerId);
			     	customerGroup.setIsActive(groupDto.getIsActive() !=null? groupDto.getIsActive():customerGroup.getIsActive());
			     	customerGroup.setAssignedDate(groupDto.getAssignedDate());
			     	customerGroup.setUpdatedAt(LocalDateTime.now());
			     	customerGroup.setUpdatedBy(ValidateFieldInteger(groupDto.getUpdatedBy(),"Updated by"));
				
				resultGroup=groupMappingRepository.save(customerGroup);
			 }
			 
			 return convertToDto(resultGroup).getGroupMappingId();
					 
		 }catch( BusinessException e) {
			 throw e;
		 }
		
 		 
	}
	
	
	@Transactional(readOnly = true)
	public CustomerGroupMappingDto getCustomerGroupById(Integer groupMappingId) {
		try {
			CustomerGroupMappingModel customerGroup = groupMappingRepository.findByGroupMappingIdAndIsDeleteFalse(groupMappingId);
			if(customerGroup == null) {
				throw new BusinessException("Details not founf for id:"+groupMappingId);
			}
			return convertToDto(customerGroup);
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = true)
	public  Page<CustomerGroupMappingDto> getAllCustomerGroupDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<CustomerGroupMappingModel> pageResult = groupMappingRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::convertToDto);
	}
	
	
	@Transactional
	public String SoftdeleteCustomerGroupDetailsById(CustomerGroupMappingDto  groupDto) {
		try {
			if(groupDto.getUpdatedBy()==null) {
				throw new BusinessException("Updated by should not be null");
			}
			if(groupDto.getGroupMappingId() == null) {
				throw new BusinessException("Group mapping ID should not be null");
			}
			CustomerGroupMappingModel customerGroupModel = groupMappingRepository.findByGroupMappingIdAndIsDeleteFalse(groupDto.getGroupMappingId());
			if(customerGroupModel == null) {
				throw new BusinessException("Details not found for Id "+ groupDto.getGroupMappingId() );
			}
			customerGroupModel.setIsDelete(true);
			customerGroupModel.setUpdatedBy(groupDto.getUpdatedBy());
			customerGroupModel.setUpdatedAt(LocalDateTime.now());
			
			groupMappingRepository.save(customerGroupModel);
			
			return "customer additional details wit id "+groupDto.getGroupMappingId()+"deleted successfully" ;
		}catch(BusinessException e) {
			throw e;
		}
	}
	
	
	private CustomerGroupMappingDto  convertToDto(CustomerGroupMappingModel GroupModel) {
		 CustomerGroupMappingDto dto = new CustomerGroupMappingDto();

		    dto.setGroupMappingId(GroupModel.getGroupMappingId());
		    dto.setCustomerId(GroupModel.getCustomerId());
		    dto.setGroupId(GroupModel.getGroupId());
		    dto.setAssignedDate(GroupModel.getAssignedDate());
		    dto.setIsActive(GroupModel.getIsActive());
		    dto.setIdentity(GroupModel.getIdentity());
		    dto.setCreatedBy(GroupModel.getCreatedBy());
		    dto.setCreatedAt(GroupModel.getCreatedAt());
		    dto.setUpdatedBy(GroupModel.getUpdatedBy());
		    dto.setUpdatedAt(GroupModel.getUpdatedAt());
		    dto.setIsDelete(GroupModel.getIsDelete());
		    
		    return dto;
	}

	public Integer ValidateFieldInteger(Integer field, String name) {
	    if (field == null) {
	        throw new BusinessException(name + " cannot be null or empty");
	    }
	    return field;
	}

}
