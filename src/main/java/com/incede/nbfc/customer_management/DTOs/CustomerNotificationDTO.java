package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNotificationDTO {

    private Integer notificationId;
    private Integer customerId;
    private Boolean consentSms;
    private Boolean consentEmail;
    private Boolean consentWhatsapp;
    private UUID identity;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
    // Optional extras: customerName or contact methods if you want to enrich it
