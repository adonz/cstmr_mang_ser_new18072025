package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerBankAccountDto;
import com.incede.nbfc.customer_management.Models.CustomerBankAccountModel;
import com.incede.nbfc.customer_management.Repositories.CustomerBankAccountRepository;
import com.incede.nbfc.customer_management.Services.CustomerBankAccountService;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class CustomerBankAccountServiceTest {

    @Mock
    private CustomerBankAccountRepository repository;

    @InjectMocks
    private CustomerBankAccountService service;

    private CustomerBankAccountModel sampleAccountModel;

    @BeforeEach
    void setUp() {
        sampleAccountModel = new CustomerBankAccountModel();
        sampleAccountModel.setBankAccountId(1);
        sampleAccountModel.setCustomerId(2);
        sampleAccountModel.setBankId(1);
        sampleAccountModel.setBranchName("Njarakkal");
        sampleAccountModel.setAccountNumber("123456780001");
        sampleAccountModel.setUpiId("glodin171@upi");
        sampleAccountModel.setAccountType(2);
        sampleAccountModel.setIsActive(true);
        sampleAccountModel.setIsPrimary(true);
        sampleAccountModel.setIdentity(UUID.randomUUID());
        sampleAccountModel.setCreatedAt(LocalDateTime.now());
        sampleAccountModel.setCreatedBy(1);
        sampleAccountModel.setUpdatedAt(LocalDateTime.now());
        sampleAccountModel.setUpdatedBy(1);
        sampleAccountModel.setIsDelete(false);
    }

    @Test
    void testGetBankAccountById_Success() {
        when(repository.findByBankAccountIdAndIsDeleteFalse(1)).thenReturn(sampleAccountModel);

        CustomerBankAccountDto result = service.getCustomerBankDetailsByBankAccountId(1);

        assertNotNull(result);
        assertEquals("123456780001", result.getAccountNumber());

        verify(repository, times(1)).findByBankAccountIdAndIsDeleteFalse(1);
    }
}
