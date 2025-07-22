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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lead_source_master")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LeadSourceMaster extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Integer sourceId;

    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "source_name", length = 100)
    private String sourceName;


    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;
    
    
    @PrePersist
    public void prePersist() {
    	if (identity == null) {
    		identity = UUID.randomUUID();
    	}
    }

}