package com.incede.nbfc.customer_management.Models;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;


@Entity
@Table(
    name = "customer_followups",
    schema = "customers"
//    indexes = {
//        @Index(name = "idx_customer_id", columnList = "customer_id"),
//        @Index(name = "idx_staff_id", columnList = "staff_id"),
//        @Index(name = "idx_followup_date", columnList = "followup_date"),
//        @Index(name = "idx_status", columnList = "status")
//     
//    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFollowup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private Integer followupId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "staff_id", nullable = false)
    private Integer staffId;

    @Column(name = "followup_date", nullable = false)
    private LocalDate followupDate;

    @Column(name = "followup_type", nullable = false)
    private Integer followupType;

    @Column(name = "followup_notes", columnDefinition = "TEXT")
    private String followupNotes;

    @Column(name = "next_followup_date")
    private LocalDate nextFollowupDate;

    @Column(name = "status", nullable = false)
    private Integer status;


    @Column(name = "identity", nullable = false, unique = true, columnDefinition = "UUID")
    private UUID identity;
    
    

@PrePersist
public void prePersist() {
	if (identity == null) {
		identity = UUID.randomUUID();
	}
}

  
}
