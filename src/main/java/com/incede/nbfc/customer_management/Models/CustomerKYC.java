package com.incede.nbfc.customer_management.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers.customer_kyc")
public class CustomerKYC extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kycId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "document_id", nullable = false)
    private Integer documentId;

    @Column(name = "id_number", nullable = false, length = 50)
    private String idNumber;

    @Column(name = "place_of_issue", length = 100)
    private String placeOfIssue;

    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_from")
    private Date validFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "document_file_id" , nullable = false)
    private Integer documentFileId;


    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;


    // Getters and setters can go here (or use Lombok for brevity)
}
