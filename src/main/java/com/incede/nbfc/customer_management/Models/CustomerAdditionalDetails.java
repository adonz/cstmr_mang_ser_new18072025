package com.incede.nbfc.customer_management.Models;


import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_additional_details", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdditionalDetails extends BaseEntity{

	@Id
	@Column(name = "customer_id" , nullable = false)
	private Integer customerId;

    @Column(name = "nationality_id", nullable = false)
    private Integer nationalityId;

    @Column(name = "occupation_id", nullable = false)
    private Integer occupationId;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Column(name = "designation_id", nullable = false)
    private Integer designationId;

    @Column(name = "employer_details")
    private String employerDetails;

    @Column(name = "referral_source")
    private String referralSource;

    @Column(name = "canvasser_employee_id")
    private Integer canvasserEmployeeId;

    @Column(name = "refer_customer_id")
    private Integer referCustomerId;

    @Column(name = "source_of_income")
    private String sourceOfIncome;

    @Column(name = "own_any_assets", nullable = false)
    private Boolean ownAnyAssets;

    @Column(name = "pep_status", nullable = false)
    private Boolean pepStatus;

    @Column(name = "pep_category")
    private String pepCategory;

    @Column(name = "pep_relationship_type")
    private String pepRelationshipType;

    @Column(name = "pep_verification_source")
    private String pepVerificationSource;

}
