package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.incede.nbfc.customer_management.DTOs.CustomerRoleAssignmentDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerRoleAssignmentService;


@RestController
@RequestMapping("/v1/customermanagement/customer-role-assignments")
public class CustomerRoleAssignmentController {

    @Autowired
    private CustomerRoleAssignmentService assignmentService;

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<CustomerRoleAssignmentDTO>> assignRoleToCustomer(
            @RequestBody CustomerRoleAssignmentDTO dto) {
        CustomerRoleAssignmentDTO saved = assignmentService.assignRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.created(saved, "Role assigned successfully."));
    }
    
    @GetMapping("/active-roles/{customerId}")
    public ResponseEntity<ResponseWrapper<List<CustomerRoleAssignmentDTO>>> getActiveRoles(
            @PathVariable Integer customerId) {

        List<CustomerRoleAssignmentDTO> activeRoles = assignmentService.getActiveRolesByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(activeRoles, "Active roles fetched successfully."));
    }
    
    @PutMapping("/deactivate/{roleAssignmentId}")
    public ResponseEntity<ResponseWrapper<String>> deactivateRole(
            @PathVariable Integer roleAssignmentId,
            @RequestParam Integer adminUserId) {

        assignmentService.deactivateRoleAssignment(roleAssignmentId, adminUserId);
        return ResponseEntity.ok(ResponseWrapper.success("Role assignment deactivated successfully."));
    }
    
    @DeleteMapping("/soft-delete/{roleAssignmentId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteRoleAssignment(
            @PathVariable Integer roleAssignmentId,
            @RequestParam Integer adminUserId) {

        assignmentService.softDeleteRoleAssignment(roleAssignmentId, adminUserId);
        return ResponseEntity.ok(ResponseWrapper.success("Role assignment soft-deleted successfully."));
    }



}
