package com.incede.nbfc.customer_management.Controllers;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incede.nbfc.customer_management.DTOs.CustomerGroupDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerGroupService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "${cors.allowed-origin}")
@RestController
@RequestMapping("/v1/customer-groups")
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    public CustomerGroupController(CustomerGroupService customerGroupService) {
        this.customerGroupService = customerGroupService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<Integer>> createCustomerGroup(@Valid @RequestBody CustomerGroupDto dto) {
        CustomerGroupDto saved = customerGroupService.createCustomerGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.created(saved.getGroupId(), "Customer group created successfully."));
    }
    
    
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ResponseWrapper<List<CustomerGroupDto>>> getActiveGroups(@PathVariable Integer tenantId) {
        List<CustomerGroupDto> groups = customerGroupService.getActiveGroupsByTenantId(tenantId);

        String message = groups.isEmpty()
            ? "No active customer groups found for tenant ID: " + tenantId
            : "Active customer groups fetched successfully";

        return ResponseEntity.ok(ResponseWrapper.success(groups, message));
    }
    
    @PutMapping("/update")
    public ResponseEntity<ResponseWrapper<Integer>> updateCustomerGroup(@Valid @RequestBody CustomerGroupDto dto) {
        CustomerGroupDto updatedDto = customerGroupService.updateCustomerGroup(dto);
        return ResponseEntity.ok(ResponseWrapper.success(
            updatedDto.getGroupId(),
            "Customer group updated successfully"
        ));
    }
    
    
    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<Void>> softDeleteCustomerGroup(
            @PathVariable Integer groupId,
            @RequestParam Integer userId) {
        // Call service method
        customerGroupService.softDeleteCustomerGroup(groupId, userId);
        
        return ResponseEntity.ok(ResponseWrapper.success(null, "Customer group soft deleted successfully"));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseWrapper<CustomerGroupDto>> getCustomerGroupById(@PathVariable Integer groupId) {
        CustomerGroupDto dto = customerGroupService.getCustomerGroupById(groupId);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Customer group fetched successfully"));
    }

    @PutMapping("/{groupId}/toggle-active")
    public ResponseEntity<ResponseWrapper<CustomerGroupDto>> toggleActiveStatus(
            @PathVariable Integer groupId,
            @RequestParam Integer updatedBy) {
        CustomerGroupDto updatedDto = customerGroupService.toggleActiveStatus(groupId, updatedBy);
        String message = updatedDto.getIsActive() ? "Customer group activated successfully" : "Customer group deactivated successfully";
        return ResponseEntity.ok(ResponseWrapper.success(updatedDto, message));
    }

    
}
