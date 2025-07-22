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

import com.incede.nbfc.customer_management.DTOs.CustomerGroupMappingDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerGroupMappingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/customer/customer-group-mappings")
public class CustomerGroupMappingController {
 
 private final CustomerGroupMappingService groupMappingService;
	 
	 public CustomerGroupMappingController(CustomerGroupMappingService groupMappingService) {
		 this.groupMappingService=groupMappingService;
	 }
	 
		@PostMapping("/")
		public ResponseEntity<ResponseWrapper<String>> createOrUpdateCustomerGroupMApping(
				@Valid @RequestBody   CustomerGroupMappingDto  groupDto){
			    boolean updateId = groupDto.getGroupMappingId() !=null;
			    Integer resultDto = groupMappingService.createOrUpdateCustomerGroupMapping(groupDto);
			   ResponseWrapper <String> response = updateId 
					   ? ResponseWrapper.success("ID" + resultDto +"updated successfully")
					    : ResponseWrapper.created("ID" + resultDto +"created successfully");
			   return updateId
			   			? ResponseEntity.ok(response)
		                : ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		
		@GetMapping("/get/{groupMappingId}")
		public ResponseEntity<ResponseWrapper<CustomerGroupMappingDto>> getCustomerGroupDetails(
				@PathVariable Integer groupMappingId){
			CustomerGroupMappingDto categoryDetails = groupMappingService.getCustomerGroupById(groupMappingId);
			 return ResponseEntity.ok(ResponseWrapper.success(categoryDetails,"customer group details retrived successfully"));
	 
		}
		
		@DeleteMapping("/delete")
		public ResponseEntity<ResponseWrapper< String>> deleteCustomerGroupDetails(
				 @RequestBody CustomerGroupMappingDto groupMappingDto){
			String categoryModel = groupMappingService.SoftdeleteCustomerGroupDetailsById(groupMappingDto);
			 return ResponseEntity.ok(ResponseWrapper.success(categoryModel,"customer categort deleted successfully"));
	 
		}
		
		@GetMapping("/Active-Accounts")
		public ResponseEntity<ResponseWrapper< Page<CustomerGroupMappingDto>>> getAllActiveCustomerGroupInfo(
				@RequestParam(defaultValue = "0") int page ,
				@RequestParam(defaultValue = "10") int size){
					Page<CustomerGroupMappingDto> accountList = groupMappingService.getAllCustomerGroupDetails(page, size);
					return ResponseEntity.ok(ResponseWrapper.success(accountList));
		}
		
		
}
