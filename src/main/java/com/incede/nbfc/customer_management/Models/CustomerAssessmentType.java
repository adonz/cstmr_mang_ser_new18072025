package com.incede.nbfc.customer_management.Models;

import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assessment_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAssessmentType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_type_id")
    private Integer assessmentTypeId;

    @Column(name = "assessment_name", nullable = false, length = 100)
    private String assessmentName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "identity", nullable = false, updatable = false, unique = true)
    private UUID identity;

    @PrePersist
    public void generateIdentity() {
        if (this.identity == null) {
            this.identity = UUID.randomUUID();
        }
    }
}

