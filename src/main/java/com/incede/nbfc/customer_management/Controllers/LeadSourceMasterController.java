package com.incede.nbfc.customer_management.Controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.incede.nbfc.customer_management.DTOs.LeadSourceMasterDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadSourceMasterService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/lead-sources")
public class LeadSourceMasterController {

    private final LeadSourceMasterService leadSourceMasterService;

    public LeadSourceMasterController(LeadSourceMasterService leadSourceMasterService) {
        this.leadSourceMasterService = leadSourceMasterService;
    }
    
    // ✅ Create lead source
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> createLeadSource(
            @Valid @RequestBody LeadSourceMasterDto dto
    ) {
        System.out.println("Received DTO: " + dto);
        LeadSourceMasterDto created = leadSourceMasterService.create(dto);
        return ResponseEntity.ok(ResponseWrapper.success(created, "Lead Source created successfully"));
    }

    // ✅ Update lead source name
    @PutMapping("/update/{identity}")
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> updateLeadSourceName(
            @PathVariable UUID identity,
            @RequestParam String newSourceName,
            @RequestParam Integer updatedBy
    ) {
        LeadSourceMasterDto updated = leadSourceMasterService.updateSourceName(identity, newSourceName, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(updated, "Lead Source updated successfully"));
    }

    // ✅ Soft delete lead source by UUID
    @DeleteMapping("/delete/{identity}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteLeadSource(
            @PathVariable UUID identity,
            @RequestParam Integer deletedBy
    ) {
        leadSourceMasterService.softDeleteByIdentity(identity, deletedBy);
        return ResponseEntity.ok(ResponseWrapper.success("Lead Source deleted successfully"));
    }

    // ✅ Get all lead sources for current tenant
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<LeadSourceMasterDto>>> getAllByTenant() {
        List<LeadSourceMasterDto> sources = leadSourceMasterService.getAllByTenant();
        return ResponseEntity.ok(ResponseWrapper.success(sources, "Lead Sources retrieved successfully"));
    }

    // ✅ Get lead source by UUID
    @GetMapping("/{identity}")
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> getByIdentity(@PathVariable UUID identity) {
        LeadSourceMasterDto dto = leadSourceMasterService.getByIdentity(identity);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Lead Source retrieved successfully"));
    }

    

}
