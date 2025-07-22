package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.LeadAssignmentDTO;
import com.incede.nbfc.customer_management.DTOs.LeadAssignmentHistoryDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.LeadAssignmentService;

@RestController
@RequestMapping("/v1/customermanagement/customer-lead-assignments")
public class LeadAssignmentController {

	@Autowired
    private LeadAssignmentService assignmentService;
    @PostMapping("/assign")
    public ResponseEntity<ResponseWrapper<LeadAssignmentDTO>> assignLead(
            @RequestBody LeadAssignmentDTO dto) {
        LeadAssignmentDTO result = assignmentService.assignLead(dto);
        return ResponseEntity.ok(ResponseWrapper.success(result));
    }
    
    @PutMapping("/reassign")
    public ResponseEntity<ResponseWrapper<LeadAssignmentDTO>> reassignLead(
            @RequestParam Integer leadId,
            @RequestParam Integer newAssignedTo,
            @RequestParam Integer adminId) {

        LeadAssignmentDTO result = assignmentService.reassignLead(leadId, newAssignedTo, adminId);
        return ResponseEntity.ok(ResponseWrapper.success(result));
    }
    
    @GetMapping("/history/{leadId}")
    public ResponseEntity<ResponseWrapper<List<LeadAssignmentHistoryDTO>>> viewAssignmentHistory(
            @PathVariable Integer leadId) {

        List<LeadAssignmentHistoryDTO> history = assignmentService.getAssignmentHistory(leadId);
        return ResponseEntity.ok(ResponseWrapper.success(history));
    }

    @PutMapping("/soft-delete/{assignmentId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteAssignmentRecord(
            @PathVariable Integer assignmentId,
            @RequestParam Integer adminUserId) {

        assignmentService.softDeleteAssignment(assignmentId, adminUserId);
        return ResponseEntity.ok(
            ResponseWrapper.success("Assignment record marked as deleted.")
        );
    }

    
    


	
}
