package com.incede.nbfc.customer_management.Models;


import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="customer_optional_details",schema="customers")
public class CustomerOptionalDetailsModel extends BaseEntity {
	
	@Id
 	@Column(name="customer_id")
	private  Integer customerId;
	
	@Column(name="loan_purpose_id")
	private Integer loanPurposeId;
	
	@Column(name="residential_status", nullable = false)
	private String residentialStatus;
	
	@Column(name="education_level")
	private String educationalLevel;
	
	@Column(name="has_home_loan")
	private Boolean hasHomeLoan;
	
	@Column(name="home_loan_amount")
	private Integer homeLoanAmount;
	
	@Column(name="home_loan_company")
	private String homeLoanCompany;
	
	@Column(name="language_id")
	private Integer languageId;
	
	
	
}
