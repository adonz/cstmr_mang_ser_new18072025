package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.LeadMasterDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.LeadMasterModel;
import com.incede.nbfc.customer_management.Repositories.LeadMasterRepository;

@ExtendWith(MockitoExtension.class)
public class LeadMasterServiceTest {
	
	@Mock
	private LeadMasterRepository leadRepository;
	@InjectMocks
	private LeadMasterService leadService;
	
	private LeadMasterModel leadModel;
	
	private LeadMasterDto leadMDto;
	
	@BeforeEach
	public void setUp() {
		
			leadMDto  = new LeadMasterDto();
			leadMDto.setLeadId(1);
			leadMDto.setTenantId(101);
			leadMDto.setLeadCode("LD123");
			leadMDto.setFullName("John Doe");
			leadMDto.setContactNumber("9876543210");
			leadMDto.setGenderId(1);
			leadMDto.setEmailId("john@example.com");

			leadMDto.setDoorNumber("12B");
			leadMDto.setAddressLine1("Street 1");
			leadMDto.setAddressLine2("Near Mall");
			leadMDto.setLandMark("Big Tower");
			leadMDto.setPlaceName("Ernakulam");
			leadMDto.setCity("Cochin");
			leadMDto.setDistrict("Ernakulam");
			leadMDto.setStateName("Kerala");
			leadMDto.setCountryId(91);
			leadMDto.setPincode("682001");
			leadMDto.setRemarks("Test lead");
			leadMDto.setSourceId(10);
			leadMDto.setStageId(3);
			leadMDto.setStatusId(1);
			leadMDto.setInterestedProductId(1001);
			leadMDto.setIdentity(UUID.randomUUID());
			leadMDto.setIsDelete(false);
			leadMDto.setCreatedAt(LocalDateTime.now());
			leadMDto.setUpdatedAt(LocalDateTime.now());
			leadMDto.setCreatedBy(5001);
			leadMDto.setUpdatedBy(5002);
		    
		      leadModel = new LeadMasterModel();
		      leadModel.setLeadId(1);
		      leadModel.setTenantId(101);
		      leadModel.setLeadCode("LD123");
		      leadModel.setFullName("John Doe");
		      leadModel.setContactNumber("9876543210");
		      leadModel.setGenderId(1);
		      leadModel.setEmailId("john@example.com");

		      leadModel.setDoorNumber("12B");
		      leadModel.setAddressLine1("Street 1");
		      leadModel.setAddressLine2("Near Mall");
		      leadModel.setLandMark("Big Tower");
		      leadModel.setPlaceName("Ernakulam");
		      leadModel.setCity("Cochin");
		      leadModel.setDistrict("Ernakulam");
		      leadModel.setStateName("Kerala");
		      leadModel.setCountryId(91);
		      leadModel.setPincode("682001");
		    leadModel.setRemarks("Test lead");
		    leadModel.setSourceId(10);
		    leadModel.setStageId(3);
		    leadModel.setStatusId(1);
		    leadModel.setInterestedProductId(1001);
		    leadModel.setIdentity(UUID.randomUUID());
		    leadModel.setIsDelete(false);
		    leadModel.setCreatedAt(LocalDateTime.now());
		    leadModel.setUpdatedAt(LocalDateTime.now());
		    leadModel.setCreatedBy(5001);
		    leadModel.setUpdatedBy(5002);
		
	}
	
    @Test
    void getLeadMasterByLeadId_success() {
        when(leadRepository.findByLeadIdAndIsDeleteFalse(1)).thenReturn(leadModel);
        LeadMasterDto result = leadService.getLeadMasterByLeadId(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("LD123", result.getLeadCode());
        assertEquals("john@example.com", result.getEmailId());
        assertEquals("Cochin", result.getCity());
    }

    @Test
    void getLeadMasterByLeadId_notFound_throwsBusinessException() {
         when(leadRepository.findByLeadIdAndIsDeleteFalse(123)).thenReturn(null);

         BusinessException ex = assertThrows(BusinessException.class, () -> {
        	 leadService.getLeadMasterByLeadId(123);
        });

        assertTrue(ex.getMessage().contains("Data not found for id"));
    }

    @Test
    void getLeadMasterByLeadId_nullId_throwsBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class, () -> {
        	leadService.getLeadMasterByLeadId(null);
        });

        assertTrue(ex.getMessage().contains("lead Id"));
    }
    
    @Test
    void softDeleteLeadMasterById_Success() {
        when(leadRepository.findByLeadIdAndIsDeleteFalse(1)).thenReturn(leadModel);
         String result = leadService.softDeleteLeadMaster(leadMDto);
         assertEquals("Data deleted successfully for id "+1,result);

    }
    
    @Test
    void softDeleteLeadMasterById_Failure() {
        when(leadRepository.findByLeadIdAndIsDeleteFalse(1)).thenReturn(null);
        BusinessException ex = assertThrows(BusinessException.class, () -> {
       	 leadService.softDeleteLeadMaster(leadMDto);
       });

       assertTrue(ex.getMessage().contains("Data not found for id :"+1));

    }
    
	

}
