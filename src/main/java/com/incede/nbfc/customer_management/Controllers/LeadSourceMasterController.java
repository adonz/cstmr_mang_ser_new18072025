package com.incede.nbfc.customer_management.Controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.incede.nbfc.customer_management.DTOs.LeadSourceMasterDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadSourceMasterService;



@RestController
@RequestMapping("/v1/lead-sources")
public class LeadSourceMasterController {

    private final LeadSourceMasterService leadSourceMasterService;

    public LeadSourceMasterController(LeadSourceMasterService leadSourceMasterService) {
        this.leadSourceMasterService = leadSourceMasterService;
    }
    
    // ✅ Create lead source

    // ✅ User Story 1: Create lead source
    @PostMapping
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> createLeadSource(
            @RequestBody LeadSourceMasterDto dto
    ) {
        LeadSourceMasterDto created = leadSourceMasterService.create(dto);
        return ResponseEntity.ok(ResponseWrapper.success(created, "Lead Source created successfully"));
    }

    // ✅ User Story 2: Update lead source name by sourceId
    @PutMapping("/{sourceId}")
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> updateLeadSource(
            @PathVariable Integer sourceId,
            @RequestBody LeadSourceMasterDto dto
    ) {
        LeadSourceMasterDto updated = leadSourceMasterService.update(sourceId, dto);
        return ResponseEntity.ok(ResponseWrapper.success(updated, "Lead Source updated successfully"));
    }

    // ✅ User Story 3: Soft delete lead source by sourceId
    @DeleteMapping("/{sourceId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteLeadSource(
            @PathVariable Integer sourceId,
            @RequestParam Integer updatedBy
    ) {
        leadSourceMasterService.softDelete(sourceId, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success("Lead Source soft deleted successfully"));
    }

    // ✅ User Story 4: View all by tenant
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<LeadSourceMasterDto>>> getAllByTenant() {
        List<LeadSourceMasterDto> sources = leadSourceMasterService.getAllByTenant();
        return ResponseEntity.ok(ResponseWrapper.success(sources, "Lead Sources retrieved successfully"));
    }

    // ✅ User Story 5: Get by UUID
    @GetMapping("/identity/{uuid}")
    public ResponseEntity<ResponseWrapper<LeadSourceMasterDto>> getByIdentity(
            @PathVariable UUID uuid
    ) {
        LeadSourceMasterDto dto = leadSourceMasterService.getByIdentity(uuid);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Lead Source retrieved successfully by identity"));
    }

}
