package com.incede.nbfc.customer_management.Models;

import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "activity_type_master")
public class LeadActivityType extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_type_id")
    private Integer activityTypeId;

    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "type_name", length = 100)
    private String typeName;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
    
    
    @PrePersist
    public void prePersist() {
    	if (identity == null) {
    		identity = UUID.randomUUID();
    	}
    }

}
