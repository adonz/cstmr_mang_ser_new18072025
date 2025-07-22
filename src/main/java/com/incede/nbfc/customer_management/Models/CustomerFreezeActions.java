package com.incede.nbfc.customer_management.Models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

@Entity
@Table(name = "customer_freeze_info", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFreezeActions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freeze_id")
    private Integer freezeId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "freeze_type", nullable = false)
    private String freezeType;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "status", nullable = false)
    private String status;

    

   
}

