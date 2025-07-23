package com.incede.nbfc.customer_management.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerAddressesDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerAddressesService;


@RestController
@RequestMapping("")
public class CustomerAddressesController {
	
	private final CustomerAddressesService customerAddressService;
	
	public CustomerAddressesController(CustomerAddressesService customerAddressService) {
		this.customerAddressService=customerAddressService;
	}

	
	@PostMapping("/customers/{customerId}/addresses")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerAddress(
			@PathVariable Integer customerId ,
			@RequestBody CustomerAddressesDto customerAddressDetails){
		Integer customerAccount = customerAddressService.createCustomerAddressDetails(customerId,customerAddressDetails);
	    return ResponseEntity.ok(ResponseWrapper.created( customerAccount,"Address details created successfully"));

	}
	
	@PutMapping("/customers/{customerId}/addresses/{addressId}")
	public ResponseEntity<ResponseWrapper<Integer>> updateCustomerAddress(
			@PathVariable Integer customerId ,
			@PathVariable Integer addressId,
			@RequestBody CustomerAddressesDto customerAddressDetails){
		Integer customerAccount = customerAddressService.updatedCustomerAddressDetails(customerId,addressId,customerAddressDetails);
	    return ResponseEntity.ok(ResponseWrapper.success(customerAccount,"Address details updated successfully"));

	}
	
	@PatchMapping("/customers/{customerId}/addresses/{addressId}")
	public ResponseEntity<ResponseWrapper<String>> deleteCustomerAddress(
			@PathVariable Integer customerId ,
			@PathVariable Integer addressId){
		 String customerAccount = customerAddressService.softDeleteCustomerAddressDetails(customerId,addressId);
	    return ResponseEntity.ok(ResponseWrapper.success(customerAccount,"Address details Deleted successfully"));

	}
	
	@GetMapping("/customers/{customerId}/addresses/{addressId}")
	public ResponseEntity<ResponseWrapper<CustomerAddressesDto>> getCustomerAddressDetails( @PathVariable Integer customerId, @PathVariable Integer addressId){
		CustomerAddressesDto resultDto = customerAddressService.getCustomerAddressDetailsByCustomerIdAndAddressId(customerId, addressId);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"Address details retrived successfully")); 
	}
	
	
	@GetMapping("/customers/{customerId}/addresses")
	public ResponseEntity<ResponseWrapper<Page<CustomerAddressesDto>>> getCustomerAddress(
			@PathVariable Integer customerId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Page<CustomerAddressesDto> addresspage = customerAddressService.getAllCustomerAddreddDetails(customerId, page, size);
	    return ResponseEntity.ok(ResponseWrapper.success(addresspage, "Fetched address list successfully"));

		
	}
		
	
	
	
}
