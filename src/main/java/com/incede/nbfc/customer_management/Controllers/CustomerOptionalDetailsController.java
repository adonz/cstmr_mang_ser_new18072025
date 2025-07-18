package com.incede.nbfc.customer_management.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
