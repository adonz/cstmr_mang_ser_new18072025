package com.incede.nbfc.customer_management.Models;

import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_notifications", schema = "customers")
public class CustomerNotification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "consent_sms")
    private Boolean consentSms = false;

    @Column(name = "consent_email")
    private Boolean consentEmail = false;

    @Column(name = "consent_whatsapp")
    private Boolean consentWhatsapp = false;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}

