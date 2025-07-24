package com.incede.nbfc.customer_management.Controllers;

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

import com.incede.nbfc.customer_management.DTOs.CustomerCodeDefinitionDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerCodeDefinitionService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://192.168.1.65:5173")
@RestController
@RequestMapping("/v1/code-definitions")
public class CustomerCodeDefinitionController {

    private final CustomerCodeDefinitionService customerCodeDefinitionService;

    public CustomerCodeDefinitionController(CustomerCodeDefinitionService customerCodeDefinitionService) {
        this.customerCodeDefinitionService = customerCodeDefinitionService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Integer>> createCodeDefinition(
//            @PathVariable Integer tenantId,
            @Valid @RequestBody CustomerCodeDefinitionDto dto
    ) {
//        dto.setTenantId(tenantId); 
        CustomerCodeDefinitionDto savedDto = customerCodeDefinitionService.createCustomerCodeDefinition(dto);
        return ResponseEntity.status(201)
                .body(ResponseWrapper.created(savedDto.getId(), "Customer code definition created successfully."));
    }
    

    
    
    @PostMapping("{tenantId}/generate")
    public ResponseEntity<ResponseWrapper<String>> generateCustomerCode(
            @PathVariable Integer tenantId,
            @RequestParam(required = false) String branchCode,
            @RequestParam(required = false) String productType,
            @RequestParam Integer userId) {

        String customerCode = customerCodeDefinitionService.generateCustomerCode(tenantId, branchCode, productType, userId);
        return ResponseEntity.ok(ResponseWrapper.success(customerCode, "Customer code generated successfully."));
    }
    
    
    @GetMapping("{tenantId}")
    public ResponseEntity<ResponseWrapper<List<CustomerCodeDefinitionDto>>> getDefinitionsByTenant(
            @PathVariable Integer tenantId) {
        List<CustomerCodeDefinitionDto> list = customerCodeDefinitionService.getActiveDefinitionsByTenant(tenantId);
        return ResponseEntity.ok(ResponseWrapper.success(list, "Customer code definitions fetched successfully."));
    }
    
    
    
    @PostMapping("/{customerCodeDefinitionId}/deactivate")
    public ResponseEntity<ResponseWrapper<String>> deactivateCodeDefinition(
//            @PathVariable Integer tenantId,
            @PathVariable Integer customerCodeDefinitionId,
            @RequestParam Integer userId) {
        customerCodeDefinitionService.deactivateCustomerCodeDefinition(customerCodeDefinitionId, userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Customer code definition deactivated successfully."));
    }


}

