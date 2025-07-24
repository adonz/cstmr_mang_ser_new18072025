package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalReferenceValueDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerAdditionalReferenceValue;
import com.incede.nbfc.customer_management.Repositories.CustomerAdditionalReferenceValueRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerAdditionalReferenceValueServiceTest {
	
	@Mock
	private CustomerAdditionalReferenceValueRepository referenceValueRepository;
	
	@InjectMocks
	private CustomerAdditionalReferenceValueService referenceService;
	
	private CustomerAdditionalReferenceValue referenceModel;
	private CustomerAdditionalReferenceValueDto referenceDto;
	
	@BeforeEach
	void setUp() {
	    referenceDto = new CustomerAdditionalReferenceValueDto();
	    referenceDto.setCustomerId(1001);
	    referenceDto.setCustomerAdditionalReferenceName("PAN");
	    referenceDto.setCustomerAdditionalReferenceValue("ABCDE1234F");
	    referenceDto.setIdentity(UUID.randomUUID());
	    referenceDto.setIsDelete(false);
	    referenceDto.setCreatedAt(LocalDateTime.now());
	    referenceDto.setUpdatedAt(LocalDateTime.now());
	    referenceDto.setCreatedBy(1);
	    referenceDto.setUpdatedBy(2);

	    referenceModel = new CustomerAdditionalReferenceValue();
	    referenceModel.setCustomerId(referenceDto.getCustomerId());
	    referenceModel.setCustomerAdditionalReferenceName(referenceDto.getCustomerAdditionalReferenceName());
	    referenceModel.setCustomerAdditionalReferenceValue(referenceDto.getCustomerAdditionalReferenceValue());
	    referenceModel.setIdentity(referenceDto.getIdentity());
	    referenceModel.setIsDelete(referenceDto.getIsDelete());
	    referenceModel.setCreatedAt(referenceDto.getCreatedAt());
	    referenceModel.setUpdatedAt(referenceDto.getUpdatedAt());
	    referenceModel.setCreatedBy(referenceDto.getCreatedBy());
	    referenceModel.setUpdatedBy(referenceDto.getUpdatedBy());
	}

    @Test
    void saveCustomerAdditionalReference_successful() {
        List<CustomerAdditionalReferenceValueDto> dtoList = List.of(referenceDto);

         when(referenceValueRepository.findByCustomerIdAndCustomerAdditionalReferenceValue(
        		 referenceDto.getCustomerId(), referenceDto.getCustomerAdditionalReferenceValue()))
            .thenReturn(null);

         when(referenceValueRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        int result = referenceService.createCustomerAdditionalInformation(dtoList);
        assertEquals(1, result);

        verify(referenceValueRepository, times(1)).saveAll(anyList());
    }
    
    @Test
    void saveCustomerAdditionalReference_throwsException_whenCustomerIdIsNull() {
    	referenceDto.setCustomerId(null);
        List<CustomerAdditionalReferenceValueDto> dtoList = List.of(referenceDto);

        BusinessException thrown = assertThrows(BusinessException.class, () ->
            referenceService.createCustomerAdditionalInformation(dtoList));

        assertTrue(thrown.getMessage().contains("customer Id cannot be null"));
    }
    
    @Test
    void saveCustomerAdditionalReference_throwsException_whenDuplicateExists() {
        List<CustomerAdditionalReferenceValueDto> dtoList = List.of(referenceDto);

         when(referenceValueRepository.findByCustomerIdAndCustomerAdditionalReferenceValue(
        		referenceDto.getCustomerId(), referenceDto.getCustomerAdditionalReferenceValue()))
            .thenReturn(new CustomerAdditionalReferenceValue());

        BusinessException thrown = assertThrows(BusinessException.class, () ->
            referenceService.createCustomerAdditionalInformation(dtoList));

        assertTrue(thrown.getMessage().contains("duplicate value"));
    }
	

}
