package com.incede.nbfc.customer_management.Models;

import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customer_additional_reference_value", schema="customers")
public class CustomerAdditionalReferenceValue extends BaseEntity {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="customer_addi_ref_val_id")
	private Integer customerAdditionalReferenceValueId;
	
	@Column(name="customer_id", nullable=false)
	private Integer customerId;
	
	@Column(name="customer_addi_ref_name")
	private String customerAdditionalReferenceName;
	
	@Column(name="customer_addi_ref_value")
	private String customerAdditionalReferenceValue;
	
	@Column(name="identity", nullable=false)
	private UUID identity;
	
	
}
