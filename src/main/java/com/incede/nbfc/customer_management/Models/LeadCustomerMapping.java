package com.incede.nbfc.customer_management.Models;

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

@Entity
@Table(name = "lead_customer_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadCustomerMapping extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer mappingId;

    @Column(name = "lead_id", nullable = false)
    private Integer leadId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "conversion_date", nullable = false)
    private Date conversionDate;

    @Column(name = "identity", nullable = false, unique = true, updatable = false)
    private UUID identity;
}

