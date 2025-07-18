package com.incede.nbfc.customer_management.Models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;


@Entity
@Table(name = "customer_contacts", schema = "customers")
@Data
@NoArgsConstructor
public class CustomerContact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer contactId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "contact_type", nullable = false)
    private Integer contactType; // 1=Mobile, 2=WhatsApp, 3=Landline, 4=Email

    @Column(name = "contact_value", nullable = false, unique = true)
    private String contactValue;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

//    @Column(name = "is_del")
//    private Boolean isDel = false;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
    
    
    @PrePersist
	public void prePersist() {
	    if (identity == null) {
	        identity = UUID.randomUUID();
	    }
	}


}
