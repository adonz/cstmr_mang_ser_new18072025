package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerGroupMappingDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerGroupMappingModel;
import com.incede.nbfc.customer_management.Repositories.CustomerGroupMappingRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerGroupMappingServiceTest {
	
	@Mock
	private CustomerGroupMappingRepository groupMappingRepository;
	
	@InjectMocks
	private CustomerGroupMappingService groupMappingSrvice;
	
	private CustomerGroupMappingModel groupMappingModel;
	
	private CustomerGroupMappingDto groupMappingDto;
	
	@BeforeEach
	public void setUp() {
 	    groupMappingDto = new CustomerGroupMappingDto();
	    groupMappingDto.setGroupMappingId(1);
	    groupMappingDto.setCustomerId(101);
	    groupMappingDto.setGroupId(202);
	    groupMappingDto.setAssignedDate(LocalDateTime.now().minusDays(1)); 
	    groupMappingDto.setIsActive(true);
	    groupMappingDto.setIdentity(UUID.randomUUID());
	    groupMappingDto.setIsDelete(false);
	    groupMappingDto.setCreatedAt(LocalDateTime.now().minusDays(5));
	    groupMappingDto.setUpdatedAt(LocalDateTime.now());
	    groupMappingDto.setCreatedBy(1001);
	    groupMappingDto.setUpdatedBy(1002);

 	    groupMappingModel = new CustomerGroupMappingModel();
	    groupMappingModel.setGroupMappingId(1);
	    groupMappingModel.setCustomerId(101);
	    groupMappingModel.setGroupId(202);
	    groupMappingModel.setAssignedDate(LocalDateTime.now().minusDays(1));  
	    groupMappingModel.setIsActive(true);
	    groupMappingModel.setIdentity(groupMappingDto.getIdentity());  
	    groupMappingModel.setIsDelete(false);
	    groupMappingModel.setCreatedAt(LocalDateTime.now().minusDays(5));
	    groupMappingModel.setUpdatedAt(LocalDateTime.now());
	    groupMappingModel.setCreatedBy(1001);
	    groupMappingModel.setUpdatedBy(1002);
	}
	
	@Test
	void getCustomerGroupById_success() {
 	    Integer groupMappingId = 1;
	    when(groupMappingRepository.findByGroupMappingIdAndIsDeleteFalse(groupMappingId)).thenReturn(groupMappingModel);

 	    CustomerGroupMappingDto result = groupMappingSrvice.getCustomerGroupById(groupMappingId);
 	    assertNotNull(result);
	    assertEquals(groupMappingId, result.getGroupMappingId());
	    assertEquals(groupMappingModel.getCustomerId(), result.getCustomerId());
	    verify(groupMappingRepository, times(1)).findByGroupMappingIdAndIsDeleteFalse(groupMappingId);
	}
	
	@Test
	void getCustomerGroupById_notFound_throwsBusinessException() {    
	    Integer groupMappingId = 123;
	    when(groupMappingRepository.findByGroupMappingIdAndIsDeleteFalse(groupMappingId)).thenReturn(null);

 	    BusinessException ex = assertThrows(BusinessException.class, () -> {
	        groupMappingSrvice.getCustomerGroupById(groupMappingId);
	    });
	    assertEquals("Details not founf for id:" + groupMappingId, ex.getMessage());
	    verify(groupMappingRepository, times(1)).findByGroupMappingIdAndIsDeleteFalse(groupMappingId);
	}

}
