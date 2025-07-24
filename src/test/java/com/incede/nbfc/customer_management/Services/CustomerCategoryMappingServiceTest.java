package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerCategoryMappingDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerCategoryMappingModel;
import com.incede.nbfc.customer_management.Repositories.CustomerCategoryMappingRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerCategoryMappingServiceTest {

	@Mock
	private CustomerCategoryMappingRepository categoryRepository;
	
	@InjectMocks
	private CustomerCategoryMappingService categoryService;
	
	private CustomerCategoryMappingDto customerCategoryDto;
	
	private CustomerCategoryMappingModel customerCategoryModel;
	
	
    @BeforeEach
    void setUp() {
        customerCategoryDto = new CustomerCategoryMappingDto();
        customerCategoryDto.setCustomerId(1001);
        customerCategoryDto.setCategoryId(2002);
        customerCategoryDto.setAssignedDate(LocalDateTime.now().minusDays(1));
        customerCategoryDto.setIsActive(true);
        customerCategoryDto.setIdentity(UUID.randomUUID());
        customerCategoryDto.setIsDelete(false);
        customerCategoryDto.setCreatedAt(LocalDateTime.now().minusDays(1));
        customerCategoryDto.setUpdatedAt(LocalDateTime.now());
        customerCategoryDto.setCreatedBy(101);
        customerCategoryDto.setUpdatedBy(102);

        customerCategoryModel = new CustomerCategoryMappingModel();
        customerCategoryModel.setCustomerId(customerCategoryDto.getCustomerId());
        customerCategoryModel.setCategoryId(customerCategoryDto.getCategoryId());
        customerCategoryModel.setAssignedDate(customerCategoryDto.getAssignedDate());
        customerCategoryModel.setIsActive(customerCategoryDto.getIsActive());
        customerCategoryModel.setIdentity(customerCategoryDto.getIdentity());
        customerCategoryModel.setIsDelete(customerCategoryDto.getIsDelete());
        customerCategoryModel.setCreatedAt(customerCategoryDto.getCreatedAt());
        customerCategoryModel.setUpdatedAt(customerCategoryDto.getUpdatedAt());
        customerCategoryModel.setCreatedBy(customerCategoryDto.getCreatedBy());
        customerCategoryModel.setUpdatedBy(customerCategoryDto.getUpdatedBy());
    }
    
    @Test
    void getCustomerCategoryById_validId_shouldReturnDto() {
         when(categoryRepository.findByCategoryMappingIdAndIsDeleteFalse(1))
               .thenReturn(customerCategoryModel);
        CustomerCategoryMappingDto result = categoryService.getCustomerCategoryById(1);
        assertNotNull(result);
         assertEquals(customerCategoryDto.getCustomerId(), result.getCustomerId());
         assertEquals(customerCategoryDto.getCategoryId(), result.getCategoryId());
    }
    
    @Test
    void getCustomerCategoryById_invalidId_shouldThrowException() {
         when(categoryRepository.findByCategoryMappingIdAndIsDeleteFalse(99))
               .thenReturn(null);
         BusinessException thrown =  assertThrows(BusinessException.class, () -> {
        	 categoryService.getCustomerCategoryById(99);
        });

         assertTrue(thrown.getMessage().contains("Details not founf for id:99"));
    }
   
    
}
