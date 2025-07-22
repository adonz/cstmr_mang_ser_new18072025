package com.incede.nbfc.customer_management.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAdditionalDetailsDTO {

	@NotNull
    private Integer customerId;
    @NotNull
    private Integer nationalityId;

    @NotNull
    private Integer occupationId;

    @NotNull
    private Integer salary;

    @NotNull
    private Integer designationId;

    private String employerDetails;

    private String referralSource;

    private Integer canvasserEmployeeId;

    private Integer referCustomerId;

    private String sourceOfIncome;

    @NotNull
    private Boolean ownAnyAssets;

    @NotNull
    private Boolean pepStatus;

    private String pepCategory;

    private String pepRelationshipType;

    private String pepVerificationSource;

    @NotNull
    private Integer createdBy;
    
    private String nationality;
    private String occupationName;
    private String designationName;
    private Integer updatedBy;
    
    public CustomerAdditionalDetailsDTO(
    	    Integer customerId,
    	    Integer nationalityId,
    	    Integer occupationId,
    	    Integer salary,
    	    Integer designationId,
    	    String employerDetails,
    	    String referralSource,
    	    Integer canvasserEmployeeId,
    	    Integer referCustomerId,
    	    String sourceOfIncome,
    	    Boolean ownAnyAssets,
    	    Boolean pepStatus,
    	    String pepCategory,
    	    String pepRelationshipType,
    	    String pepVerificationSource,
    	    Integer createdBy
    	) {
    	    this.customerId = customerId;
    	    this.nationalityId = nationalityId;
    	    this.occupationId = occupationId;
    	    this.salary = salary;
    	    this.designationId = designationId;
    	    this.employerDetails = employerDetails;
    	    this.referralSource = referralSource;
    	    this.canvasserEmployeeId = canvasserEmployeeId;
    	    this.referCustomerId = referCustomerId;
    	    this.sourceOfIncome = sourceOfIncome;
    	    this.ownAnyAssets = ownAnyAssets;
    	    this.pepStatus = pepStatus;
    	    this.pepCategory = pepCategory;
    	    this.pepRelationshipType = pepRelationshipType;
    	    this.pepVerificationSource = pepVerificationSource;
    	    this.createdBy = createdBy;
    	}


}

