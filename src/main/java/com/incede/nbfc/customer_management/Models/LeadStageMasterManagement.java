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
@Table(name="lead_stage_master", schema="customers")
public class LeadStageMasterManagement extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="stage_id")
	private Integer stageId;
	
	@Column(name="tenant_id")
	private Integer tenentId;
	
	@Column(name="stage_name")
	private String stageName;
	
	@Column(name="identity")
	private UUID identity;
	
}
