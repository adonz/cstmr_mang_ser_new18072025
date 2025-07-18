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

@Entity
@Data
@Table(name="customer_bank_accounts", schema = "customers")
public class CustomerBankAccountModel extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bank_account_id")
	private Integer bankAccountId;
	
	@Column(name="customer_id" , nullable=false)
	private Integer customerId;
	
	@Column(name="bank_id", nullable = false)
	private Integer bankId;
	
	@Column(name="bank_name", nullable = false)
	private String bankName;
	
	@Column(name="branch_name", nullable=false)
	private String branchName;
	
	@Column(name="ifsc_code")
	private String ifscCode;
	
	@Column(name="account_number", nullable =false)
	private String accountNumber;
	
	@Column(name="upi_id", nullable =true )
	private String upiId;
	
	@Column(name="account_holder_name")
	private String accountHolderName;
 
	@Column(name="account_type")
	private Integer accountType;
	
	@Column(name="account_status")
	private String accountStatus;
	
	@Column(name="bank_proof_file_id")
	private  Integer bankProofFileId;
	
	@Column(name="payment_mode")
	private String paymentMode;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="is_primary")
	private Boolean isPrimary;
	
	@Column(name = "identity")
	private UUID identity;
	
	@Column(name="is_verified")
	private Boolean isVerified;
	
	@Column(name="verified_by")
	private Integer verifiedBy;
	
	@Column(name="verified_at")
	private LocalDateTime verifiedAt;

}
