package com.incede.nbfc.customer_management.Models;

import java.math.BigDecimal;
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
@Table(name = "customer_risk_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRiskProfile extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_id")
    private Integer riskId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "assessment_type_id", nullable = false)
    private Integer assessmentTypeId;

    @Column(name = "risk_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Column(name = "risk_category", nullable = false)
    private Integer riskCategory;

    @Column(name = "assessment_date", nullable = false)
    private Date assessmentDate;

    @Column(name = "risk_reason", columnDefinition = "TEXT")
    private String riskReason;
    
    @Column(name = "identity", nullable = false, unique = true, updatable = false)
    private UUID identity;

}

