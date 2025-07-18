package com.incede.nbfc.customer_management.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers.customer_kyc_ckyc_uploads")
public class CustomerKYCCKYCUpload extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ckycUploadId;

    @Column(nullable = false)
    private Integer customerId;

    @Column
    private Integer kycId;

    @Column(nullable = false, length = 20)
    private String uploadStatus; // PENDING / SUCCESS / FAILED

    @Column(length = 100, unique = true)
    private String ckycReferenceNo;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadDate;
}
