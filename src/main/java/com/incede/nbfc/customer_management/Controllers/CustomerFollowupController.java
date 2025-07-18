package com.incede.nbfc.customer_management.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerFollowupDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerFollowupService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "${cors.allowed-origin}")
@RestController
@RequestMapping("/v1/customer-followups")

public class CustomerFollowupController {
	
	
	  private final CustomerFollowupService customerFollowupService;

     public CustomerFollowupController(CustomerFollowupService customerFollowupService) {
	        this.customerFollowupService = customerFollowupService;
	    }

	    @PostMapping("/")
	    public ResponseEntity<ResponseWrapper<Integer>> createOrUpdate(@Valid @RequestBody CustomerFollowupDto dto) {
	        boolean isUpdate = dto.getFollowupId() != null;
	        CustomerFollowupDto result = customerFollowupService.createOrUpdate(dto);
	        Integer followupId = result.getFollowupId();
	        return isUpdate
	                ? ResponseEntity.ok(ResponseWrapper.success(followupId, "Customer follow-up updated successfully"))
	                : ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created(followupId, "Customer follow-up created successfully"));
	    }
	    
	    @GetMapping("/customer/{customerId}")
	    public ResponseEntity<ResponseWrapper<List<CustomerFollowupDto>>> getFollowupsByCustomerId(@PathVariable Integer customerId) {
	        List<CustomerFollowupDto> followups = customerFollowupService.getFollowupsByCustomerId(customerId);

	        return ResponseEntity.ok(
	            ResponseWrapper.success(followups, "Customer follow-up history fetched successfully")
	        );
	    }
	    
	  
       @PutMapping("/update")
	    public ResponseEntity<ResponseWrapper<Integer>> updateFollowup(@Valid @RequestBody CustomerFollowupDto dto) {
	        CustomerFollowupDto updatedDto = customerFollowupService.updateFollowup(dto);
	        return ResponseEntity.ok(
	            ResponseWrapper.success(updatedDto.getFollowupId(), "Customer follow-up updated successfully")
	        );
	    }

	    
     @PostMapping("/delete/{followupId}")
     public ResponseEntity<ResponseWrapper<String>> softDeleteFollowup(
             @PathVariable Integer followupId,
             @RequestBody Map<String, Integer> body) {        
         Integer updatedBy = body.get("updatedBy");
         customerFollowupService.softDeleteFollowup(followupId, updatedBy);        
         return ResponseEntity.ok(ResponseWrapper.success(null , "Follow-up deleted successfully."));
     }
    
	    
     @GetMapping("/staff/{staffId}/upcoming")
     public ResponseEntity<ResponseWrapper<List<CustomerFollowupDto>>> getUpcomingFollowupsForStaff(
             @PathVariable Integer staffId) {     
         List<CustomerFollowupDto> upcoming = customerFollowupService.getUpcomingFollowupsForStaffiWithinSevenDays(staffId);
         String message = upcoming.isEmpty()
                 ? "No upcoming follow-ups found for staff ID: " + staffId
                 : "Upcoming follow-ups fetched successfully for staff ID: " + staffId;           
         return ResponseEntity.ok(
             ResponseWrapper.success(upcoming, message)
         );
     }
   
     
     //Extra End points (Not mentioned in the user story )
     @GetMapping("/{followupId}")
     public ResponseEntity<ResponseWrapper<CustomerFollowupDto>> getFollowupById(@PathVariable Integer followupId) {
         CustomerFollowupDto dto = customerFollowupService.getFollowupById(followupId);
         return ResponseEntity.ok(ResponseWrapper.success(dto, "Follow-up fetched successfully"));
     }
     
     
     @GetMapping("/active")
     public ResponseEntity<ResponseWrapper<List<CustomerFollowupDto>>> getAllActiveFollowups() {
         List<CustomerFollowupDto> activeFollowups = customerFollowupService.getAllActiveFollowups();
         return ResponseEntity.ok(ResponseWrapper.success(activeFollowups, "Active follow-ups fetched successfully"));
     }

    


}
