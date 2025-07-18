package com.incede.nbfc.customer_management.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

@Entity
@Table(name = "customer_relationships")
@Data
@NoArgsConstructor
public class CustomerRelationship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Integer relationshipId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "staff_id", nullable = false)
    private Integer staffId;

    @Column(name = "assigned_from", nullable = false)
    private LocalDate assignedFrom;

    @Column(name = "assigned_to")
    private LocalDate assignedTo;

    @Column(name = "last_contacted")
    private LocalDate lastContacted;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

//    @Column(name = "is_del", nullable = false)
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
