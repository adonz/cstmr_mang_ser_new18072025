package com.incede.nbfc.customer_management.Services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerAddressesDto;
import com.incede.nbfc.customer_management.DTOs.CustomerRelationshipDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerAddressesModel;
import com.incede.nbfc.customer_management.Repositories.CustomerAddressesRepository;
import com.incede.nbfc.customer_management.Services.CustomerAddressesService;

@ExtendWith(MockitoExtension.class)
public class CustomerAddressesServiceTest {

    @Mock
    private CustomerAddressesRepository customerAddressRepository;

    @InjectMocks
    private CustomerAddressesService customerAddressesService;

    private CustomerAddressesModel customerAddressesModel;

    @BeforeEach
    void setUp() {
    	
    	CustomerAddressesDto customerAddressesDto = new CustomerAddressesDto();
    	customerAddressesDto.setAddressId(1);
    	customerAddressesDto.setCustomerId(1001);
    	customerAddressesDto.setAddressType("Home");
    	customerAddressesDto.setDoorNumber("12A");
    	customerAddressesDto.setAddressLineOne("1st Cross Street");
    	customerAddressesDto.setAddressLineTwo("Near Park");
    	customerAddressesDto.setLandMark("City Center");
    	customerAddressesDto.setPlaceName("Ernakulam");
    	customerAddressesDto.setCity("Ernakulam");
    	customerAddressesDto.setDistrict("Ernakulam");
    	customerAddressesDto.setStateName("Kerala");
    	customerAddressesDto.setCountry(91);
    	customerAddressesDto.setPincode("682035");
    	customerAddressesDto.setIsActive(true);
    	customerAddressesDto.setIdentity(UUID.randomUUID());
    	customerAddressesDto.setUpdatedBy(1001);
    	
        customerAddressesModel = new CustomerAddressesModel();
        customerAddressesModel.setAddressId(1);
        customerAddressesModel.setCustomerId(1001);
        customerAddressesModel.setAddressType("Home");
        customerAddressesModel.setDoorNumber("12A");
        customerAddressesModel.setAddressLineOne("1st Cross Street");
        customerAddressesModel.setAddressLineTwo("Near Park");
        customerAddressesModel.setLandMark("City Center");
        customerAddressesModel.setPlaceName("Ernakulam");
        customerAddressesModel.setCity("Ernakulam");
        customerAddressesModel.setDistrict("Ernakulam");
        customerAddressesModel.setStateName("Kerala");
        customerAddressesModel.setCountry(91);
        customerAddressesModel.setPincode("682035");
        customerAddressesModel.setIsActive(true);
        customerAddressesModel.setIdentity(UUID.randomUUID());
        customerAddressesModel.setUpdatedBy(1001);
    }

    @Test
    void testGetCustomerAddressById_Success() {
        when(customerAddressRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(1001, 1))
            .thenReturn(customerAddressesModel);

        CustomerAddressesDto result = customerAddressesService
            .getCustomerAddressDetailsByCustomerIdAndAddressId(1001, 1);

        assertNotNull(result);
        assertEquals("Ernakulam", result.getPlaceName());

        verify(customerAddressRepository, times(1))
            .findByCustomerIdAndAddressIdAndIsDeleteFalse(1001, 1);
    }
    
    @Test
    void testGetByIdReturnsDto() {
        when(customerAddressRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(1001,1)).thenReturn((customerAddressesModel));

        CustomerAddressesDto result = customerAddressesService.getCustomerAddressDetailsByCustomerIdAndAddressId( 1001, 1);

        assertEquals(1001, result.getCustomerId());
    }
    
    @Test
   void testGetByIdThrowsException_WhenDetailsNotFound() {
	   when(customerAddressRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(1001, 1)).thenReturn(null);
	   BusinessException thrown = assertThrows(BusinessException.class,
			   () -> customerAddressesService.getCustomerAddressDetailsByCustomerIdAndAddressId(1001, 1)) ;
	   assertEquals("details not forund", thrown.getMessage());
   }
    
    @Test
    void testSoftDeleteCustomerAddress_Success() {
    	when(customerAddressRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(1001, 1)).thenReturn(customerAddressesModel);
    	String result = customerAddressesService.softDeleteCustomerAddressDetails(1001, 1);
    	assertEquals("1customer details deleted successfully",result);
    	assertEquals(1001,customerAddressesModel.getUpdatedBy());
    	
    }
    
    @Test
    void testSoftdeleteCustomerAddress_Error() {
    	when(customerAddressRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(1001, 1)).thenReturn( null);
    	BusinessException thrown = assertThrows(BusinessException.class,
    			()->customerAddressesService.softDeleteCustomerAddressDetails(1001, 1));
    			assertEquals("No customer details found !!",thrown.getMessage());
     }
    
}
