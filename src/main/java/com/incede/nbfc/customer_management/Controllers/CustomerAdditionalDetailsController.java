package com.incede.nbfc.customer_management.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalDetailsDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.DesignationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.NationalityDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.OccupationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.StaffDto;
import com.incede.nbfc.customer_management.Models.CustomerAdditionalDetails;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerAdditionalDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/customers/additional-details")
public class CustomerAdditionalDetailsController {

    private final CustomerAdditionalDetailsService customerAdditionalDetailsService;
    
    public CustomerAdditionalDetailsController(CustomerAdditionalDetailsService customerAdditionalDetailsService)
    {
    	this.customerAdditionalDetailsService = customerAdditionalDetailsService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<CustomerAdditionalDetails>> createCustomerAdditionalDetails(
            @Valid @RequestBody CustomerAdditionalDetailsDTO dto) {
        
        CustomerAdditionalDetails saved = customerAdditionalDetailsService.save(dto);
        return ResponseEntity
                .status(201)
                .body(ResponseWrapper.created(saved, "Customer additional details saved successfully"));
    }

    
    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<String>> updateDetails(
            @PathVariable Integer customerId,
            @RequestBody CustomerAdditionalDetailsDTO dto) {
    	customerAdditionalDetailsService.updateDetails(customerId, dto);
        return ResponseEntity.ok(ResponseWrapper.success("Additional details updated successfully."));
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteDetails(
            @PathVariable Integer customerId,
            @RequestParam Integer userId) {
    	customerAdditionalDetailsService.softDelete(customerId, userId);
        return ResponseEntity.ok(ResponseWrapper.success("Additional details deleted successfully."));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseWrapper<CustomerAdditionalDetailsDTO>> viewDetails(
            @PathVariable Integer customerId) {
        CustomerAdditionalDetailsDTO dto = customerAdditionalDetailsService.getDetails(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(dto, "Additional details retrieved."));
    }
    
    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<CustomerAdditionalDetailsDTO>>> getAllDetails() {
        List<CustomerAdditionalDetailsDTO> details = customerAdditionalDetailsService.getAllDetails();
        return ResponseEntity.ok(ResponseWrapper.success(details, "All customer additional details fetched."));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<ResponseWrapper<List<CustomerAdditionalDetailsDTO>>> filterCustomerDetails(
        @RequestParam(required = false) Integer nationalityId,
        @RequestParam(required = false) Integer occupationId,
        @RequestParam(required = false) Integer designationId,
        @RequestParam(required = false) Integer referCustomerId,
        @RequestParam(required = false) Integer canvasserEmployeeId
    ) {
        List<CustomerAdditionalDetailsDTO> result = customerAdditionalDetailsService.filterDetails(
            nationalityId, occupationId, designationId, referCustomerId, canvasserEmployeeId
        );

        return ResponseEntity.ok(ResponseWrapper.success(result, "Filtered customer details fetched"));
    }
    
    @GetMapping("/nationalities")
    public ResponseEntity<ResponseWrapper<List<NationalityDTO>>> getNationalites() {
    List<NationalityDTO> nationality = customerAdditionalDetailsService.getAllNationalities();
    return ResponseEntity.ok(ResponseWrapper.success(nationality, "nationalitites fetched from Master Data successfully."));
    }
    
    @GetMapping("/occupations")
    public ResponseEntity<ResponseWrapper<List<OccupationDTO>>> getOccupations() {
    List<OccupationDTO> occupation = customerAdditionalDetailsService.getAllOccupations();
    return ResponseEntity.ok(ResponseWrapper.success(occupation, "occupations fetched from Master Data successfully."));
    }
    
    @GetMapping("/designations")
    public ResponseEntity<ResponseWrapper<List<DesignationDTO>>> getDesignations() {
    List<DesignationDTO> designation = customerAdditionalDetailsService.getAllDesignations();
    return ResponseEntity.ok(ResponseWrapper.success(designation, "designations fetched from Master Data successfully."));
    }
    
    @GetMapping("/staffs")
    public ResponseEntity<ResponseWrapper<List<StaffDto>>> getStaffs() {
    List<StaffDto> staffs = customerAdditionalDetailsService.getAllStaffs();
    return ResponseEntity.ok(ResponseWrapper.success(staffs, "staffs fetched from Master Data successfully."));
    }

    
}


