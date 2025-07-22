package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerAssessmentTypeDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerAssessmentTypeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/v1/assessment-type")
public class CustomerAssessmentTypeController {

    @Autowired
    private CustomerAssessmentTypeService assessmentTypeService;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<CustomerAssessmentTypeDTO>> create(@RequestBody CustomerAssessmentTypeDTO dto) {
        CustomerAssessmentTypeDTO saved = assessmentTypeService.create(dto);
        return ResponseEntity.ok(ResponseWrapper.success(saved, "Assessment type created successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper<CustomerAssessmentTypeDTO>> update(
            @PathVariable Integer id,
            @RequestBody CustomerAssessmentTypeDTO dto) {
    	CustomerAssessmentTypeDTO updated = assessmentTypeService.update(id, dto);
        return ResponseEntity.ok(ResponseWrapper.success(updated, "Assessment type updated successfully"));
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<ResponseWrapper<String>> toggleActive(@PathVariable Integer id) {
        assessmentTypeService.toggleStatus(id);
        return ResponseEntity.ok(ResponseWrapper.success("Assessment type status updated"));
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> softDelete(@PathVariable Integer id) {
        assessmentTypeService.softDelete(id);
        return ResponseEntity.ok(ResponseWrapper.success("Assessment type soft-deleted"));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<CustomerAssessmentTypeDTO>>> getAll() {
        List<CustomerAssessmentTypeDTO> types = assessmentTypeService.getAllActive();
        return ResponseEntity.ok(ResponseWrapper.success(types, "Fetched assessment types successfully"));
    }
}

