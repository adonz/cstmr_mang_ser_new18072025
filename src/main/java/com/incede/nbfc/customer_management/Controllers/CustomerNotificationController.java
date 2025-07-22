package com.incede.nbfc.customer_management.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerNotificationDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerNotificationService;

@RestController
@RequestMapping("/v1/notifications")
public class CustomerNotificationController {

    @Autowired
    private CustomerNotificationService notificationService;

    @PostMapping("/record-consent")
    public ResponseEntity<ResponseWrapper<CustomerNotificationDTO>> recordConsent(@RequestBody CustomerNotificationDTO dto) {
        CustomerNotificationDTO savedDto = notificationService.recordConsent(dto);
        return ResponseEntity.ok(ResponseWrapper.created(savedDto, "Notification consent recorded successfully"));
    }

    @PutMapping("/update-consent/{customerId}")
    public ResponseEntity<ResponseWrapper<Void>> updateConsent(
            @PathVariable Integer customerId,
            @RequestBody CustomerNotificationDTO dto) {

        notificationService.updateConsent(customerId, dto);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Notification preferences updated successfully"));
    }

    @DeleteMapping("/soft-delete/{customerId}")
    public ResponseEntity<ResponseWrapper<Void>> softDeleteConsent(
            @PathVariable Integer customerId,
            @RequestParam Integer userId) {

        notificationService.softDeleteConsent(customerId, userId);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Notification preferences soft-deleted successfully"));
    }
}
