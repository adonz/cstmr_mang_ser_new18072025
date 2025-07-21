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
@Table(name="lead_master",schema="customers")
public class LeadMasterModel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="lead_id")
	private Integer leadId;
	
	@Column(name="tenant_id", nullable=false)
	private Integer tenantId;
	
	@Column(name="lead_code", unique = true)
	private String leadCode;
	
	@Column(name="full_name",nullable=false)
	private String fullName;
	
	@Column(name="contact_number",nullable = false)
	private String contactNumber;
	
	@Column(name="gender_id", nullable=false)
	private Integer genderId;
	
	@Column(name="email",nullable=true)
	private String emailId;
	
	@Column(name="door_number")
	private String doorNumber;
	
	@Column(name="address_line1")
	private String addressLine1;
	
	@Column(name="address_line2")
	private String addressLine2;
	
	@Column(name="land_mark")
	private String landMark;
	
	@Column(name="place_name")
	private String placeName;
	
	@Column(name="city")
	private String city;
	
	@Column(name="district")
	private String district;
	
	@Column(name="state_name")
	private String stateName;
	
	@Column(name="country_id")
	private Integer countryId;
	
	@Column(name="pincode")
	private  String pincode;
	
	@Column(name="remarks",nullable=true)
	private String remarks;
	
	@Column(name="source_id",nullable=false)
	private Integer sourceId;
	
	@Column(name="stage_id", nullable=false)
	private Integer stageId;
	
	@Column(name="status_id",nullable=false)
	private Integer statusId;
	
	@Column(name="interested_product_id",nullable=false)
	private Integer interestedProductId;
	
	@Column(name="identity", nullable= false)
	private UUID identity;
	
}

