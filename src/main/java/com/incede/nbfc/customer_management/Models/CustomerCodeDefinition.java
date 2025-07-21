package com.incede.nbfc.customer_management.Models;

import java.sql.Date;
import com.incede.nbfc.customer_management.BaseEntity.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customer_code_definition", schema = "customers")
public class CustomerCodeDefinition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_code_definition_id")
    private Integer id;

    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "max_length", nullable = false)
    private Integer maxLength;

   
    @Column(name = "suffix")
    private String suffix;

    @Column(name = "incl_branch_code")
    private Boolean includeBranchCode;

    @Column(name = "branch_code_length", nullable = false)
    private Integer branchCodeLength;

    @Column(name = "incl_product_type")
    private Boolean includeProductType;

    @Column(name = "product_type_length", nullable = false)
    private Integer productTypeLength;

    @Column(name = "last_serial_no", nullable = false)
    private Integer lastSerialNo;

    @Column(name = "serial_no_length", nullable = false)
    private Integer serialNoLength;

    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;

    @Column(name = "effective_to")
    private Date effectiveTo;

    @Column(name = "is_active")
    private Boolean isActive;

}
