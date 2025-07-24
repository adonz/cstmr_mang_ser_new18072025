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
	private LeadMasterDto leadDto;
	
	@BeforeEach
	public void setUp() {
		
		   LeadMasterDto dto = new LeadMasterDto();
		    dto.setLeadId(1);
		    dto.setTenantId(101);
		    dto.setLeadCode("LD123");
		    dto.setFullName("John Doe");
		    dto.setContactNumber("9876543210");
		    dto.setGenderId(1);
		    dto.setEmailId("john@example.com");

		    dto.setDoorNumber("12B");
		    dto.setAddressLine1("Street 1");
		    dto.setAddressLine2("Near Mall");
		    dto.setLandMark("Big Tower");
		    dto.setPlaceName("Ernakulam");
		    dto.setCity("Cochin");
		    dto.setDistrict("Ernakulam");
		    dto.setStateName("Kerala");
		    dto.setCountryId(91);
		    dto.setPincode("682001");
		    dto.setRemarks("Test lead");
		    dto.setSourceId(10);
		    dto.setStageId(3);
		    dto.setStatusId(1);
		    dto.setInterestedProductId(1001);
		    dto.setIdentity(UUID.randomUUID());
		    dto.setIsDelete(false);
		    dto.setCreatedAt(LocalDateTime.now());
		    dto.setUpdatedAt(LocalDateTime.now());
		    dto.setCreatedBy(5001);
		    dto.setUpdatedBy(5002);
		    
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
	

}
