package com.incede.nbfc.customer_management.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incede.nbfc.customer_management.DTOs.CustomerRelationshipDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerRelationshipService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relationships")
public class CustomerRelationshipController {

    private final CustomerRelationshipService relationshipService;

    public CustomerRelationshipController(CustomerRelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    // 1. Create new relationship
    @PostMapping
    public ResponseEntity<ResponseWrapper<Integer>> createRelationship(
            @Valid @RequestBody CustomerRelationshipDto dto) {
        CustomerRelationshipDto createdDto = relationshipService.createRelationship(dto); 
        Integer id = createdDto.getRelationshipId(); 
        return ResponseEntity.status(201)
                .body(ResponseWrapper.created(id, "Relationship created successfully."));
    }



    // 2. View active relationships for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseWrapper<List<CustomerRelationshipDto>>> getByCustomerId(
            @PathVariable Integer customerId) {
        List<CustomerRelationshipDto> list = relationshipService.getByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(list, "Active relationships fetched."));
    }
    

    // 3. Mark end of assignment (close relationship)
    @PatchMapping("/{relationshipId}/close")
    public ResponseEntity<ResponseWrapper<Integer>> closeRelationship(
            @PathVariable Integer relationshipId,
            @RequestBody Map<String, Object> requestBody) {
    	if (!requestBody.containsKey("assignedTo") || !requestBody.containsKey("updatedBy")) {
    	    throw new BusinessException("assignedTo and updatedBy are required.");
    	}
        LocalDate assignedTo = LocalDate.parse(requestBody.get("assignedTo").toString());
        Integer updatedBy = Integer.parseInt(requestBody.get("updatedBy").toString());
        CustomerRelationshipDto dto = relationshipService.closeRelationship(relationshipId, assignedTo, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(dto.getRelationshipId(), "Relationship closed successfully."));
    }


    // 4. Update last contacted date
    @PatchMapping("/{relationshipId}/last-contacted")
    public ResponseEntity<ResponseWrapper<Integer>> updateLastContacted(
            @PathVariable Integer relationshipId,
            @Valid@RequestBody Map<String, Object> requestBody) {    
            LocalDate lastContacted = LocalDate.parse(requestBody.get("lastContacted").toString());
            Integer updatedBy = Integer.parseInt(requestBody.get("updatedBy").toString());
            CustomerRelationshipDto dto = relationshipService.updateLastContacted(relationshipId, lastContacted, updatedBy);
            return ResponseEntity.ok(ResponseWrapper.success(dto.getRelationshipId(), "Last contacted date updated."));    
    }


    // 5. Soft delete relationship
    @DeleteMapping("/{relationshipId}")
    public ResponseEntity<ResponseWrapper<String>> softDelete(
            @PathVariable Integer relationshipId,
            @RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("updatedBy")) {
            throw new BusinessException("updatedBy is required.");
        }
        Integer updatedBy;
        try {
            updatedBy = Integer.parseInt(requestBody.get("updatedBy").toString());
        } catch (Exception e) {
            throw new BusinessException("Invalid format for updatedBy.");
        }
        relationshipService.softDelete(relationshipId, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Relationship deleted (soft) successfully."));
    }



    // 6. Get relationship by ID
    @GetMapping("/{relationshipId}")
    public ResponseEntity<ResponseWrapper<CustomerRelationshipDto>> getById(
            @PathVariable Integer relationshipId) {
        CustomerRelationshipDto dto = relationshipService.getById(relationshipId);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Relationship details retrieved."));
    }
}
