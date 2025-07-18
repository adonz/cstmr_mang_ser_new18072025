package com.incede.nbfc.customer_management.Services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.Customer;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TenantConfig tenantConfig;

    @Mock
    private MockOtpService mockOtpService;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto validDto;
    private Customer entity;

    @BeforeEach
    void setUp() {
        validDto = new CustomerDto();
        validDto.setCustomerId(null); // Default to null for create scenarios
        validDto.setTenantId(1);
        validDto.setCustomerCode("CUST001");
        validDto.setSalutationId(1);
        validDto.setFirstName("John");
        validDto.setLastName("Doe");
        validDto.setDisplayName("John Doe");
        validDto.setCustomerMobileno("1234567890");
        validDto.setTaxCatId(1);
        validDto.setCustomerStatus(1);
        validDto.setCrmReferenceId("CRM001");
        validDto.setCreatedBy(1);
        validDto.setUpdatedBy(1);
        validDto.setIsDelete(false);

        entity = new Customer();
        entity.setCustomerId(1);
        entity.setTenantId(1);
        entity.setCustomerCode("CUST001");
        entity.setSalutationId(1);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDisplayName("John Doe");
        entity.setCustomerMobileno("1234567890");
        entity.setTaxCatId(1);
        entity.setCustomerStatus(2);
        entity.setCrmReferenceId("CRM001");
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedBy(1);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIsDelete(false);

        when(tenantConfig.getTenantId()).thenReturn(1);
    }

    @Test
    void createORUpdate_Create_Success() {
        when(customerRepository.existsByCustomerCodeAndTenantIdAndIsDeleteFalse("CUST001", 1)).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        Integer customerId = customerService.createORUpdate(validDto);

        assertNotNull(customerId);
        assertEquals(1, customerId);
        verify(customerRepository).existsByCustomerCodeAndTenantIdAndIsDeleteFalse("CUST001", 1);
        verify(customerRepository).save(any(Customer.class));
        verify(customerRepository, never()).findByCustomerIdAndTenantIdAndIsDeleteFalse(anyInt(), anyInt());
    }

    @Test
    void createORUpdate_Create_DuplicateCode_ThrowsBusinessException() {
        when(customerRepository.existsByCustomerCodeAndTenantIdAndIsDeleteFalse("CUST001", 1)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.createORUpdate(validDto));

        assertEquals("Customer code already exists for this tenant.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createORUpdate_Create_NullCreatedBy_ThrowsBusinessException() {
        validDto.setCreatedBy(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.createORUpdate(validDto));

        assertEquals("createdBy is required when creating a customer.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createORUpdate_Update_Success() {
        validDto.setCustomerId(1); // Simulate update
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        Integer customerId = customerService.createORUpdate(validDto);

        assertNotNull(customerId);
        assertEquals(1, customerId);
        verify(customerRepository).findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createORUpdate_Update_ChangeCustomerCode_ThrowsBusinessException() {
        validDto.setCustomerId(1);
        validDto.setCustomerCode("CUST002");
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.createORUpdate(validDto));

        assertEquals("Customer code cannot be changed", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createORUpdate_Update_NotFound_ThrowsBusinessException() {
        validDto.setCustomerId(1);
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.createORUpdate(validDto));

        assertEquals("Customer not found for update", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void getAllActiveCustomers_Success() {
        when(customerRepository.findAllByTenantIdAndIsDeleteFalse(1)).thenReturn(List.of(entity));

        List<CustomerDto> result = customerService.getAllActiveCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getCustomerId(), result.get(0).getCustomerId());
        verify(customerRepository).findAllByTenantIdAndIsDeleteFalse(1);
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.of(entity));

        CustomerDto result = customerService.getCustomerById(1);

        assertNotNull(result);
        assertEquals(entity.getCustomerId(), result.getCustomerId());
        assertEquals(entity.getCustomerCode(), result.getCustomerCode());
        verify(customerRepository).findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1);
    }

    @Test
    void getCustomerById_NotFound_ThrowsBusinessException() {
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.getCustomerById(1));

        assertEquals("Customer not found or has been deleted.", exception.getMessage());
        verify(customerRepository).findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1);
    }

    @Test
    void softDeleteCustomer_Success() {
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        customerService.softDeleteCustomer(1, 1);

        verify(customerRepository).findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void softDeleteCustomer_NotFound_ThrowsBusinessException() {
        when(customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(1, 1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> customerService.softDeleteCustomer(1, 1));

        assertEquals("Customer not found or already deleted.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }
}