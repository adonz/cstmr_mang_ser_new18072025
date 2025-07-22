package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.incede.nbfc.customer_management.DTOs.LeadStageMasterManagementDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadStageMastermanagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/customer")
public class LeadStageMasterManagementController {
	
	private final LeadStageMastermanagementService leadStageService;
	
	public LeadStageMasterManagementController(LeadStageMastermanagementService leadStageService) {
		this.leadStageService= leadStageService;
	}

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<Integer>> createLeasStageMaster(
            @Valid @RequestBody LeadStageMasterManagementDto leadStageDto) {
         Integer saved = leadStageService.addLeadStageMaster(leadStageDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.created(saved, "leadStage details recorded successfully."));
    }
    
    @GetMapping("/lead-master-management/{leadStageId}")
    public ResponseEntity<ResponseWrapper<List<LeadStageMasterManagementDto>>> getByLeadStageId(
            @PathVariable Integer leadStageId) {
        List<LeadStageMasterManagementDto> list = leadStageService.getByLeadStageMatserId(leadStageId);
        return ResponseEntity.ok(ResponseWrapper.success(list, "Active lead Stage Masters fetched."));
    }
    
    @PutMapping("/lead-master-management/update/")
    public ResponseEntity<ResponseWrapper< Integer>> updateByLeadStageId(
            @RequestBody LeadStageMasterManagementDto leadStageDto) {
         Integer updatedDtat = leadStageService.updateByLeadStageMatserId(leadStageDto);
        return ResponseEntity.ok(ResponseWrapper.success(updatedDtat, "lead Stage Masters updated."));
    }
    
    @DeleteMapping("/lead-master-management/soft-delete/{leadStageId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteLeasStageMaster(
            @PathVariable Integer leadStageId,
            @RequestParam Integer  updatedBy) {

    	leadStageService.softDeleteLeadStageMaster(leadStageId, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success("Leas Stage Master soft-deleted successfully."));
    }
    
    
    
    
}
