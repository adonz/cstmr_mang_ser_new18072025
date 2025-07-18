package com.incede.nbfc.customer_management.Models;

import java.time.LocalDate;
import java.util.Date;
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
@Table(name = "customer_role_assignments")
public class CustomerRoleAssignment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_assignment_id")
    private Integer roleAssignmentId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "assigned_by", nullable = false)
    private Integer assignedBy;

    @Column(name = "assigned_date")
    private Date assignedDate = new Date();

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}

