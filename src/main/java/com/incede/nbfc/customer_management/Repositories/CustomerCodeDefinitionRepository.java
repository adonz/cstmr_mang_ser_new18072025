package com.incede.nbfc.customer_management.Repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incede.nbfc.customer_management.Models.*;

public interface CustomerCodeDefinitionRepository extends JpaRepository<CustomerCodeDefinition, Integer> {
 
	

    // 1. Get the active definition for a tenant within a date range
    Optional<CustomerCodeDefinition>
    findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByCreatedAtDesc(
        Integer tenantId, Date fromDate, Date toDate
    );

    // 2. Get all active, undeleted definitions for a tenant
    List<CustomerCodeDefinition>
    findByTenantIdAndIsActiveTrueAndIsDeleteFalseOrderByCreatedAtDesc(Integer tenantId);

    // ✅ 3. Get current active definition (where effective_to is null or >= current)
    Optional<CustomerCodeDefinition>
    findTopByTenantIdAndIsActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByCreatedAtDesc(
        Integer tenantId, Date currentDate
    );

    // ✅ 4. Deactivate all existing definitions for a tenant (custom update use case)
    List<CustomerCodeDefinition>
    findByTenantIdAndIsActiveTrue(Integer tenantId);
    
    boolean existsByTenantIdAndIsActiveTrue(Integer tenantId);



}
