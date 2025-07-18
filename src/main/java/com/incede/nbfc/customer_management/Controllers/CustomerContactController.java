package com.incede.nbfc.customer_management.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incede.nbfc.customer_management.DTOs.CustomerContactDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerContactService;

import java.util.List;


//@CrossOrigin(origins = "${cors.allowed-origin}")
@CrossOrigin(origins = "http://192.168.1.65:5173")
@RestController
@RequestMapping("/v1/customer/{customerId}/contacts")
public class CustomerContactController {

    private final CustomerContactService customerContactService;

    public CustomerContactController(CustomerContactService customerContactService) {
        this.customerContactService = customerContactService;
    }

    
    @PostMapping
    public ResponseEntity<ResponseWrapper<Integer>> addContact(
            @PathVariable Integer customerId,
            @Valid @RequestBody CustomerContactDto contactDto
    ) {
        CustomerContactDto savedDto = customerContactService.addContact(customerId, contactDto);
        return ResponseEntity.status(201)
                .body(ResponseWrapper.created(savedDto.getContactId(), "Contact added successfully."));
    }


    
    @PutMapping("{contactId}")
    public ResponseEntity<ResponseWrapper<Integer>> updateContact(
            @PathVariable Integer customerId,
            @PathVariable Integer contactId,
            @Valid @RequestBody CustomerContactDto contactDto
    ) {
        CustomerContactDto updatedDto = customerContactService.updateContact(customerId, contactId, contactDto);
        return ResponseEntity.ok(ResponseWrapper.success(updatedDto.getContactId(), "Contact updated successfully."));
    }


    
    @PatchMapping("/{contactId}")
    public ResponseEntity<ResponseWrapper<String>> softDeleteContact(
            @PathVariable Integer customerId,
            @PathVariable Integer contactId,
            @RequestParam Integer updatedBy
    ) {
        customerContactService.softDeleteContact(customerId, contactId, updatedBy);
        return ResponseEntity.ok(ResponseWrapper.success("Contact soft-deleted successfully."));
    }

  
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CustomerContactDto>>> listContacts(
            @PathVariable Integer customerId
    ) {
        List<CustomerContactDto> contacts = customerContactService.getActiveContacts(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(contacts, "Active contacts retrieved successfully."));
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ResponseWrapper<CustomerContactDto>> getContactById(
            @PathVariable Integer customerId,
            @PathVariable Integer contactId
    ) {
        CustomerContactDto contacts = customerContactService.getContactById(customerId, contactId);
        return ResponseEntity.ok(ResponseWrapper.success(contacts, "Contact retrieved successfully."));
    }
}
