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

        assertEquals(1, result.getCustomerId());
    }
    
    
}
