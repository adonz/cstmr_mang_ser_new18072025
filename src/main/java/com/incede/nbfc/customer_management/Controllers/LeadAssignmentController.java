package com.incede.nbfc.customer_management.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.incede.nbfc.customer_management.DTOs.LeadAssignmentDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadAssignmentService;

public class LeadAssignmentController {

	@Autowired
    private LeadAssignmentService assignmentService;

    @PostMapping("/assign")
    public ResponseEntity<ResponseWrapper<LeadAssignmentDTO>> assignLead(
            @RequestBody LeadAssignmentDTO dto) {
        LeadAssignmentDTO result = assignmentService.assignLead(dto);
        return ResponseEntity.ok(ResponseWrapper.success(result));
    }
	
}
