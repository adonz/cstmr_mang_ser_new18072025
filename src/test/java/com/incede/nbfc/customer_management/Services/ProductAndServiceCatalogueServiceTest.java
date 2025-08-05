package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.ProductAndServicesDto;
import com.incede.nbfc.customer_management.Enums.ProductServicesTypes;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.ProductAndServicesModel;
import com.incede.nbfc.customer_management.Repositories.ProductAndServicesRepository;

@ExtendWith(MockitoExtension.class)
public class ProductAndServiceCatalogueServiceTest {
	
	@Mock
	private ProductAndServicesRepository productAndServiceRepository;
	@InjectMocks
	private ProductAndServicesService productAndServiceService;
	
	private ProductAndServicesModel productServiceModel;
	private ProductAndServicesDto productServiceDto;
	
	@BeforeEach
	void SetUp(){
	 
			 productServiceDto = new ProductAndServicesDto();
	        productServiceDto.setProductServiceId(1);
	        productServiceDto.setTenantId(1001);
	        productServiceDto.setProductServiceName("Test Product");
	        productServiceDto.setProductServiceCode("TP001");
	        productServiceDto.setProductServiceType("PRODUCT");
	        productServiceDto.setDescription("A sample product for testing.");
	        productServiceDto.setIsActive(true);
	        productServiceDto.setIsDelete(false);
	        productServiceDto.setCreatedBy(1);
	        productServiceDto.setUpdatedBy(1);
	        productServiceDto.setCreatedAt(LocalDateTime.now());
	        productServiceDto.setUpdatedAt(LocalDateTime.now());

	         productServiceModel = new ProductAndServicesModel();
	        productServiceModel.setProductServiceId(1);
	        productServiceModel.setTenantId(1001);
	        productServiceModel.setProductServiceName("Test Product");
	        productServiceModel.setProductServiceCode("TP001");
	        productServiceModel.setProductServiceType(ProductServicesTypes.PRODUCT);
	        productServiceModel.setDescription("A sample product for testing.");
	        productServiceModel.setIsActive(true);
	        productServiceModel.setIsDelete(false);
	        productServiceModel.setCreatedBy(1);
	        productServiceModel.setUpdatedBy(1);
	        productServiceModel.setCreatedAt(LocalDateTime.now());
	        productServiceModel.setUpdatedAt(LocalDateTime.now());
	}
	
	@Test
	void getProductServiceTypeByProductId_success() {
		when(productAndServiceRepository.findByProductServiceIdAndIsDeleteFalse(1)).thenReturn(productServiceModel);
		ProductAndServicesDto productDto = productAndServiceService.getProductAndServiceCatalogue(1);
		assertNotNull(productDto);
		assertEquals(1,productDto.getProductServiceId());
		assertEquals("Test Product",productDto.getProductServiceName());
		assertEquals("PRODUCT", productDto.getProductServiceType());
	}
	
	@Test
	void getProductServiceTypeByProductId_false() {
		when(productAndServiceRepository.findByProductServiceIdAndIsDeleteFalse(1)).thenReturn(null);
		BusinessException exp = assertThrows(BusinessException.class,
				()->{productAndServiceService.getProductAndServiceCatalogue(1);
				});
		assertTrue(exp.getMessage().contains("Data not found for id "+1));
	}
	
	@Test
	void softdeleteProductServiceTypeByProductId_success() {
		when(productAndServiceRepository.findByProductServiceIdAndIsDeleteFalse(1)).thenReturn(productServiceModel);
		 String productDto = productAndServiceService.softDeleteProductAndService(1,1);
		assertEquals("data deleted successfully id:"+1,productDto );


	}
	
	@Test
	void softdeleteProductServiceTypeByProductId_False() {
		when(productAndServiceRepository.findByProductServiceIdAndIsDeleteFalse(1)).thenReturn(null);
		BusinessException exp = assertThrows(BusinessException.class,
				()->{productAndServiceService.softDeleteProductAndService(1,1);
				});
		assertTrue(exp.getMessage().contains("Data not found for id "+1));

	}

	
}
