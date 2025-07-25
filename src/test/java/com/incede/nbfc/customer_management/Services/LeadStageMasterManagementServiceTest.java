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

import com.incede.nbfc.customer_management.DTOs.LeadStageMasterManagementDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.LeadStageMasterManagement;
import com.incede.nbfc.customer_management.Repositories.LeadStageMasterManagementRepository;

 
@ExtendWith(MockitoExtension.class)
public class LeadStageMasterManagementServiceTest {

	@Mock
	private LeadStageMasterManagementRepository leadStageRepository;
	@InjectMocks
	private LeadStageMastermanagementService leadStageService;
	
	private LeadStageMasterManagement leadStageModel;
	private LeadStageMasterManagementDto leadStageDto;
	
	@BeforeEach
	public void setUp() {
		
		leadStageDto =  new LeadStageMasterManagementDto();
	 	 leadStageDto.setStageId(1);
 	 	 leadStageDto.setTenentId(1001);
 	 	 leadStageDto.setStageName("pending");
	 	 leadStageDto.setIdentity(UUID.randomUUID());
	 	leadStageDto.setIsDelete(false);	 	
	 	 leadStageDto.setCreatedBy(1);
	 	leadStageDto.setUpdatedBy(1);
	 	 leadStageDto.setCreatedAt(LocalDateTime.now());
	 	 leadStageDto.setUpdatedAt(LocalDateTime.now()); 
	 	 
	 	leadStageModel =  new LeadStageMasterManagement();
	 	leadStageModel.setStageId(1);
	 	leadStageModel.setTenentId(1001);
	 	leadStageModel.setStageName("pending");
	 	leadStageModel.setIdentity(UUID.randomUUID());
	 	leadStageModel.setIsDelete(false);	 	
	 	leadStageModel.setCreatedBy(1);
	 	leadStageModel.setUpdatedBy(1);
	 	leadStageModel.setCreatedAt(LocalDateTime.now());
	 	leadStageModel.setUpdatedAt(LocalDateTime.now()); 
	 
	}
	
	@Test
	void getLeadStageMasterDetailsByStageId_success() {
		when(leadStageRepository.findByStageIdAndIsDeleteFalse(1)).thenReturn(leadStageModel);
		LeadStageMasterManagementDto result  = leadStageService.getByLeadStageMatserId(1);
		 assertNotNull(result);
	        assertEquals(1001, result.getTenentId());
	        assertEquals("pending", result.getStageName());
	        assertEquals(1, result.getStageId());
	        assertEquals(1, result.getCreatedBy());
	}
	
	@Test
	void getLeadStageMasterDetailsByStageId_fails() {
		when(leadStageRepository.findByStageIdAndIsDeleteFalse(1)).thenReturn(null);
		DataNotFoundException exp = assertThrows(DataNotFoundException.class,
				()->{leadStageService.getByLeadStageMatserId(1);});
		assertTrue(exp.getMessage().contains("data not found for id:"+1));

	}
	
	@Test
	void softDeleteLeadStageMasterDetails_Success() {
		when(leadStageRepository.findByStageIdAndIsDeleteFalse(1)).thenReturn(leadStageModel);
		 String result  = leadStageService.softDeleteLeadStageMaster(1,1);
		 assertEquals("Data delete successfully for id :"+1, result);

	}
	
	@Test
	void softDeleteLeadStageMasterDetails_Failure() {
		when(leadStageRepository.findByStageIdAndIsDeleteFalse(1)).thenReturn(null);
		DataNotFoundException exp = assertThrows(DataNotFoundException.class,
				()->{leadStageService.softDeleteLeadStageMaster(1,1);});
		assertTrue(exp.getMessage().contains("data not found for id :"+1));
	}
	
	
	
	
}
