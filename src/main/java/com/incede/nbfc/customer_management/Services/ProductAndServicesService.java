package com.incede.nbfc.customer_management.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.ProductAndServicesDto;
import com.incede.nbfc.customer_management.Enums.ProductServicesTypes;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Models.LeadStageMasterManagement;
import com.incede.nbfc.customer_management.Models.ProductAndServicesModel;
import com.incede.nbfc.customer_management.Repositories.ProductAndServicesRepository;

@Service
public class ProductAndServicesService {

	private final ProductAndServicesRepository productAndServiceRepository;
	
	public ProductAndServicesService(ProductAndServicesRepository productAndServiceRepository) {
		this.productAndServiceRepository=productAndServiceRepository;
	}

	@Transactional
	public Integer createProductAndServicesCatalogue( ProductAndServicesDto productDto) {
		try {
			ProductAndServicesModel ver_psName = productAndServiceRepository.findByTenentIdAndProductAndServiceNameAndIsDeleteFalse(
					productDto.getTenantId(),productDto.getProductServiceName());
			
			ProductAndServicesModel ver_psCode = productAndServiceRepository.findByTenantIdAndProductAndServiceCodeAndIsDeleteFalse(
					productDto.getTenantId(),productDto.getProductServiceCode());
			if(productDto.getCreatedBy() ==null) {
				throw new BadRequestException("created by should not be null");
			}
			 if(ver_psName ==null) {
				 throw new ConflictException("product service name already exists in database");

			 }
			 if(ver_psCode ==null) {
				 throw new ConflictException("product service code (ps_code) already exists in database");

			 }
			ProductServicesTypes ver_productService;
					try {
						  ver_productService = ProductServicesTypes.valueOf(productDto.getProductServiceType().toUpperCase());

					}catch(BadRequestException e){
						throw  new BusinessException("ProductServicesTypes should be in PRODUCT or SERVICE"+e);
					}	
			 
			 
			 ProductAndServicesModel newProductService = new ProductAndServicesModel();
			 newProductService.setIsActive(true);
			 newProductService.setCreatedBy(productDto.getCreatedBy());
			 newProductService.setDescription(productDto.getDescription());
			 newProductService.setProductServiceCode(productDto.getProductServiceCode());
			 newProductService.setProductServiceName(productDto.getProductServiceName());
			 newProductService.setProductServiceType(ver_productService);
			 newProductService.setTenantId(productDto.getTenantId());
		
			 ProductAndServicesModel result = productAndServiceRepository.save(newProductService);
			 
			 return convertToDtoProductService(result).getTenantId();
		}
		catch(BusinessException e) {
			throw e;
		}
	}


	@Transactional
	public Integer updateProductAndServicesCatalogue(ProductAndServicesDto productDto) {
		try {
			if(productDto.getCreatedBy() == null) throw new BadRequestException("createBy should not be null");
			if(productDto.getProductServiceId() ==null) throw new BadRequestException("Tenent id should not be null for updating");

			if(productDto.getProductServiceCode() !=null) {
				ProductAndServicesModel existingPscode = productAndServiceRepository.findByProductAndServiceCodeAndIsDeleteFalse(productDto.getProductServiceCode());
				if(existingPscode !=null)
					throw new BadRequestException("product service code already exists");
			}
			if(productDto.getProductServiceName() !=null) {
				ProductAndServicesModel existingPsname = productAndServiceRepository.findByProductAndServiceNameAndIsDeleteFalse(productDto.getProductServiceCode());
				if(existingPsname !=null) {
					throw new BadRequestException("product service name already exists");
				}
			}
			ProductServicesTypes ver_productService;
			try {
				  ver_productService = ProductServicesTypes.valueOf(productDto.getProductServiceType().toUpperCase());

			}catch(BadRequestException e){
				throw  new BusinessException("ProductServicesTypes should be in PRODUCT or SERVICE"+e);
			}
			ProductAndServicesModel existingModel = productAndServiceRepository.findByProductServiceIdAndIsDeletedFalse(productDto.getProductServiceId());

			if(existingModel == null) {
				existingModel.setProductServiceName(productDto.getProductServiceName() !=null?productDto.getProductServiceName():existingModel.getProductServiceName());
				existingModel.setProductServiceCode(productDto.getProductServiceCode() !=null ? productDto.getProductServiceCode() :existingModel.getProductServiceCode());
				existingModel.setProductServiceType(ver_productService);
				existingModel.setDescription(productDto.getDescription() !=null ? productDto.getDescription() : existingModel.getDescription());
				existingModel.setIsActive(productDto.getIsActive() !=null ? productDto.getIsActive() :existingModel.getIsActive());
				 
				ProductAndServicesModel updateModel = productAndServiceRepository.save(existingModel);
				return convertToDtoProductService(updateModel).getProductServiceId();
			}else {
				throw new BusinessException("Data not found for id"+productDto.getProductServiceId());
			}
		}
		catch(BusinessException e) {
 		 throw e;
		}
	}

	@Transactional
	public ProductAndServicesDto getProductAndServiceCatalogue(Integer productServiceId) {
 		 try {
 			ProductAndServicesModel productServiceCatalogue = productAndServiceRepository.findByProductServiceIdAndIsDeletedFalse(productServiceId);
 			if(productServiceCatalogue ==null) throw new BusinessException("Data not found for id "+productServiceId);
 			return convertToDtoProductService(productServiceCatalogue);
 		 }
 		 catch(BusinessException e) {
 			 throw e;
 		 }
	}

	@Transactional
	public void softDeleteProductAndService(Integer serviceId, Integer updatedBy) {
		try { 
 			ProductAndServicesModel productServiceCatalogueDelete = productAndServiceRepository.findByProductServiceIdAndIsDeletedFalse(serviceId);
 			if(productServiceCatalogueDelete ==null) throw new BusinessException("Data not found for id "+serviceId);
 			productServiceCatalogueDelete.setIsDelete(true);
 			productServiceCatalogueDelete.setUpdatedBy(updatedBy);
		}
		catch(BusinessException e) {
			throw e;
		}
 		
	}

	@Transactional
	public Page<ProductAndServicesDto> getAllProductAndServiceCatalogue(int page, int size) {
		Pageable pageble = PageRequest.of(page, size);
		Page<ProductAndServicesModel> pageResult = productAndServiceRepository.findByIsDeleteFalse(pageble);
		if(pageResult ==null || pageResult.isEmpty()) return Page.empty(pageble);
		 return pageResult.map(this::convertToDtoProductService);
	}
	
	
	private ProductAndServicesDto  convertToDtoProductService(ProductAndServicesModel result) {
	    ProductAndServicesDto dto = new ProductAndServicesDto();
	    dto.setTenantId(result.getTenantId());
	    dto.setProductServiceName(result.getProductServiceName());
	    dto.setProductServiceCode(result.getProductServiceCode());
	    dto.setDescription(result.getDescription());
	    dto.setIsActive(result.getIsActive());
	    dto.setIsDelete(result.getIsDelete());
	    dto.setCreatedBy(result.getCreatedBy());
	    dto.setUpdatedBy(result.getUpdatedBy());
	    dto.setCreatedAt(result.getCreatedAt());
	    dto.setUpdatedAt(result.getUpdatedAt());
	    
	    return dto;
		
 	}
	
	 
}
