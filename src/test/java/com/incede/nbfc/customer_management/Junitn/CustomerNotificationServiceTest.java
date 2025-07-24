package com.incede.nbfc.customer_management.Junitn;

import com.incede.nbfc.customer_management.DTOs.CustomerNotificationDTO;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerNotification;
import com.incede.nbfc.customer_management.Repositories.CustomerNotificationRepository;
import com.incede.nbfc.customer_management.Services.CustomerNotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CustomerNotificationServiceTest {

    @Mock private CustomerNotificationRepository repository;

    @InjectMocks private CustomerNotificationService service;

    private CustomerNotificationDTO dto;
    private CustomerNotification entity;

    @BeforeEach
    void setUp() {
        dto = new CustomerNotificationDTO();
        dto.setCustomerId(1001);
        dto.setConsentSms(true);
        dto.setConsentEmail(null); // should default to false
        dto.setConsentWhatsapp(true);
        dto.setCreatedBy(999);
        dto.setUpdatedBy(888);

        entity = new CustomerNotification();
        entity.setNotificationId(1);
        entity.setCustomerId(1001);
        entity.setConsentSms(true);
        entity.setConsentEmail(false);
        entity.setConsentWhatsapp(true);
        entity.setCreatedBy(999);
        entity.setUpdatedBy(888);
        entity.setIdentity(UUID.randomUUID());
        entity.setIsDelete(false);
    }

    @Test
    void testRecordConsentSuccess() {
        when(repository.save(any())).thenReturn(entity);

        CustomerNotificationDTO result = service.recordConsent(dto);

        assertNotNull(result);
        assertEquals(1001, result.getCustomerId());
        assertEquals(false, result.getConsentEmail()); // defaulted
        verify(repository).save(any());
    }

    @Test
    void testUpdateConsentSuccess() {
        when(repository.findByCustomerIdAndIsDeleteFalse(1001)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        dto.setConsentEmail(true);
        service.updateConsent(1001, dto);

        assertEquals(true, entity.getConsentEmail());
        assertEquals(true, entity.getConsentSms());
        assertEquals(true, entity.getConsentWhatsapp());
        assertEquals(888, entity.getUpdatedBy());
        verify(repository).save(entity);
    }

    @Test
    void testUpdateConsentNotFound() {
        when(repository.findByCustomerIdAndIsDeleteFalse(999)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.updateConsent(999, dto));
    }

    @Test
    void testSoftDeleteConsentSuccess() {
        when(repository.findByCustomerIdAndIsDeleteFalse(1001)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        service.softDeleteConsent(1001, 777);

        assertTrue(entity.getIsDelete());
        assertEquals(777, entity.getUpdatedBy());
        verify(repository).save(entity);
    }

    @Test
    void testSoftDeleteConsentNotFound() {
        when(repository.findByCustomerIdAndIsDeleteFalse(777)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.softDeleteConsent(777, 123));
    }
}

