package com.incede.nbfc.customer_management.Models;

import java.time.LocalDate;
import java.time.Period;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


import lombok.Data;


@Entity
@Table(name = "customer_relation_details")
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class CustomerRelationDetails extends BaseEntity {
	
	
    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "marital_status_id", nullable = false)
    private Integer maritalStatusId;

    @Column(name = "gender_id", nullable = false)
    private Integer genderId;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "is_minor")
    private Boolean isMinor;

    @Column(name = "father_name", nullable = false)
    private String fatherName;

    @Column(name = "mother_name", nullable = false)
    private String motherName;

    @Column(name = "annual_income", nullable = false)
    private Integer annualIncome;

    @Column(name = "customer_list_type")
    private String customerListType;

    @Column(name = "customer_value_score")
    private Integer customerValueScore;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
    
    
    @PrePersist
    @PreUpdate
    private void calculateIsMinor() {
        if (this.dob != null) {
            this.isMinor = Period.between(this.dob, LocalDate.now()).getYears() < 18;
        }
    }
	
}
