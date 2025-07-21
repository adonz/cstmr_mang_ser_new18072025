package com.incede.nbfc.customer_management.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerOptionalDetailsService;


@RestController
@RequestMapping("/v1/customer")
public class CustomerOptionalDetailsController {
	
	private final CustomerOptionalDetailsService optionalDetailsService;
	public CustomerOptionalDetailsController(CustomerOptionalDetailsService optionalDetailsService) {
		this.optionalDetailsService = optionalDetailsService;
	}
	@PostMapping("/customer-optional-details/")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerOptionalDetails(
			@RequestBody  CustomerOptionalDetailsDto customerOptionalDetails){
		Integer customerOptional = optionalDetailsService.createCustomerOptionalDetails(customerOptionalDetails);
	    return ResponseEntity.ok(ResponseWrapper.created( customerOptional,"Address details created successfully"));

	}
	
	@GetMapping("/customer-optional-details/get/{customerID}")
	public ResponseEntity<ResponseWrapper<CustomerOptionalDetailsDto>> getCustomerOptionalDetails(
			 @PathVariable  Integer customerID){
		CustomerOptionalDetailsDto customerOptional = optionalDetailsService.getCustomerOptionalDetailsById(customerID);
	    return ResponseEntity.ok(ResponseWrapper.created( customerOptional,"Address details retrived successfully"));

	}
	
	@GetMapping("/customer-optional-details/get/Active-Accounts")
	public ResponseEntity<ResponseWrapper< Page<CustomerOptionalDetailsDto>>> getAllActiveCustomersOptionalDetails(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<CustomerOptionalDetailsDto> accountList = optionalDetailsService.getAllCustomerOptionalDetails(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(accountList));
			}
	
	@PostMapping("/customer-optional-details/delete")
	public ResponseEntity<ResponseWrapper< String>> deleteCustomerOptionalDetailsByAccountId(@RequestBody CustomerOptionalDetailsDto customerOptionalDetails){
		 String resultDto = optionalDetailsService.softDeleteCustomerOptionalDetails(customerOptionalDetails);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto));
	}
	
	@PostMapping("/customer-optional-details/update")
	public ResponseEntity<ResponseWrapper<Integer>> UpdateCustomerOptionalDetails(
			 @RequestBody  CustomerOptionalDetailsDto customerOptionalDetails){
		Integer updatecustomerOptional = optionalDetailsService.updateCustomerOptionalDetails(customerOptionalDetails);
	    return ResponseEntity.ok(ResponseWrapper.created( updatecustomerOptional,"Address details created successfully"));

	}
}
