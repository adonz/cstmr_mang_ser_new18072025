package com.incede.nbfc.customer_management.Services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.LeadMasterDto;
import com.incede.nbfc.customer_management.DTOs.ProductAndServicesDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Repositories.ProductAndServicesRepository;

@Service
public class ProductAndServicesService {

	private final ProductAndServicesRepository productAndServiceRepository;
	
	public ProductAndServicesService(ProductAndServicesRepository productAndServiceRepository) {
		this.productAndServiceRepository=productAndServiceRepository;
	}

	@Transactional
	public Integer createProductAndServicesCatalogue( LeadMasterDto leadDto) {
		try {
			
		}
		catch(BusinessException e) {
			throw e;
		}
		return null;
	}

	@Transactional
	public Integer updateProductAndServicesCatalogue(LeadMasterDto leadDto) {
		
 		return null;
	}

	@Transactional
	public ProductAndServicesDto getProductAndServiceCatalogue(Integer leadMasterId) {
 		return null;
	}

	@Transactional
	public void softDeleteProductAndService(Integer serviceId, Integer updatedBy) {
 		
	}

	@Transactional
	public Page<ProductAndServicesDto> getAllProductAndServiceCatalogue(int page, int size) {
 		return null;
	}
}
