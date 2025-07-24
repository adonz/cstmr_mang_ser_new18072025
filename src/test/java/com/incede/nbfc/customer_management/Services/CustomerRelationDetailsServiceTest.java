//package com.incede.nbfc.customer_management.Services;
//
//import com.incede.nbfc.customer_management.DTOs.CustomerRelationDetailsDto;
//import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
//import com.incede.nbfc.customer_management.Models.CustomerRelationDetails;
//import com.incede.nbfc.customer_management.Repositories.CustomerRelationDetailsRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CustomerRelationDetailsServiceTest {
//
//    @Mock
//    private CustomerRelationDetailsRepository customerRelationDetailsRepository;
//
//    @InjectMocks
//    private CustomerRelationDetailsService customerRelationDetailsService;
//
//    private CustomerRelationDetailsDto validDto;
//    private CustomerRelationDetails entity;
//    private Integer customerId;
//
//    @BeforeEach
//    void setUp() {
//        customerId = 1;
//
//        validDto = new CustomerRelationDetailsDto();
//        validDto.setCustomerId(customerId);
//        validDto.setMaritalStatusId(1);
//        validDto.setGenderId(1);
//        validDto.setDob(LocalDate.of(2000, 1, 1)); // Age > 18
//        validDto.setFatherName("John Doe");
//        validDto.setMotherName("Jane Doe");
//        validDto.setAnnualIncome(1200000);
//        validDto.setCustomerListType("normal");
//        validDto.setCustomerValueScore(90);
//        validDto.setLoyaltyPoints(100);
//        validDto.setCreatedBy(100);
//        validDto.setUpdatedBy(100);
//
//        entity = new CustomerRelationDetails();
//        entity.setCustomerId(customerId);
//        entity.setMaritalStatusId(1);
//        entity.setGenderId(1);
//        entity.setDob(LocalDate.of(2000, 1, 1));
//        entity.setIsMinor(false);
//        entity.setFatherName("John Doe");
//        entity.setMotherName("Jane Doe");
//        entity.setAnnualIncome(1200000);
//        entity.setCustomerListType("normal");
//        entity.setCustomerValueScore(90);
//        entity.setLoyaltyPoints(100);
//        entity.setIsDelete(false);
//        entity.setCreatedBy(100);
//        entity.setCreatedAt(LocalDateTime.now());
//        entity.setUpdatedBy(100);
//        entity.setUpdatedAt(LocalDateTime.now());
//    }
//
//    // Tests for createCustomerRelationDetails
//    @Test
//    void createCustomerRelationDetails_ValidDto_ReturnsDto() {
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);
//
//        assertNotNull(result);
//        assertEquals(customerId, result.getCustomerId());
//        assertEquals("John Doe", result.getFatherName());
//        assertFalse(result.getIsMinor()); // Age > 18
//        assertEquals(100, result.getLoyaltyPoints());
//        assertFalse(result.getIsDelete());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void createCustomerRelationDetails_MinorAge_SetsIsMinorTrue() {
//        validDto.setDob(LocalDate.now().minusYears(17)); // Age < 18
//        entity.setDob(LocalDate.now().minusYears(17));
//        entity.setIsMinor(true);
//
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);
//
//        assertNotNull(result);
//        assertTrue(result.getIsMinor()); // Age < 18
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void createCustomerRelationDetails_NullLoyaltyPoints_SetsDefaultZero() {
//        validDto.setLoyaltyPoints(null);
//        entity.setLoyaltyPoints(0);
//
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);
//
//        assertNotNull(result);
//        assertEquals(0, result.getLoyaltyPoints());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    // Tests for getCustomerRelationDetailsById
//    @Test
//    void getCustomerRelationDetailsById_ValidId_ReturnsDto() {
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.getCustomerRelationDetailsById(customerId);
//
//        assertNotNull(result);
//        assertEquals(customerId, result.getCustomerId());
//        assertEquals("normal", result.getCustomerListType());
//        verify(customerRelationDetailsRepository, times(1)).findByCustomerIdAndIsDeleteFalse(customerId);
//    }
//
//    @Test
//    void getCustomerRelationDetailsById_NotFound_ThrowsBusinessException() {
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.empty());
//
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            customerRelationDetailsService.getCustomerRelationDetailsById(customerId);
//        });
//
//        assertEquals("Customer relation details you are looking for is removed/not exists", exception.getMessage());
//        verify(customerRelationDetailsRepository, times(1)).findByCustomerIdAndIsDeleteFalse(customerId);
//    }
//
//    // Tests for deleteMapping
//    @Test
//    void deleteMapping_ValidId_SuccessfullyDeletes() {
//        when(customerRelationDetailsRepository.findById(customerId)).thenReturn(Optional.of(entity));
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        customerRelationDetailsService.deleteMapping(customerId);
//
//        assertTrue(entity.getIsDelete());
//        assertNotNull(entity.getUpdatedAt());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void deleteMapping_NotFound_ThrowsBusinessException() {
//        when(customerRelationDetailsRepository.findById(customerId)).thenReturn(Optional.empty());
//
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            customerRelationDetailsService.deleteMapping(customerId);
//        });
//
//        assertEquals("Customer relation not found with ID: " + customerId, exception.getMessage());
//        verify(customerRelationDetailsRepository, never()).save(any(CustomerRelationDetails.class));
//    }
//
//    // Tests for updateCustomerRelationDetails
//    @Test
//    void updateCustomerRelationDetails_ValidDto_ReturnsUpdatedDto() {
//        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
//        updateDto.setMaritalStatusId(2);
//        updateDto.setGenderId(2);
//        updateDto.setDob(LocalDate.of(1995, 1, 1));
//        updateDto.setAnnualIncome(800000);
//        updateDto.setCustomerListType("blacklisted");
//        updateDto.setCustomerValueScore(70);
//        updateDto.setUpdatedBy(200);
//
//        entity.setMaritalStatusId(2);
//        entity.setGenderId(2);
//        entity.setDob(LocalDate.of(1995, 1, 1));
//        entity.setIsMinor(false);
//        entity.setAnnualIncome(800000);
//        entity.setCustomerListType("blacklisted");
//        entity.setCustomerValueScore(70);
//        entity.setUpdatedBy(200);
//
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
//
//        assertNotNull(result);
//        assertEquals(2, result.getMaritalStatusId());
//        assertEquals(2, result.getGenderId());
//        assertEquals(LocalDate.of(1995, 1, 1), result.getDob());
//        assertFalse(result.getIsMinor());
//        assertEquals(800000, result.getAnnualIncome());
//        assertEquals("blacklisted", result.getCustomerListType());
//        assertEquals(70, result.getCustomerValueScore());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void updateCustomerRelationDetails_NullFields_KeepsExistingValues() {
//        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
//        updateDto.setUpdatedBy(1); // Only update updatedBy
//
//        
//        entity.setMaritalStatusId(2); // <<<<<< important
//        entity.setGenderId(1);
//        entity.setCustomerValueScore(100);
//        entity.setUpdatedBy(1);
//
//
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
//
//        assertNotNull(result);
//        assertEquals(2, result.getMaritalStatusId()); // Should pass now
//        assertEquals(2, result.getGenderId()); // Should pass
//        assertEquals(85890, result.getCustomerValueScore()); // Unchanged
//        assertEquals(1, result.getUpdatedBy());
//        verify(customerRelationDetailsRepository, times(2)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void updateCustomerRelationDetails_NotFound_ThrowsBusinessException() {
//        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.empty());
//
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
//        });
//
//        assertEquals("Customer relation details not found with ID: " + customerId, exception.getMessage());
//        verify(customerRelationDetailsRepository, never()).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void updateCustomerRelationDetails_AnnualIncomeChange_UpdatesCustomerValueScore() {
//        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
//        updateDto.setAnnualIncome(1500000); // > 10L, should set score to 90
//        updateDto.setUpdatedBy(200);
//
//        entity.setAnnualIncome(1500000);
//        entity.setCustomerValueScore(90);
//
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
//
//        assertNotNull(result);
//        assertEquals(1500000, result.getAnnualIncome());
//        assertEquals(90, result.getCustomerValueScore());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//    }
//
//    @Test
//    void updateCustomerRelationDetails_Blacklisted_TriggersAlert() {
//        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
//        updateDto.setCustomerListType("blacklisted");
//        updateDto.setUpdatedBy(200);
//
//        entity.setCustomerListType("blacklisted");
//
//        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
//        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);
//
//        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
//
//        assertNotNull(result);
//        assertEquals("blacklisted", result.getCustomerListType());
//        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
//        // Note: The blacklisted alert is a System.out.println, which is not easily testable.
//        // Consider replacing with a logging framework for better testability.
//    }
//}

package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationDetailsDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerRelationDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerRelationDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRelationDetailsServiceTest {

    @Mock
    private CustomerRelationDetailsRepository customerRelationDetailsRepository;

    @InjectMocks
    private CustomerRelationDetailsService customerRelationDetailsService;

    private CustomerRelationDetailsDto validDto;
    private CustomerRelationDetails entity;
    private Integer customerId;

    @BeforeEach
    void setUp() {
        customerId = 1;

        validDto = new CustomerRelationDetailsDto();
        validDto.setCustomerId(customerId);
        validDto.setMaritalStatusId(1);
        validDto.setGenderId(1);
        validDto.setDob(LocalDate.of(2000, 1, 1));
        validDto.setFatherName("John Doe");
        validDto.setMotherName("Jane Doe");
        validDto.setAnnualIncome(1200000);
        validDto.setCustomerListType("normal");
        validDto.setCustomerValueScore(90);
        validDto.setLoyaltyPoints(100);
        validDto.setCreatedBy(100);
        validDto.setUpdatedBy(100);

        entity = new CustomerRelationDetails();
        entity.setCustomerId(customerId);
        entity.setMaritalStatusId(1);
        entity.setGenderId(1);
        entity.setDob(LocalDate.of(2000, 1, 1));
        entity.setIsMinor(false);
        entity.setFatherName("John Doe");
        entity.setMotherName("Jane Doe");
        entity.setAnnualIncome(1200000);
        entity.setCustomerListType("normal");
        entity.setCustomerValueScore(90);
        entity.setLoyaltyPoints(100);
        entity.setIsDelete(false);
        entity.setCreatedBy(100);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedBy(100);
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createCustomerRelationDetails_ValidDto_ReturnsDto() {
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals("John Doe", result.getFatherName());
        assertFalse(result.getIsMinor());
        assertEquals(100, result.getLoyaltyPoints());
        assertFalse(result.getIsDelete());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void createCustomerRelationDetails_MinorAge_SetsIsMinorTrue() {
        validDto.setDob(LocalDate.now().minusYears(17));
        entity.setDob(LocalDate.now().minusYears(17));
        entity.setIsMinor(true);

        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);

        assertNotNull(result);
        assertTrue(result.getIsMinor());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void createCustomerRelationDetails_NullLoyaltyPoints_SetsDefaultZero() {
        validDto.setLoyaltyPoints(null);
        entity.setLoyaltyPoints(0);

        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.createCustomerRelationDetails(validDto);

        assertNotNull(result);
        assertEquals(0, result.getLoyaltyPoints());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void getCustomerRelationDetailsById_ValidId_ReturnsDto() {
        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));

        CustomerRelationDetailsDto result = customerRelationDetailsService.getCustomerRelationDetailsById(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals("normal", result.getCustomerListType());
        verify(customerRelationDetailsRepository, times(1)).findByCustomerIdAndIsDeleteFalse(customerId);
    }

    @Test
    void getCustomerRelationDetailsById_NotFound_ThrowsBusinessException() {
        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerRelationDetailsService.getCustomerRelationDetailsById(customerId);
        });

        assertEquals("Customer relation details you are looking for is removed/not exists", exception.getMessage());
        verify(customerRelationDetailsRepository, times(1)).findByCustomerIdAndIsDeleteFalse(customerId);
    }

    @Test
    void deleteMapping_ValidId_SuccessfullyDeletes() {
        when(customerRelationDetailsRepository.findById(customerId)).thenReturn(Optional.of(entity));
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        customerRelationDetailsService.deleteMapping(customerId);

        assertTrue(entity.getIsDelete());
        assertNotNull(entity.getUpdatedAt());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void deleteMapping_NotFound_ThrowsBusinessException() {
        when(customerRelationDetailsRepository.findById(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerRelationDetailsService.deleteMapping(customerId);
        });

        assertEquals("Customer relation not found with ID: " + customerId, exception.getMessage());
        verify(customerRelationDetailsRepository, never()).save(any(CustomerRelationDetails.class));
    }

    @Test
    void updateCustomerRelationDetails_ValidDto_ReturnsUpdatedDto() {
        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
        updateDto.setMaritalStatusId(2);
        updateDto.setGenderId(2);
        updateDto.setDob(LocalDate.of(1995, 1, 1));
        updateDto.setAnnualIncome(800000);
        updateDto.setCustomerListType("blacklisted");
        updateDto.setCustomerValueScore(70);
        updateDto.setUpdatedBy(200);

        entity.setMaritalStatusId(2);
        entity.setGenderId(2);
        entity.setDob(LocalDate.of(1995, 1, 1));
        entity.setIsMinor(false);
        entity.setAnnualIncome(800000);
        entity.setCustomerListType("blacklisted");
        entity.setCustomerValueScore(70);
        entity.setUpdatedBy(200);

        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);

        assertNotNull(result);
        assertEquals(2, result.getMaritalStatusId());
        assertEquals(2, result.getGenderId());
        assertEquals(LocalDate.of(1995, 1, 1), result.getDob());
        assertFalse(result.getIsMinor());
        assertEquals(800000, result.getAnnualIncome());
        assertEquals("blacklisted", result.getCustomerListType());
        assertEquals(70, result.getCustomerValueScore());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void updateCustomerRelationDetails_NullFields_KeepsExistingValues() {
        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
        updateDto.setUpdatedBy(1);

        entity.setMaritalStatusId(2);
        entity.setGenderId(1);
        entity.setCustomerValueScore(100);
        entity.setUpdatedBy(1);

        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);

        assertNotNull(result);
        assertEquals(2, result.getMaritalStatusId());
        assertEquals(1, result.getGenderId());
        assertEquals(100, result.getCustomerValueScore());
        assertEquals(1, result.getUpdatedBy());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void updateCustomerRelationDetails_NotFound_ThrowsBusinessException() {
        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);
        });

        assertEquals("Customer relation details not found with ID: " + customerId, exception.getMessage());
        verify(customerRelationDetailsRepository, never()).save(any(CustomerRelationDetails.class));
    }

    @Test
    void updateCustomerRelationDetails_AnnualIncomeChange_UpdatesCustomerValueScore() {
        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
        updateDto.setAnnualIncome(1500000);
        updateDto.setUpdatedBy(200);

        entity.setAnnualIncome(1500000);
        entity.setCustomerValueScore(90);

        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);

        assertNotNull(result);
        assertEquals(1500000, result.getAnnualIncome());
        assertEquals(90, result.getCustomerValueScore());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }

    @Test
    void updateCustomerRelationDetails_Blacklisted_TriggersAlert() {
        CustomerRelationDetailsDto updateDto = new CustomerRelationDetailsDto();
        updateDto.setCustomerListType("blacklisted");
        updateDto.setUpdatedBy(200);

        entity.setCustomerListType("blacklisted");

        when(customerRelationDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)).thenReturn(Optional.of(entity));
        when(customerRelationDetailsRepository.save(any(CustomerRelationDetails.class))).thenReturn(entity);

        CustomerRelationDetailsDto result = customerRelationDetailsService.updateCustomerRelationDetails(customerId, updateDto);

        assertNotNull(result);
        assertEquals("blacklisted", result.getCustomerListType());
        verify(customerRelationDetailsRepository, times(1)).save(any(CustomerRelationDetails.class));
    }
}