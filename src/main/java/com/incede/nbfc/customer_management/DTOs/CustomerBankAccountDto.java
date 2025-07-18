package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerBankAccountDto {
	
	private Integer bankAccountId;
	
	@NotNull(message="Customer Id should not be null")
 	private Integer customerId;
	
 	@NotNull(message="Bank Id should not be null")
 	private Integer bankId;
	
 	@NotNull(message =" Branch Name should not be null")
 	private String branchName;
 	
 	@NotNull(message = "Bank name should not be empty")
 	private String bankName;
	
 	@NotNull(message = "IFSC code should not be null")
 	private String ifscCode;
	
 	@NotNull(message = "Account number should not be null")
 	private String accountNumber;
	
 	private String upiId;
 	
 	private String accountHolderName;
	
 	@Min(1)
 	@Max(2)
 	@NotNull(message="Account type should not be null")
 	private Integer accountType;
	
 	private Boolean isActive;
	
 	private Boolean isPrimary;
	
 	private UUID identity;
 	
 	private String paymentMode;
 	
 	private Integer bankProofFileId;
 	
 	private String accountStatus;
 	
 	
 	private Boolean isDelete;
 	
 	private LocalDateTime createdAt;
 	
 	private LocalDateTime updatedAt;
 	
 	@NotNull(message="Create by should not be null")
 	private Integer createdBy;
 	
 	private Integer updatedBy;
 	
 	
 	private Boolean isVerified;
	
 	private Integer verifiedBy;
	
 	private LocalDateTime verifiedAt;

}


