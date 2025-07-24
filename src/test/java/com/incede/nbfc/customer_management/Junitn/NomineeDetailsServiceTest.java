package com.incede.nbfc.customer_management.Junitn;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.incede.nbfc.customer_management.DTOs.NomineeDetailsDTO;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerAddressesModel;
import com.incede.nbfc.customer_management.Models.NomineeDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerAddressesRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.Repositories.NomineeDetailsRepository;
import com.incede.nbfc.customer_management.Services.NomineeDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomineeDetailsServiceTest {

    @Mock
    private NomineeDetailsRepository nomineeRepo;

    @Mock
    private CustomerAddressesRepository addressRepo;

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private NomineeDetailsService service;

    private NomineeDetailsDTO validDto;

    @BeforeEach
    void setup() {
        validDto = new NomineeDetailsDTO();
        validDto.setCustomerId(1);
        validDto.setFullName("Test Nominee");
        validDto.setRelationship(1);
        validDto.setDob(LocalDate.of(2010, 1, 1));
        validDto.setContactNumber("1234567890");
        validDto.setIsSameAddress(false);
        validDto.setPercentageShare(BigDecimal.valueOf(50));
        validDto.setIsMinor(false);
        validDto.setCreatedBy(101);
    }

    @Test
    void testAddNominee_ValidNominee_Success() {
        when(customerRepo.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(nomineeRepo.getTotalShareByCustomerId(1)).thenReturn(BigDecimal.ZERO);
        when(nomineeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        NomineeDetailsDTO result = service.addNominee(validDto);

        assertEquals("Test Nominee", result.getFullName());
        verify(nomineeRepo).save(any());
    }

    @Test
    void testAddNominee_CustomerNotFound_ThrowsDataNotFoundException() {
        when(customerRepo.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.addNominee(validDto));
    }

    @Test
    void testAddNominee_ShareExceedsLimit_ThrowsConflictException() {
        when(customerRepo.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(nomineeRepo.getTotalShareByCustomerId(1)).thenReturn(BigDecimal.valueOf(60));

        validDto.setPercentageShare(BigDecimal.valueOf(50)); // total = 110%

        assertThrows(ConflictException.class, () -> service.addNominee(validDto));
    }

//    @Test
//    void testAddNominee_MinorWithoutGuardian_ThrowsBusinessException() {
//        when(customerRepo.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
//        when(nomineeRepo.getTotalShareByCustomerId(1)).thenReturn(BigDecimal.ZERO);
//
//        validDto.setIsMinor(true);
//        validDto.setGuardianName(null);
//
//        assertThrows(BusinessException.class, () -> service.addNominee(validDto));
//    }

    @Test
    void testAddNominee_SameAddressCopiesFields() {
        validDto.setIsSameAddress(true);

        CustomerAddressesModel mockAddr = new CustomerAddressesModel();
        mockAddr.setDoorNumber("123");
        mockAddr.setAddressLineOne("Line 1");
        mockAddr.setAddressLineTwo("Line 2");
        mockAddr.setLandMark("Landmark");
        mockAddr.setPlaceName("Place");
        mockAddr.setCity("City");
        mockAddr.setDistrict("District");
        mockAddr.setStateName("State");
        mockAddr.setCountry(1);
        mockAddr.setPincode("654321");

        when(customerRepo.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(nomineeRepo.getTotalShareByCustomerId(1)).thenReturn(BigDecimal.ZERO);
        when(addressRepo.findByCustomerIdAndAddressTypeAndIsDeleteFalse(1, "Permanant"))
            .thenReturn(Optional.of(mockAddr));
        when(nomineeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        NomineeDetailsDTO result = service.addNominee(validDto);

        assertEquals("123", result.getHouseNumber());
        assertEquals("Line 1", result.getAddressLine1());
    }

    @Test
    void testGetActiveNominees_ReturnsList() {
        NomineeDetails nominee = new NomineeDetails();
        nominee.setFullName("A");
        nominee.setRelationship(1);
        nominee.setContactNumber("999");
        nominee.setPercentageShare(BigDecimal.TEN);
        nominee.setCity("C");
        nominee.setStateName("S");
        nominee.setIsMinor(false);
        nominee.setGuardianName(null);

        when(nomineeRepo.findByCustomerIdAndIsDeleteFalse(1)).thenReturn(List.of(nominee));

        List<Map<String, Object>> result = service.getActiveNominees(1);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).get("fullName"));
    }

    @Test
    void testUpdateNominee_Valid_Success() {
        NomineeDetails nominee = new NomineeDetails();
        nominee.setNomineeId(100);
        nominee.setCustomerId(1);
        when(nomineeRepo.findByNomineeIdAndIsDeleteFalse(100)).thenReturn(Optional.of(nominee));
        when(nomineeRepo.getTotalShareExcludingNominee(1, 100)).thenReturn(BigDecimal.valueOf(40));

        NomineeDetailsDTO dto = new NomineeDetailsDTO();
        dto.setCustomerId(1);
        dto.setFullName("Updated");
        dto.setPercentageShare(BigDecimal.valueOf(50));
        dto.setIsMinor(false);

        service.updateNominee(100, dto, 101);

        assertEquals("Updated", nominee.getFullName());
        verify(nomineeRepo).save(nominee);
    }

    @Test
    void testUpdateNominee_ShareExceedsLimit_ThrowsConflictException() {
        NomineeDetails nominee = new NomineeDetails();
        nominee.setCustomerId(1);

        when(nomineeRepo.findByNomineeIdAndIsDeleteFalse(100)).thenReturn(Optional.of(nominee));
        when(nomineeRepo.getTotalShareExcludingNominee(1, 100)).thenReturn(BigDecimal.valueOf(90));

        NomineeDetailsDTO dto = new NomineeDetailsDTO();
        dto.setCustomerId(1);
        dto.setPercentageShare(BigDecimal.valueOf(20));

        assertThrows(ConflictException.class, () -> service.updateNominee(100, dto, 101));
    }

    @Test
    void testSoftDeleteNominee_Success() {
        NomineeDetails nominee = new NomineeDetails();
        when(nomineeRepo.findByNomineeIdAndIsDeleteFalse(100)).thenReturn(Optional.of(nominee));

        service.softDeleteNominee(100, 101);

        assertTrue(nominee.getIsDelete());
        verify(nomineeRepo).save(nominee);
    }

    @Test
    void testSoftDeleteNominee_NotFound_ThrowsException() {
        when(nomineeRepo.findByNomineeIdAndIsDeleteFalse(100)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.softDeleteNominee(100, 101));
    }
}
