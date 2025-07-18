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
@Table(name = "customer_groups", schema = "customers")
@Data

public class CustomerGroup extends BaseEntity {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    
    private Boolean isActive = true;


    @Column(name = "identity", nullable = false, unique = true, columnDefinition = "UUID")
    private UUID identity;



    @PrePersist
    public void prePersist() {
        if (identity == null) {
            identity = UUID.randomUUID();
        }
    }
}
