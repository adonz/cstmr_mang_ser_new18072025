package com.incede.nbfc.customer_management.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.LeadMasterDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadMasterService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/v1/customer")
public class LeadMasterController {
 
	private final LeadMasterService leadService;
	
	public LeadMasterController(LeadMasterService leadService) {
		this.leadService=leadService;
	}
	
	
	@PostMapping("/lead_master/")
	ResponseEntity<ResponseWrapper<Integer>> createCustomerLeadMater(@Valid @RequestBody LeadMasterDto leadDto){
		Integer leadId = leadService.createLeadMaster(leadDto);
		return ResponseEntity.ok(ResponseWrapper.created(leadId,"Customer Lead master Created successfully"));
	}
	
	@PutMapping("/lead_master/update/")
	ResponseEntity<ResponseWrapper<Integer>> updateCustomerLeadMater(@RequestBody LeadMasterDto leadDto){
		Integer leadId = leadService.updateLeadmaster(leadDto);
		return ResponseEntity.ok(ResponseWrapper.success(leadId,"Customer Lead master Updated successfully"));
	}
	
	@GetMapping("/lead_master/get/leadId/")
	ResponseEntity<ResponseWrapper<LeadMasterDto>> getCustomerLeadMaterDetailsById(@PathVariable Integer leadMasterId){
		 LeadMasterDto leadId = leadService.getLeadMasterByLeadId(leadMasterId);
		return ResponseEntity.ok(ResponseWrapper.success(leadId,"Customer Lead master Updated successfully"));
	}
	
	@GetMapping("/lead_master/get/Active-Accounts")
	public ResponseEntity<ResponseWrapper< Page<LeadMasterDto>>> getAllActiveCustomerLeadMaterDetailsById(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<LeadMasterDto>  leadMasterDetails = leadService.getAllLeadMasterDetails(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(leadMasterDetails));
	}
	
	@PostMapping("/lead_master/delete")
	public ResponseEntity<ResponseWrapper< String>> deleteLeadmasterDetailsByLeadId(@RequestBody LeadMasterDto  leadDto){
		 String resultDto = leadService.softDeleteLeadMaster(leadDto);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto));
	}
}