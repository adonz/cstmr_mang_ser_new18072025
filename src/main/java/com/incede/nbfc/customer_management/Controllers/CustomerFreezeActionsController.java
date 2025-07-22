package com.incede.nbfc.customer_management.Controllers;

import com.incede.nbfc.customer_management.DTOs.CustomerFreezeActionsDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerFreezeActionsService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://192.168.1.65:5173")
@RestController
@RequestMapping("/v1/customer-freezes")
public class CustomerFreezeActionsController {

    private final CustomerFreezeActionsService freezeActionsService;

    public CustomerFreezeActionsController(CustomerFreezeActionsService freezeActionsService) {
        this.freezeActionsService = freezeActionsService;
    }

    // CREATE
    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<CustomerFreezeActionsDto>> createFreezeAction(
            @PathVariable Integer customerId,
            @Valid @RequestBody CustomerFreezeActionsDto dto
    ) {
        dto.setCustomerId(customerId);
        CustomerFreezeActionsDto saved = freezeActionsService.createCustomerFreezeAction(dto);
        return ResponseEntity.status(201)
                .body(ResponseWrapper.created(saved, "Freeze action created successfully."));
    }

    // GET ALL BY CUSTOMER ID
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<List<CustomerFreezeActionsDto>>> getAllFreezeActions(
            @PathVariable Integer customerId
    ) {
        List<CustomerFreezeActionsDto> actions = freezeActionsService.getAllByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(actions, "Freeze actions retrieved successfully."));
    }

    // GET BY ID
//    @GetMapping("/{freezeId}")
//    public ResponseEntity<ResponseWrapper<CustomerFreezeActionsDto>> getFreezeActionById(
//            @PathVariable Integer customerId,
//            @PathVariable Integer freezeId
//    ) {
//        CustomerFreezeActionsDto action = freezeActionsService.getById(freezeId);
//        return ResponseEntity.ok(ResponseWrapper.success(action, "Freeze action retrieved successfully."));
//    }

    // UPDATE
    @PutMapping("/{customerId}/{freezeId}")
    public ResponseEntity<ResponseWrapper<CustomerFreezeActionsDto>> updateFreezeAction(
            @PathVariable Integer customerId,
            @PathVariable Integer freezeId,
            @Valid @RequestBody CustomerFreezeActionsDto dto
    ) {
        dto.setCustomerId(customerId);
        CustomerFreezeActionsDto updated = freezeActionsService.updateFreezeAction(freezeId, dto);
        return ResponseEntity.ok(ResponseWrapper.success(updated, "Freeze action updated successfully."));
    }

    // DELETE (Soft Delete)
    @PatchMapping("/{freezeId}/delete")
    public ResponseEntity<ResponseWrapper<String>> softDeleteFreezeAction(
            @PathVariable Integer freezeId
    ) {
        freezeActionsService.deleteFreezeAction(freezeId);
        return ResponseEntity.ok(ResponseWrapper.success("Freeze action soft-deleted successfully."));
    }
    
    @PostMapping("/freeze/lift")
    public ResponseEntity<ResponseWrapper<CustomerFreezeActionsDto>> liftFreeze(
            @RequestBody Map<String, Object> requestBody) {
        Integer freezeId = (Integer) requestBody.get("freezeId");
        Integer updatedBy = (Integer) requestBody.get("updatedBy"); // <- comes from request body
        LocalDate effectiveTo = null;
        if (requestBody.containsKey("effectiveTo") && requestBody.get("effectiveTo") != null) {
            effectiveTo = LocalDate.parse(requestBody.get("effectiveTo").toString());
        }
        CustomerFreezeActionsDto result = freezeActionsService.liftFreezeAction(freezeId, effectiveTo, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success(result, "Freeze lifted successfully."));
    }
    
    
    @GetMapping("/freeze/check")
    public ResponseEntity<ResponseWrapper<List<CustomerFreezeActionsDto>>> checkFreezeStatus(
            @RequestParam Integer customerId) {
        List<CustomerFreezeActionsDto> freezes = freezeActionsService.checkIfCustomerIsUnderFreeze(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(freezes, "Freeze status retrieved"));
    }



}
//Pending User Story  5
//Vlidation Check