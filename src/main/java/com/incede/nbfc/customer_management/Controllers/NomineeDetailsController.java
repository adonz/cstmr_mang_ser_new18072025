package com.incede.nbfc.customer_management.Controllers;

import java.util.List;
import java.util.Map;

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

import com.incede.nbfc.customer_management.DTOs.NomineeDetailsDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.NomineeDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/customermanagement/customer-nominees-details")
public class NomineeDetailsController {

    @Autowired
    private NomineeDetailsService nomineeDetailsService;

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<NomineeDetailsDTO>> createNominee(
            @Valid @RequestBody NomineeDetailsDTO dto) {
        NomineeDetailsDTO saved = nomineeDetailsService.addNominee(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.created(saved, "Nominee details recorded successfully."));
    }
    
    @GetMapping("/view-all/{customerId}")
    public ResponseEntity<ResponseWrapper<List<Map<String, Object>>>> viewNominees(@PathVariable Integer customerId) {
        List<Map<String, Object>> result = nomineeDetailsService.getActiveNominees(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(result, "Nominee list fetched successfully."));
    }
    
    @PutMapping("/update/{nomineeId}")
    public ResponseEntity<ResponseWrapper<String>> updateNominee(
            @PathVariable Integer nomineeId,
            @RequestBody NomineeDetailsDTO dto,
            @RequestParam Integer userId) {

        nomineeDetailsService.updateNominee(nomineeId, dto, userId);
        return ResponseEntity.ok(ResponseWrapper.success("Nominee details updated successfully."));
    }
    
    @DeleteMapping("/soft-delete/{nomineeId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteNominee(
            @PathVariable Integer nomineeId,
            @RequestParam Integer userId) {

        nomineeDetailsService.softDeleteNominee(nomineeId, userId);
        return ResponseEntity.ok(ResponseWrapper.success("Nominee record soft-deleted successfully."));
    }



}
