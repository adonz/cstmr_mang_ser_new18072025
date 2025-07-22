package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.LeadCustomerMappingDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadCustomerMappingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/v1/customermanagement/customer-lead-mapping")
public class LeadCustomerMappingController {

    @Autowired
    private LeadCustomerMappingService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<LeadCustomerMappingDTO>> create(@RequestBody LeadCustomerMappingDTO dto) {
        System.out.println("Received DTO:");
        System.out.println("leadId = " + dto.getLeadId());
        System.out.println("customerId = " + dto.getCustomerId());
        System.out.println("createdBy = " + dto.getCreatedBy());
        return ResponseEntity.ok(ResponseWrapper.success(service.create(dto), "Mapping created"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper<LeadCustomerMappingDTO>> update(@PathVariable Integer id, @RequestBody LeadCustomerMappingDTO dto) {
        return ResponseEntity.ok(ResponseWrapper.success(service.update(id, dto), "Mapping updated"));
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<ResponseWrapper<Void>> softDelete(@PathVariable Integer id, @RequestParam Integer userId) {
        service.softDelete(id, userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Mapping soft-deleted"));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<LeadCustomerMappingDTO>>> list() {
        return ResponseEntity.ok(ResponseWrapper.success(service.getAllActive(), "Fetched lead-customer mappings"));
    }
}

