package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.LeadMasterDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.LeadMasterModel;
import com.incede.nbfc.customer_management.Repositories.LeadMasterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class LeadMasterService {

	private final LeadMasterRepository leadRepository;
	
	public LeadMasterService(LeadMasterRepository  leadRepository) {
		this.leadRepository=leadRepository;
	}

	@Transactional
	public Integer createLeadMaster(LeadMasterDto leadDto) {
	    try {
	        System.out.println(leadDto);

 	        Integer ver_createdBy = validateField(leadDto.getCreatedBy(), "created by");
 	        System.out.println("createdby ---------"+ver_createdBy);
	        Integer ver_tenantId = validateField(leadDto.getTenantId(), "tenant Id");
	        System.out.println("tenentid"+ver_tenantId);
	        String ver_leadCode = generateLeadCode();
	        String ver_name = validateField(leadDto.getFullName(), "fullname");
	        Integer ver_sourceId = validateField(leadDto.getSourceId(), "source id");
	        Integer ver_stageId = validateField(leadDto.getStageId(), "stage id");
	        Integer ver_statusId = validateField(leadDto.getStatusId(), "status id");
	        Integer ver_genderId = validateField(leadDto.getGenderId(), "gender id");
	        Integer ver_productId = validateField(leadDto.getInterestedProductId(), "interested product id");

	 
	        LeadMasterModel leadModel = new LeadMasterModel();

	 
	        leadModel.setCreatedBy(ver_createdBy);
	        leadModel.setTenantId(ver_tenantId);
	        leadModel.setLeadCode(ver_leadCode);
	        leadModel.setFullName(ver_name);
	        leadModel.setSourceId(ver_sourceId);
	        leadModel.setStageId(ver_stageId);
	        leadModel.setStatusId(ver_statusId);
	        leadModel.setGenderId(ver_genderId);
	        leadModel.setInterestedProductId(ver_productId);

	 
	        if (validatePhoneNumber(leadDto.getContactNumber())) {
	            leadModel.setContactNumber(leadDto.getContactNumber());
	        }

	 
	        if (validateEmail(leadDto.getEmailId())) {
	            leadModel.setEmailId(leadDto.getEmailId());
	        }

	 
	        leadModel.setAddressLine1(leadDto.getAddressLine1());
	        leadModel.setAddressLine2(leadDto.getAddressLine2());
	        leadModel.setCity(leadDto.getCity());
	        leadModel.setDistrict(leadDto.getDistrict());
	        leadModel.setStateName(leadDto.getStateName());
	        leadModel.setCountryId(leadDto.getCountryId());
	        leadModel.setPincode(leadDto.getPincode());
	        leadModel.setDoorNumber(leadDto.getDoorNumber());
	        leadModel.setLandMark(leadDto.getLandMark());
	        leadModel.setPlaceName(leadDto.getPlaceName());

 	        leadModel.setIdentity(UUID.randomUUID());
	        leadModel.setCreatedAt(LocalDateTime.now());
	        leadModel.setIsDelete(false);

	 
	        LeadMasterModel resultEntity = leadRepository.save(leadModel);
	        
	        return convertToDto(resultEntity).getLeadId();

	    } catch (Exception e) {
	        throw new BusinessException("error: " + e);
	    }
	}
	
	@Transactional
	public Integer updateLeadmaster(LeadMasterDto updateLeadMasterDto) {
		try {
			Integer leadMasterId = validateField(updateLeadMasterDto.getLeadId(),"lead Id ");
			Integer Updated_by = validateField(updateLeadMasterDto.getUpdatedBy(),"updated By");
			LeadMasterModel existigLaedMaster = leadRepository.findByLeadIdAndIsDeleteFalse(leadMasterId);
			if(existigLaedMaster == null) {
				throw new BusinessException("dat not found for id "+leadMasterId);
			}
			existigLaedMaster.setFullName(updateLeadMasterDto.getFullName());
			existigLaedMaster.setContactNumber(updateLeadMasterDto.getContactNumber());
			existigLaedMaster.setEmailId(updateLeadMasterDto.getEmailId());
			existigLaedMaster.setRemarks(updateLeadMasterDto.getRemarks());
			existigLaedMaster.setInterestedProductId(updateLeadMasterDto.getInterestedProductId());
			existigLaedMaster.setStageId(updateLeadMasterDto.getStageId());
			existigLaedMaster.setStatusId(updateLeadMasterDto.getStatusId());
			existigLaedMaster.setCreatedAt(LocalDateTime.now());
			existigLaedMaster.setUpdatedBy(Updated_by);
			 LeadMasterModel updatedData = leadRepository.save(existigLaedMaster);
			 
			return convertToDto(updatedData).getLeadId();			
		}catch(Exception e) {
			throw new BusinessException("error :"+e);
		}
	}
	
	
	@Transactional
	public String softDeleteLeadMaster(LeadMasterDto deleteLeadMasterDto) {
		try {
			Integer leadMasterId = validateField(deleteLeadMasterDto.getLeadId(),"lead Id ");
			Integer Updated_by = validateField(deleteLeadMasterDto.getUpdatedBy(),"updated By");
			LeadMasterModel existigLaedMaster = leadRepository.findByLeadIdAndIsDeleteFalse(leadMasterId);
            if(existigLaedMaster == null) {
            	throw new BusinessException("Data not found for id :"+leadMasterId);
            }
            existigLaedMaster.setIsDelete(true);
            existigLaedMaster.setUpdatedBy(Updated_by);
            
            leadRepository.save(existigLaedMaster);
            
            return "Data deleted successfully for id "+leadMasterId;
		}catch(Exception e) {
			throw new BusinessException("error :"+e);
		}
	}
	
	
	@Transactional(readOnly = true)
	public LeadMasterDto getLeadMasterByLeadId(Integer leadMasterId) {
		try {
			Integer ver_leadId = validateField(leadMasterId,"lead Id ");
			LeadMasterModel existigLaedMaster = leadRepository.findByLeadIdAndIsDeleteFalse(ver_leadId);
			if(existigLaedMaster == null) {
				throw new BusinessException("Data not found for id "+ leadMasterId);
			}
			return convertToDto(existigLaedMaster);
		}catch(Exception e) {
			throw new BusinessException("error :"+e);
		}
	}
	
	
	@Transactional(readOnly = true)
	public  Page<LeadMasterDto> getAllLeadMasterDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<LeadMasterModel> pageResult = leadRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::convertToDto);
	}
	
	
	@Transactional(readOnly = true)
	public LeadMasterDto getLeadMasterByLeadCode( String leadMasterCode) {
		try {
			 String ver_leadCode = validateField(leadMasterCode,"lead Code");
			LeadMasterModel existigLaedMaster = leadRepository.findByLeadCodeAndIsDeleteFalse(ver_leadCode);
			if(existigLaedMaster == null) {
				throw new BusinessException("Data not found for id "+ ver_leadCode);
			}
			return convertToDto(existigLaedMaster);
		}catch(Exception e) {
			throw new BusinessException("error :"+e);
		}
	}
	
	
	
	
	

	
	
	private LeadMasterDto convertToDto(LeadMasterModel leadModel) {
	    LeadMasterDto leadDto = new LeadMasterDto();
	    leadDto.setTenantId(leadModel.getTenantId());
	    leadDto.setLeadCode(leadModel.getLeadCode());
	    leadDto.setFullName(leadModel.getFullName());
	    leadDto.setContactNumber(leadModel.getContactNumber());
	    leadDto.setGenderId(leadModel.getGenderId());
	    leadDto.setEmailId(leadModel.getEmailId());
	    leadDto.setAddressLine1(leadModel.getAddressLine1());
	    leadDto.setAddressLine2(leadModel.getAddressLine2());
	    leadDto.setCity(leadModel.getCity());
	    leadDto.setDistrict(leadModel.getDistrict());
	    leadDto.setStateName(leadModel.getStateName());
	    leadDto.setCountryId(leadModel.getCountryId());
	    leadDto.setPincode(leadModel.getPincode());
	    leadDto.setDoorNumber(leadModel.getDoorNumber());
	    leadDto.setLandMark(leadModel.getLandMark());
	    leadDto.setPlaceName(leadModel.getPlaceName());
	    leadDto.setSourceId(leadModel.getSourceId());
	    leadDto.setStageId(leadModel.getStageId());
	    leadDto.setStatusId(leadModel.getStatusId());
	    leadDto.setInterestedProductId(leadModel.getInterestedProductId());
	    leadDto.setIdentity(leadModel.getIdentity());
	    leadDto.setCreatedBy(leadModel.getCreatedBy());
	    leadDto.setCreatedAt(leadModel.getCreatedAt());
	    leadDto.setIsDelete(leadModel.getIsDelete());
	    return leadDto;
	}

	
	
	
	private int currentYear = Year.now().getValue();
	private int sequenceCounter = 1;

	public synchronized String generateLeadCode() {
	    int year = Year.now().getValue();
	    
 	    if (year != currentYear) {
	        currentYear = year;
	        sequenceCounter = 1;
	    }
	    
	    String prefix = "LD-" + year + "-";
	    String sequence = String.format("%04d", sequenceCounter++);
	    
	    String leadCode = prefix + sequence;
	    
	    return leadCode;
	}

	private boolean validateEmail(String email) {
	    if (email == null || email.trim().isEmpty()) {
	       throw new BusinessException("email should not be null");
	    }
		LeadMasterModel verify_Email = leadRepository.findByEmailIdAndIsDeleteFalse(email);
			if(verify_Email !=null) {
				throw new BusinessException("The Email id already exist in the system and are associated with another lead");
			}

	    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    return email.trim().matches(regex);
	}
	
	
	private boolean validatePhoneNumber(String ver_PhoneNumber) {
	    if (ver_PhoneNumber == null || ver_PhoneNumber.trim().isEmpty()) {
	         throw new BusinessException("mobile number should not be null");
	    }
		LeadMasterModel verify_mobile = leadRepository.findByContactNumberAndIsDeleteFalse(ver_PhoneNumber);
			if(verify_mobile !=null ) {
				throw new BusinessException("The contact number already exist in the system and are associated with another lead");
			}

	    String regex = "^(\\+91[-\\s]?)?(0)?([6-9][0-9]{9}|[1-9][0-9]{1,4}[-\\s]?[0-9]{6,8})$";

	    return ver_PhoneNumber.trim().matches(regex);
	}


	public <T> T validateField (T value ,String fieldName) {
		if(value == null) {
			throw new BusinessException(fieldName+"cannot be empty");
		}
		if(value instanceof String str) {
            str = str.trim().replaceAll("\\s{2,}", " ");
            if(str.isEmpty()) {
    			throw new BusinessException(fieldName+"cannot be empty");
            }
            return (T) str;
		}
		return value;
	}
}

