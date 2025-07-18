package com.incede.nbfc.customer_management.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationDetailsDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerRelationDetailsService;

import jakarta.validation.Valid;


@CrossOrigin(origins = "${cors.allowed-origin}")
@RestController
@RequestMapping("/v1/customer-relation-details")
public class CustomerRelationDetailsController {
	
 private final CustomerRelationDetailsService customerRelationDetailsService;

    public CustomerRelationDetailsController(CustomerRelationDetailsService customerRelationDetailsService) {
        this.customerRelationDetailsService = customerRelationDetailsService;
    }
    
    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<Integer>> createCustomerRelationDetails(
            @Valid @RequestBody CustomerRelationDetailsDto dto) {
        if (dto.getCustomerId() == null) {
            throw new IllegalArgumentException("customerId is required for creation.");
        }
        CustomerRelationDetailsDto savedDto = customerRelationDetailsService.createCustomerRelationDetails(dto);
        Integer customerId = savedDto.getCustomerId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.created(customerId, "Customer relation details created successfully"));
    }
    
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<CustomerRelationDetailsDto>> getCustomerRelationDetailsById(
            @PathVariable Integer customerId) {
        
        CustomerRelationDetailsDto dto = customerRelationDetailsService.getCustomerRelationDetailsById(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Customer relation details fetched successfully"));
    }
    
    
    @PostMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<String>> deleteCustomerRelationDetails(@PathVariable Integer customerId) {
        customerRelationDetailsService.deleteMapping(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(null,"Customer relation details deleted successfully"));
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<ResponseWrapper<Integer>> updateCustomerRelationDetails(
            @PathVariable Integer customerId,
            @Valid @RequestBody CustomerRelationDetailsDto dto) {       
        dto.setCustomerId(customerId); 
        CustomerRelationDetailsDto updatedDto = customerRelationDetailsService.updateCustomerRelationDetails(customerId, dto);
        return ResponseEntity.ok(ResponseWrapper.success(updatedDto.getCustomerId(), "Customer relation details updated successfully"));
    }

	
	
	
}
