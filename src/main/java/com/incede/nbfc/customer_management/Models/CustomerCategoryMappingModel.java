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
import lombok.Data;

@Data
@Entity
@Table(name="customer_category_mappings", schema="customers")
public class CustomerCategoryMappingModel extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="category_map_id")
	private Integer categoryMappingId;
	
	@Column(name="customer_id", nullable=false)
	private Integer customerId;
	
	@Column(name="category_id", nullable=false)
	private Integer categoryId;
	
	@Column(name="assigned_date", nullable=false)
	private LocalDateTime assignedDate;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="identity",nullable=false,unique = true)
	private UUID identity;
	
	
}
