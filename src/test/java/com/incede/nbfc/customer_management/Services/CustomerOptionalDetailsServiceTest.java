package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;
import com.incede.nbfc.customer_management.Repositories.CustomerOptionalDetailsRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerOptionalDetailsServiceTest {
	
	@Mock
	private CustomerOptionalDetailsRepository detailsRepository;
	@InjectMocks
	private CustomerOptionalDetailsService detailsService;

	private CustomerOptionalDetailsModel detailsModel;
	
	private CustomerOptionalDetailsDto detailsDto;
	
	
    @BeforeEach
    void setUp() {
    	
    	 detailsDto = new CustomerOptionalDetailsDto();
    	detailsDto.setCustomerId(101);
    	detailsDto.setLoanPurposeId(5);
    	detailsDto.setResidentialStatus("Owned");
    	detailsDto.setEducationalLevel("Graduate");
    	detailsDto.setHasHomeLoan(true);
    	detailsDto.setHomeLoanAmount(250000);
    	detailsDto.setHomeLoanCompany("ABC Bank");
    	detailsDto.setLanguageId(2);
    	detailsDto.setCreatedBy(1001);
    	detailsDto.setUpdatedBy(1001);
    	
    	detailsModel = new CustomerOptionalDetailsModel();
    	detailsModel.setCustomerId(101);
    	detailsModel.setLoanPurposeId(5);
    	detailsModel.setResidentialStatus("Owned");
    	detailsModel.setEducationalLevel("Graduate");
    	detailsModel.setHasHomeLoan(true);
    	detailsModel.setHomeLoanAmount(250000);
    	detailsModel.setHomeLoanCompany("ABC Bank");
    	detailsModel.setLanguageId(2);
    	detailsModel.setCreatedBy(1001);
    	detailsModel.setUpdatedBy(1001);
    }
    
    @Test
    void testGetCustomerOptionalDetailsByCustomerId_Success() {
        when(detailsRepository.findByCustomerIdAndIsDeleteFalse(101))
            .thenReturn(detailsModel);

        CustomerOptionalDetailsDto result = detailsService
            .getCustomerOptionalDetailsById(101);

        assertNotNull(result);
        assertEquals("Graduate", result.getEducationalLevel());
        assertTrue(result.getHasHomeLoan());

        verify(detailsRepository, times(1)).findByCustomerIdAndIsDeleteFalse(101);
    }
    
    @Test
    void softdeletCustomerOptionalDetails_success() {
        when(detailsRepository.findByCustomerIdAndIsDeleteFalse(101))
        .thenReturn(detailsModel);
         String result = detailsService.softDeleteCustomerOptionalDetails(detailsDto);
         assertEquals("Customer optional details with ID 101 deleted successfully", result);
         assertTrue(detailsModel.getIsDelete());
         assertEquals(1001, detailsModel.getUpdatedBy());
         verify(detailsRepository, times(1)).save(detailsModel);

    }
    
    @Test
    void softdeleteCustomerOptionalDetails_fails() {
    	when(detailsRepository.findByCustomerIdAndIsDeleteFalse(101)).thenReturn(null);
    	BusinessException thrown = assertThrows(BusinessException.class,
    		()-> detailsService.softDeleteCustomerOptionalDetails(detailsDto));
    }
    
    @Test
    void softdeleteCustomerOptionalDetails_fails_updatedByIsNull() {
    	detailsDto.setUpdatedBy(null);
    	BusinessException thrown = assertThrows(BusinessException.class,
        		()-> detailsService.softDeleteCustomerOptionalDetails(detailsDto));
    	 assertEquals("updated by (ID) should not be empty", thrown.getMessage());
    }
    
    @Test
    void softdeleteCustomerOptionalDetails_fails_CustomerIdIsNull() {
    	detailsDto.setCustomerId(null);
    	BusinessException thrown = assertThrows(BusinessException.class,
        		()-> detailsService.softDeleteCustomerOptionalDetails(detailsDto));
    	 assertEquals("customer id should not be null when deleting", thrown.getMessage());
    }
    
    
    
}
