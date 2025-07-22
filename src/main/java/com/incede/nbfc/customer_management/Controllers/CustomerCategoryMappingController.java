package com.incede.nbfc.customer_management.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerCategoryMappingDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerCategoryMappingService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/v1/customer")
public class CustomerCategoryMappingController {

	private final CustomerCategoryMappingService categoryMappingService;
	
	public CustomerCategoryMappingController(CustomerCategoryMappingService categoryMappingService) {
		this.categoryMappingService=categoryMappingService;
	}

	@PostMapping("/customer-category-mappings/")
	public ResponseEntity<ResponseWrapper<String>> createOrUpdateCustomerCategoryMApping(
			@Valid @RequestBody   CustomerCategoryMappingDto  categoryDto){
		    boolean updateId = categoryDto.getCategoryMappingId() !=null;
		    Integer resultDto = categoryMappingService.createOrUpdateCustomerCategoryMapping(categoryDto);
		   ResponseWrapper <String> response = updateId 
				   ? ResponseWrapper.success("ID" + resultDto +"updated successfully")
				    : ResponseWrapper.created("ID" + resultDto +"created successfully");
		   return updateId
		   			? ResponseEntity.ok(response)
	                : ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/customer-category-mappings/get/{categoryMappingId}")
	public ResponseEntity<ResponseWrapper<CustomerCategoryMappingDto>> getCustomerCategoryDetails(
			@PathVariable Integer categoryMappingId){
		CustomerCategoryMappingDto categoryDetails = categoryMappingService.getCustomerCategoryById(categoryMappingId);
		 return ResponseEntity.ok(ResponseWrapper.success(categoryDetails,"customer category details retrived successfully"));
 
	}
	
	@DeleteMapping("/customer-category-mappings/delete")
	public ResponseEntity<ResponseWrapper< String>> deleteCustomerCategoryDetails(
			 @RequestBody CustomerCategoryMappingDto categoryMappingDto){
		String categoryModel = categoryMappingService.SoftdeleteCustomerCategoryDetailsById(categoryMappingDto);
		 return ResponseEntity.ok(ResponseWrapper.success(categoryModel,"customer categort deleted successfully"));
 
	}
	
	@GetMapping("/customer-category-mappings/Active-Accounts")
	public ResponseEntity<ResponseWrapper< Page<CustomerCategoryMappingDto>>> getAllActiveCustomerCategoryInfo(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<CustomerCategoryMappingDto> accountList = categoryMappingService.getAllCustomerCategoryDetails(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(accountList));
			}
}
