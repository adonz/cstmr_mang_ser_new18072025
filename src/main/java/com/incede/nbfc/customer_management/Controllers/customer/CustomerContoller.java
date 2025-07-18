package com.incede.nbfc.customer_management.Controllers.customer;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "${cors.allowed-origin}")
//@CrossOrigin(origins = "http://192.168.1.65:5173")
@RestController
@RequestMapping("/v1/customer")
public class CustomerContoller {
	
	private final CustomerService customerService;
	
	public CustomerContoller (CustomerService customerService) {
		this.customerService = customerService;
		
	}
	
	@PostMapping("/")
	public ResponseEntity<ResponseWrapper<Integer>> createORUpdateCustomer(@Valid @RequestBody CustomerDto customerDto)
	{
		Integer customerId =customerService.createORUpdate(customerDto);
		Boolean isUpdate = customerDto.getCustomerId()!= null;
		return isUpdate
				?ResponseEntity.ok(ResponseWrapper.success(customerId,"Customer  Updated Sucessfully"))
				:ResponseEntity.status(201).body(ResponseWrapper.created(customerId,"Customer created Successfully"));
	}
	
	@GetMapping("/activeCustomers")
	public ResponseEntity<ResponseWrapper<List<CustomerDto>>> getActiveCustomers() {
	    List<CustomerDto> customers = customerService.getAllActiveCustomers();
	    return ResponseEntity.ok(ResponseWrapper.success(customers, "Active customers retrieved successfully"));
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<ResponseWrapper<CustomerDto>> getCustomerById(@PathVariable Integer customerId) {
	    CustomerDto customer = customerService.getCustomerById(customerId);
	    return ResponseEntity.ok(ResponseWrapper.success(customer, "Customer retrieved successfully"));
	}


	@PostMapping("/{customerId}")
	public ResponseEntity<ResponseWrapper<String>> softDeleteCustomer(
	        @PathVariable Integer customerId,
	        @RequestParam Integer updatedBy) {

	    customerService.softDeleteCustomer(customerId, updatedBy);
	    return ResponseEntity.ok(ResponseWrapper.success("Customer soft-deleted successfully"));
	}
		
}
	
	
	
	
	