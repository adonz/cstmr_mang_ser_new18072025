package com.incede.nbfc.customer_management.DTOs;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.Data;


@Data
public class CustomerCodeDefinitionDto {
	
	
    private Integer id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "max_length", nullable = false)
    private Integer maxLength = 15;

   
    @Column(name = "suffix", length = 3)
    private String suffix;

    @Column(name = "incl_branch_code")
    private Boolean includeBranchCode = true;

    @Column(name = "branch_code_length", nullable = false)
    private Integer branchCodeLength = 4;

    @Column(name = "incl_product_type")
    private Boolean includeProductType = true;

    @Column(name = "product_type_length", nullable = false)
    private Integer productTypeLength = 3;

    @Column(name = "last_serial_no", nullable = false)
    private Integer lastSerialNo;

    @Column(name = "serial_no_length", nullable = false)
    private Integer serialNoLength = 5;

    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;

    @Column(name = "effective_to")
    private Date effectiveTo;

    @Column(name = "is_active")
    private Boolean isActive = true;
    
    
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
	private Boolean isDelete;



}
