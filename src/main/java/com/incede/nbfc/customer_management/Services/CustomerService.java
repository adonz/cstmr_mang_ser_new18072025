package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.Customer;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CustomerService {

//    private final MockOtpService mockOtpService;

    private final TenantConfig tenantConfig;
    private final CustomerRepository customerRepository;

    public CustomerService(TenantConfig tenantConfig, CustomerRepository customerRepository) {
        this.tenantConfig = tenantConfig;
        this.customerRepository = customerRepository;
//        this.mockOtpService = mockOtpService;
    }

    @Transactional
    public Integer createORUpdate(@Valid CustomerDto customerDto) {
        Integer tenantId = tenantConfig.getTenantId();
        Customer customer;

        if (customerDto.getCustomerId() != null) {
            // UPDATE FLOW
            customer = customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(
                    customerDto.getCustomerId(), tenantId)
                .orElseThrow(() -> new BusinessException("Customer not found for update"));

            // Validate immutable fields
            if (!customer.getCustomerCode().equals(customerDto.getCustomerCode())) {
                throw new BusinessException("Customer code cannot be changed");
            }
            if (!customer.getTenantId().equals(tenantId)) {
                throw new BusinessException("Tenant ID cannot be changed");
            }
            
            //
            
            

            // Update allowed fields
            customer.setSalutationId(customerDto.getSalutationId());
            customer.setFirstName(customerDto.getFirstName());
            customer.setMiddleName(customerDto.getMiddleName());
            customer.setLastName(customerDto.getLastName());
            customer.setDisplayName(customerDto.getDisplayName());
            customer.setTaxCatId(customerDto.getTaxCatId());
            customer.setCustomerStatus(customerDto.getCustomerStatus());
            customer.setUpdatedBy(customerDto.getUpdatedBy() != null
                    ? customerDto.getUpdatedBy()
                    : customerDto.getCreatedBy());
            customer.setUpdatedAt(LocalDateTime.now());

        } else {
            // CREATE FLOW
            if (customerDto.getCreatedBy() == null) {
                throw new BusinessException("createdBy is required when creating a customer.");
            }

//            if (customerDto.getCustomerId() == null) {
//                if (!mockOtpService.isVerified(customerDto.getCustomerMobileno())) {
//                    throw new BusinessException("Mobile number has not been verified via OTP");
//                }
//            }
            boolean codeExists = customerRepository.existsByCustomerCodeAndTenantIdAndIsDeleteFalse(
                    customerDto.getCustomerCode(), tenantId);
            if (codeExists) {
                throw new BusinessException("Customer code already exists for this tenant.");
            }

            customer = new Customer();
            customer.setTenantId(tenantId);
            customer.setCustomerCode(customerDto.getCustomerCode());
            customer.setSalutationId(customerDto.getSalutationId());
            customer.setFirstName(customerDto.getFirstName());
            customer.setMiddleName(customerDto.getMiddleName());
            customer.setLastName(customerDto.getLastName());
            customer.setDisplayName(customerDto.getDisplayName());
            customer.setCustomerMobileno(customerDto.getCustomerMobileno());
            customer.setTaxCatId(customerDto.getTaxCatId());
            customer.setCustomerStatus(customerDto.getCustomerStatus());
            customer.setCrmReferenceId(customerDto.getCrmReferenceId());
            customer.setCreatedBy(customerDto.getCreatedBy());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setIsDelete(false);

            customer.setIdentity(customerDto.getIdentity() != null
                    ? customerDto.getIdentity()
                    : UUID.randomUUID());
        }

        Customer saved = customerRepository.save(customer);
        return saved.getCustomerId();
    }
    
    
    @Transactional
    public List<CustomerDto> getAllActiveCustomers() {
        Integer tenantId = tenantConfig.getTenantId();

        return customerRepository.findAllByTenantIdAndIsDeleteFalse(tenantId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CustomerDto getCustomerById(Integer customerId) {
        Integer tenantId = tenantConfig.getTenantId();

        Customer customer = customerRepository.findByCustomerIdAndTenantIdAndIsDeleteFalse(customerId, tenantId)
                .orElseThrow(() -> new BusinessException("Customer not found or has been deleted."));

        return convertToDto(customer);
    }

    public CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();

        dto.setCustomerId(customer.getCustomerId());
        dto.setTenantId(customer.getTenantId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setSalutationId(customer.getSalutationId());
        dto.setFirstName(customer.getFirstName());
        dto.setMiddleName(customer.getMiddleName());
        dto.setLastName(customer.getLastName());
        dto.setDisplayName(customer.getDisplayName());
        dto.setCustomerMobileno(customer.getCustomerMobileno());
        dto.setTaxCatId(customer.getTaxCatId());
        dto.setCustomerStatus(customer.getCustomerStatus());
        dto.setCrmReferenceId(customer.getCrmReferenceId());
        dto.setIdentity(customer.getIdentity());
        dto.setIsDelete(customer.getIsDelete());
        dto.setCreatedBy(customer.getCreatedBy());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedBy(customer.getUpdatedBy());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }

    public void softDeleteCustomer(Integer customerId, Integer updatedBy) {
        Integer tenantId = tenantConfig.getTenantId();

        Customer customer = customerRepository
                .findByCustomerIdAndTenantIdAndIsDeleteFalse(customerId, tenantId)
                .orElseThrow(() -> new BusinessException("Customer not found or already deleted."));

        customer.setIsDelete(true);
        customer.setUpdatedBy(updatedBy);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }
}
