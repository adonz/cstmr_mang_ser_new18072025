package com.incede.nbfc.customer_management.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.incede.nbfc.customer_management.DTOs.CustomerContactDto;
import com.incede.nbfc.customer_management.Models.CustomerContact;
import com.incede.nbfc.customer_management.Repositories.CustomerContactRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
public class CustomerContactServiceTest {

    @InjectMocks
    private CustomerContactService customerContactService;

    @Mock
    private CustomerContactRepository customerContactRepository;

    private CustomerContactDto mockDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockDto = new CustomerContactDto();
        mockDto.setContactType(1);
        mockDto.setContactValue("9876543210");
        mockDto.setCustomerId(101);
        mockDto.setCreatedBy(1);
        mockDto.setIsPrimary(true);
        mockDto.setIsVerified(false);
        mockDto.setIsActive(true);
        mockDto.setIdentity(UUID.randomUUID());
    }
    @Test
    void testAddContactReturnsValidContactDto() {
        CustomerContact savedEntity = new CustomerContact();
        savedEntity.setContactId(123);

        when(customerContactRepository.save(any(CustomerContact.class))).thenReturn(savedEntity);

        CustomerContactDto resultDto = customerContactService.addContact(mockDto.getCustomerId(), mockDto);

        assertNotNull(resultDto);
        assertEquals(123, resultDto.getContactId());
        verify(customerContactRepository, times(1)).save(any(CustomerContact.class));
    }

}
