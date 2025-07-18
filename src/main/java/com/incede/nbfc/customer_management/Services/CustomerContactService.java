package com.incede.nbfc.customer_management.Services;

import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerContactDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerContact;
import com.incede.nbfc.customer_management.Repositories.CustomerContactRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerContactService {

    private final CustomerContactRepository customerContactRepository;

    public CustomerContactService(CustomerContactRepository customerContactRepository) {
        this.customerContactRepository = customerContactRepository;
    }

    // 1. Add contact
    public CustomerContactDto addContact(Integer customerId, CustomerContactDto dto) {
        validateContactDto(dto); 

        CustomerContact entity = convertToEntity(dto);
        entity.setCustomerId(customerId);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIdentity(UUID.randomUUID());
        entity.setIsVerified(false);
        entity.setIsDelete(false);
        entity.setIsActive(true);
        
        if (Boolean.TRUE.equals(dto.getIsPrimary())) {
            boolean primaryExists = customerContactRepository
                .existsByCustomerIdAndContactTypeAndIsPrimaryTrueAndIsDeleteFalse(customerId, dto.getContactType());
            if (primaryExists) {
                throw new IllegalArgumentException("Primary contact already exists for this type");
            }
        }


        CustomerContact saved = customerContactRepository.save(entity);
        return convertToDto(saved);
    }



    // 2. Update contact
    public CustomerContactDto  updateContact(Integer customerId, Integer contactId, CustomerContactDto dto) {
    	if (dto.getUpdatedBy() == null) {
            throw new BusinessException("UpdatedBy is required.");
        }
        CustomerContact existing = customerContactRepository.findById(contactId)
                .orElseThrow(() -> new BusinessException("Contact not found with ID: " + contactId));

        if (!existing.getCustomerId().equals(customerId)) {
            throw new BusinessException("Customer ID mismatch.");
        }
        existing.setContactType(dto.getContactType());
        existing.setContactValue(dto.getContactValue());
        existing.setIsPrimary(dto.getIsPrimary());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setUpdatedAt(LocalDateTime.now());
        CustomerContact updated = customerContactRepository.save(existing);
        return convertToDto(updated);
    }


    // 3. Soft delete contact
    public void softDeleteContact(Integer customerId, Integer contactId, Integer updatedBy) {
        CustomerContact existing = customerContactRepository.findById(contactId)
                .orElseThrow(() -> new BusinessException("Contact not found with ID: " + contactId));

        if (!existing.getCustomerId().equals(customerId)) {
            throw new BusinessException("Customer ID mismatch.");
        }
        existing.setIsDelete(true);
        existing.setIsActive(false);
        existing.setUpdatedBy(updatedBy);
        existing.setUpdatedAt(LocalDateTime.now());

        customerContactRepository.save(existing);
    }

    // 4. List active contacts for a customer
    public List<CustomerContactDto> getActiveContacts(Integer customerId) {
        List<CustomerContact> contacts = customerContactRepository.findByCustomerIdAndIsActiveTrueAndIsDeleteFalse(customerId);
        if (contacts.isEmpty()) {
            throw new BusinessException("No active contacts found for customer ID: " + customerId);
        }
        return contacts.stream()
                       .map(this::convertToDto)
                       .collect(Collectors.toList());
    }
    // 5. Get contact by ID
    public CustomerContactDto getContactById(Integer customerId, Integer contactId) {
        CustomerContact contact = customerContactRepository.findByContactIdAndCustomerIdAndIsDeleteFalse(contactId, customerId)
                .orElseThrow(() -> new BusinessException("Active contact not found with ID: " + contactId));

        return convertToDto(contact);
    }


    // -------- Conversion Methods --------
    private CustomerContactDto convertToDto(CustomerContact entity) {
        CustomerContactDto dto = new CustomerContactDto();
        dto.setContactId(entity.getContactId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setContactType(entity.getContactType());
        dto.setContactValue(entity.getContactValue());
        dto.setIsVerified(entity.getIsVerified());
        dto.setIsPrimary(entity.getIsPrimary());
        dto.setIsActive(entity.getIsActive());
        dto.setIsDel(entity.getIsDelete());
        dto.setIdentity(entity.getIdentity());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private CustomerContact convertToEntity(CustomerContactDto dto) {
        CustomerContact entity = new CustomerContact();
        entity.setContactId(dto.getContactId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setContactType(dto.getContactType());
        entity.setContactValue(dto.getContactValue());
        entity.setIsVerified(dto.getIsVerified());
        entity.setIsPrimary(dto.getIsPrimary());
        entity.setIsActive(dto.getIsActive());
        entity.setIsDelete(dto.getIsDel());
        entity.setIdentity(dto.getIdentity());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
    
    
    
    
    private void validateContactDto(CustomerContactDto dto) {
        if (dto.getContactType() == null || dto.getContactType() < 1 || dto.getContactType() > 4) {
            throw new IllegalArgumentException("Contact type must be between 1 (Mobile) and 4 (Email)");
        }

        if (dto.getContactValue() == null || dto.getContactValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact value is required");
        }

        // Validate contact value format based on type
        String contactValue = dto.getContactValue().trim();
        switch (dto.getContactType()) {
            case 1: // Mobile
            case 2: // WhatsApp
                if (!contactValue.matches("^[6-9][0-9]{9}$")) {
                    throw new IllegalArgumentException("Invalid mobile/WhatsApp number format");
                }
                break;
            case 4: // Email
                if (!contactValue.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                    throw new IllegalArgumentException("Invalid email format");
                }
                break;
            // Optional: landline validation for type 3 if needed
        }

        if (dto.getCreatedBy() == null) {
            throw new IllegalArgumentException("CreatedBy is required");
        }

        // Check uniqueness of contact_value across the system
        if (customerContactRepository.existsByContactValueIgnoreCase(contactValue)) {
            throw new IllegalArgumentException("Contact value already exists in the system");
        }
    }


}
