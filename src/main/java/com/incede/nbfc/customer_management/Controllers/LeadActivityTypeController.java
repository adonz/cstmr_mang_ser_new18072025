package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.LeadActivityTypeDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadActivityTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/lead-activity-types")
@CrossOrigin(origins = "*") // You can restrict this in production
public class LeadActivityTypeController {

    private final LeadActivityTypeService leadActivityTypeService;

    public LeadActivityTypeController(LeadActivityTypeService leadActivityTypeService) {
        this.leadActivityTypeService = leadActivityTypeService;
    }

    // Create
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<LeadActivityTypeDto>> create(@RequestBody LeadActivityTypeDto dto) {
        LeadActivityTypeDto createdDto = leadActivityTypeService.create(dto);
        return ResponseEntity.ok(ResponseWrapper.created(createdDto, "Lead Activity Type created successfully"));
    }

    // Update
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper<LeadActivityTypeDto>> update(
            @PathVariable Integer id,
            @RequestParam Integer tenantId,
            @RequestParam String newTypeName,
            @RequestParam Integer updatedBy
    ) {
        LeadActivityTypeDto updatedDto = leadActivityTypeService.update(id, tenantId, newTypeName, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(updatedDto, "Lead Activity Type updated successfully"));
    }

    // Soft Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> delete(
            @PathVariable Integer id,
            @RequestParam Integer tenantId,
            @RequestParam Integer userId
    ) {
    	leadActivityTypeService.softDelete(id, tenantId, userId);
        return ResponseEntity.ok(ResponseWrapper.success("Deleted successfully"));
    } 

    // Get All Active
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<LeadActivityTypeDto>>> getAll(@RequestParam Integer tenantId) {
        List<LeadActivityTypeDto> allDtos = leadActivityTypeService.getAllActiveForTenant(tenantId);
        return ResponseEntity.ok(ResponseWrapper.success(allDtos, "Fetched all active lead activity types"));
    }
}
