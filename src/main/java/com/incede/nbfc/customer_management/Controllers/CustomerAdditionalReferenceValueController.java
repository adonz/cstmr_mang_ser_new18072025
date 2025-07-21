package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

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

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalReferenceValueDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerAdditionalReferenceValueService;

@RestController
@RequestMapping("/v1/customer")
public class CustomerAdditionalReferenceValueController {
	
	private final CustomerAdditionalReferenceValueService additionalValueService;
	
	public CustomerAdditionalReferenceValueController(CustomerAdditionalReferenceValueService additionalValueService) {
		this.additionalValueService=additionalValueService;
	}
	
	@PostMapping("/customer-additional-reference-info/")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerAdditionalReferenceInformation(
			@RequestBody List<CustomerAdditionalReferenceValueDto> additionalReferenceDto){
		Integer AdditionalDetails = additionalValueService.createCustomerAdditionalInformation(additionalReferenceDto);
		 return ResponseEntity.ok(ResponseWrapper.created(AdditionalDetails,"customer additional details saved successfully"));
 
	}
	
	@GetMapping("/customer-additional-reference-info/get/{additionalrefId}")
	public ResponseEntity<ResponseWrapper<CustomerAdditionalReferenceValueDto>> getCustomerAdditionalReferenceInformation(
			@PathVariable Integer additionalrefId){
		CustomerAdditionalReferenceValueDto AdditionalDetails = additionalValueService.getCustomerAdditionalReferenceValueById(additionalrefId);
		 return ResponseEntity.ok(ResponseWrapper.success(AdditionalDetails,"customer additional details retrived successfully"));
 
	}
	
	@PutMapping("/customer-additional-reference-info/update")
	public ResponseEntity<ResponseWrapper<CustomerAdditionalReferenceValueDto>> updateCustomerAdditionalReferenceInformation(
			 @RequestBody CustomerAdditionalReferenceValueDto additionalReferenceDto){
		CustomerAdditionalReferenceValueDto AdditionalDetails = additionalValueService.updatedAdditionalReferenceDetails(additionalReferenceDto);
		 return ResponseEntity.ok(ResponseWrapper.success(AdditionalDetails,"customer additional details updated successfully"));
 
	}
	
	@GetMapping("/customer-additional-reference-info/Active-Accounts")
	public ResponseEntity<ResponseWrapper< Page<CustomerAdditionalReferenceValueDto>>> getAllActiveCustomersAdditionalReferenceInfo(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<CustomerAdditionalReferenceValueDto> accountList = additionalValueService.getAllCustomerAdditionalReferenceDetails(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(accountList));
			}
	
	@DeleteMapping("/customer-additional-reference-info/delete")
	public ResponseEntity<ResponseWrapper< String>> deleteCustomerAdditionalReferenceInformation(
			 @RequestBody CustomerAdditionalReferenceValueDto additionalReferenceDto){
		String categoryModel = additionalValueService.SoftdeleteCustomerAdditionalReferenceValue(additionalReferenceDto);
		 return ResponseEntity.ok(ResponseWrapper.success(categoryModel,"customer categort deleted successfully"));
 
	}
	
	

}

