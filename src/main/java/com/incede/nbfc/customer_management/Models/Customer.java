package com.incede.nbfc.customer_management.Models;
import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "tenant_id", nullable = false)
	private Integer tenantId;

	@Column(name = "customer_code", nullable = false, length = 15)
	private String customerCode;

	@Column(name = "salutation_id", nullable = false)
	private Integer salutationId;

	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Column(name = "middle_name", length = 50)
	private String middleName;

	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Column(name = "display_name", nullable = false, length = 100)
	private String displayName;

	@Column(name = "customer_mobileno", nullable = false, length = 10)
	private String customerMobileno;

	@Column(name = "tax_cat_id", nullable = false)
	private Integer taxCatId;

	@Column(name = "customer_status", nullable = false)
	private Integer customerStatus;

	@Column(name = "crm_reference_id", length = 100)
	private String crmReferenceId;
	
	@Column(name = "identity", nullable = false, unique = true, updatable = false)
	private UUID identity;

	@PrePersist
	public void prePersist() {
	    if (identity == null) {
	        identity = UUID.randomUUID();
	    }
	}
	
}
