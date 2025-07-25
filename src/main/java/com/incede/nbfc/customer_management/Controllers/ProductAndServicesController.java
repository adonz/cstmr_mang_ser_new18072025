package com.incede.nbfc.customer_management.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.LeadMasterDto;
import com.incede.nbfc.customer_management.DTOs.LeadStageMasterManagementDto;
import com.incede.nbfc.customer_management.DTOs.ProductAndServicesDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.ProductAndServicesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/customer")
public class ProductAndServicesController {

	private final ProductAndServicesService productAndServicesService;
	
	public ProductAndServicesController(ProductAndServicesService productAndServicesService) {
		this.productAndServicesService=productAndServicesService;
	}
	
	@PostMapping("/product-service-catalogue/")
	ResponseEntity<ResponseWrapper<Integer>> createCustomerProductAndServiceCatalogue(@Valid @RequestBody ProductAndServicesDto productServiceDto){
		Integer productServiceID = productAndServicesService.createProductAndServicesCatalogue(productServiceDto);
		return ResponseEntity.ok(ResponseWrapper.created(productServiceID,"Customer product and service catalogue Created successfully"));
	}
	
	@PutMapping("/product-service-catalogue/update/")
	ResponseEntity<ResponseWrapper<Integer>> updateCustomerProductAndServiceCatalogue(@RequestBody ProductAndServicesDto productServiceDto){
		Integer productServiceID = productAndServicesService.updateProductAndServicesCatalogue(productServiceDto);
		return ResponseEntity.ok(ResponseWrapper.success(productServiceID,"Customer Lead master Updated successfully"));
	}
	
	@GetMapping("/product-service-catalogue/{productId}")
	ResponseEntity<ResponseWrapper<ProductAndServicesDto>> getCustomerLeadMaterDetailsById(@PathVariable Integer productId){
		ProductAndServicesDto productService = productAndServicesService.getProductAndServiceCatalogue(productId);
		return ResponseEntity.ok(ResponseWrapper.success(productService,"Customer Lead master details fetched successfully"));
	}
	
    @DeleteMapping("/product-service-catalogue/soft-delete/{productId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteLeasStageMaster(
            @PathVariable Integer productId,
            @RequestParam Integer  updatedBy) {

    	String result = productAndServicesService.softDeleteProductAndService(productId, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(result,"Leas Stage Master soft-deleted successfully."));
    }
    
	@GetMapping("/product-service-catalogue/active-accounts")
	public ResponseEntity<ResponseWrapper< Page<ProductAndServicesDto>>> getAllActiveCustomerProductAndServiceCatalogue(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<ProductAndServicesDto>  leadMasterDetails = productAndServicesService.getAllProductAndServiceCatalogue(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(leadMasterDetails));
	}
	
}
