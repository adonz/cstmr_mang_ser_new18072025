package com.incede.nbfc.customer_management.Models;

import java.time.LocalDateTime;
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
@Table(name = "lead_assignment")
public class LeadAssignment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "lead_id", nullable = false)
    private Long leadId;

    @Column(name = "assigned_to", nullable = false)
    private Long assignedTo;

    @Column(name = "assigned_on", nullable = false)
    private LocalDateTime assignedOn;

    @Column(name = "identity_guid", nullable = false, unique = true)
    private UUID identityGuid;

}

