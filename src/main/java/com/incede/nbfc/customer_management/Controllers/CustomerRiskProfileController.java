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

import com.incede.nbfc.customer_management.DTOs.CustomerRiskProfileDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerRiskProfileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/v1/risk-profile")
public class CustomerRiskProfileController {

    @Autowired
    private CustomerRiskProfileService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<CustomerRiskProfileDTO>> create(@RequestBody CustomerRiskProfileDTO dto) {
        return ResponseEntity.ok(ResponseWrapper.success(service.create(dto), "Risk profile created successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper<CustomerRiskProfileDTO>> update(@PathVariable Integer id, @RequestBody CustomerRiskProfileDTO dto) {
        return ResponseEntity.ok(ResponseWrapper.success(service.update(id, dto), "Risk profile updated successfully"));
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<ResponseWrapper<Void>> softDelete(@PathVariable Integer id, @RequestParam Integer userId) {
        service.softDelete(id, userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Risk profile soft-deleted"));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<CustomerRiskProfileDTO>>> getAll() {
        return ResponseEntity.ok(ResponseWrapper.success(service.getAll(), "Risk profiles fetched successfully"));
    }
}

