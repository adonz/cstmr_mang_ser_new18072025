package com.incede.nbfc.customer_management.Models;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;
import com.incede.nbfc.customer_management.Enums.ProductServicesTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="product_services",schema="customers")
public class ProductAndServicesModel extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_service_id")
	private Integer productServiceId;
	
	@Column(name="tenant_id", nullable =false)
	private Integer tenantId;
	
	@Column(name="ps_name", nullable=false)
	private String productServiceName;
	
	@Column(name="ps_code", nullable=false)
	private String productServiceCode;
	
	@Column(name="ps_type", nullable=false)
	@Enumerated(EnumType.STRING)
	private ProductServicesTypes productServiceType;
	
	@Column(name="description")
	private String description;
	
	@Column(name="is_active")
	private Boolean isActive;

}
