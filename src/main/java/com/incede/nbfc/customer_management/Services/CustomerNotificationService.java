package com.incede.nbfc.customer_management.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerNotificationDTO;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerNotification;
import com.incede.nbfc.customer_management.Repositories.CustomerNotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerNotificationService {

    @Autowired
    private CustomerNotificationRepository notificationRepository;

    @Transactional
    public CustomerNotificationDTO recordConsent(CustomerNotificationDTO dto) {
        // Default all consents to FALSE if not set
        Boolean sms = dto.getConsentSms() != null ? dto.getConsentSms() : false;
        Boolean email = dto.getConsentEmail() != null ? dto.getConsentEmail() : false;
        Boolean whatsapp = dto.getConsentWhatsapp() != null ? dto.getConsentWhatsapp() : false;

        CustomerNotification entity = new CustomerNotification();
        entity.setCustomerId(dto.getCustomerId());
        entity.setConsentSms(sms);
        entity.setConsentEmail(email);
        entity.setConsentWhatsapp(whatsapp);
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setIdentity(UUID.randomUUID());

        CustomerNotification saved = notificationRepository.save(entity);
        return toDTO(saved);
    }
    
    @Transactional
    public void updateConsent(Integer customerId, CustomerNotificationDTO dto) {
        CustomerNotification existing = notificationRepository.findByCustomerIdAndIsDeleteFalse(customerId)
            .orElseThrow(() -> new DataNotFoundException("Notification preferences not found or customer is inactive."));

        // Update preferences
        existing.setConsentSms(dto.getConsentSms() != null ? dto.getConsentSms() : false);
        existing.setConsentEmail(dto.getConsentEmail() != null ? dto.getConsentEmail() : false);
        existing.setConsentWhatsapp(dto.getConsentWhatsapp() != null ? dto.getConsentWhatsapp() : false);

        existing.setUpdatedBy(dto.getUpdatedBy());

        notificationRepository.save(existing);
    }

    @Transactional
    public void softDeleteConsent(Integer customerId, Integer userId) {
        CustomerNotification existing = notificationRepository.findByCustomerIdAndIsDeleteFalse(customerId)
            .orElseThrow(() -> new DataNotFoundException("Notification preferences not found or already deleted."));

        existing.setIsDelete(true); // ❌ Logically deactivate
        existing.setUpdatedBy(userId);

        notificationRepository.save(existing);
    }


    private CustomerNotificationDTO toDTO(CustomerNotification entity) {
        CustomerNotificationDTO dto = new CustomerNotificationDTO();
        dto.setNotificationId(entity.getNotificationId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setConsentSms(entity.getConsentSms());
        dto.setConsentEmail(entity.getConsentEmail());
        dto.setConsentWhatsapp(entity.getConsentWhatsapp());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}

