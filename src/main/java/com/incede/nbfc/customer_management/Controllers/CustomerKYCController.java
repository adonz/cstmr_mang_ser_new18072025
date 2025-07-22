package com.incede.nbfc.customer_management.Controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.customer_management.DTOs.CustomerDto;
import com.incede.nbfc.customer_management.DTOs.CustomerKYCDTO;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.FeignClientsModels.DocumentRequirementConfigurationDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerKYCService;
import com.incede.nbfc.customer_management.Services.CustomerService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "${cors.allowed-origin}")
@RestController
@RequestMapping("/v1/customermanagement/customer-KYC")
public class CustomerKYCController {

	private final CustomerKYCService customerKYCService;
	
	private final CustomerService customerService;
	
	public CustomerKYCController(CustomerKYCService customerKYCService , CustomerService customerService) {
		this.customerKYCService = customerKYCService;
		this.customerService = customerService;
	}
    
	@PostMapping(value = "/upload", consumes = {"multipart/form-data"})
	public ResponseEntity<CustomerKYCDTO> uploadKYC(@Valid
	        @RequestPart("data") String dtoJson,
	        @RequestPart("document") MultipartFile document) throws JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    CustomerKYCDTO dto = mapper.readValue(dtoJson, CustomerKYCDTO.class);
	    return ResponseEntity.ok(customerKYCService.uploadKYCDocument(dto, document));
	}
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseWrapper<List<Map<String, Object>>>> viewCustomerKYC(
            @PathVariable Integer customerId,
            @RequestParam(required = false) Integer documentId,
            @RequestParam(required = false) Boolean isVerified) {
        List<Map<String, Object>> result = customerKYCService.getKYCForCustomer(customerId, documentId, isVerified);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseWrapper.success(result)); 
        }
        return ResponseEntity.ok(ResponseWrapper.success(result));
    }
    
    @PutMapping("/verify/{kycId}")
    public ResponseEntity<ResponseWrapper<String>> verifyKYC(
            @PathVariable Integer kycId,
            @RequestParam Integer customerId,
            Authentication auth) {

        try {
        	customerKYCService.verifyKYC(kycId, customerId);
            return ResponseEntity.ok(ResponseWrapper.success("KYC document marked as verified."));
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.success(null));
        }
    }

    @DeleteMapping("/delete/{kycId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteKYC(
            @PathVariable Integer kycId,
            @RequestParam Integer cusotmerId,
            Authentication auth) {

        try {
        	customerKYCService.softDeleteKYC(kycId, cusotmerId);
            return ResponseEntity.ok(ResponseWrapper.success("KYC document successfully deleted."));
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.success(null));
        }
    }
    
    @PutMapping("/update/{kycId}")
    public ResponseEntity<ResponseWrapper<String>> updateKYC(
            @PathVariable Integer kycId,
            @RequestParam Integer cusotmerId,
            @RequestPart(required = false) MultipartFile newDocument,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date validFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date validTo,
            Authentication auth) {
        try {
        	customerKYCService.updateKYCDocument(kycId, cusotmerId, newDocument, validFrom, validTo);
            return ResponseEntity.ok(ResponseWrapper.success("KYC document updated successfully."));
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.success(null));
        }
    }
    
    @GetMapping("/documentTypes")
    public ResponseEntity<ResponseWrapper<List<DocumentRequirementConfigurationDto>>> getDocumentTypes() {
    List<DocumentRequirementConfigurationDto> documentType = customerKYCService.getAllDocumentTypes();
    return ResponseEntity.ok(ResponseWrapper.success(documentType, "DocumentTypes fetched from Master Data successfully."));
    }
    
	@GetMapping("/activeCustomers")
	public ResponseEntity<ResponseWrapper<List<CustomerDto>>> getActiveCustomers() {
	    List<CustomerDto> customers = customerService.getAllActiveCustomers();
	    return ResponseEntity.ok(ResponseWrapper.success(customers, "Active customers retrieved successfully"));
	}
	
}







