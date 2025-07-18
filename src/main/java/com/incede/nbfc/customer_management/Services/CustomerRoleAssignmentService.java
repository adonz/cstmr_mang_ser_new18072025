package com.incede.nbfc.customer_management.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerRoleAssignmentDTO;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.UserClient;
import com.incede.nbfc.customer_management.Models.CustomerRoleAssignment;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerRoleAssignmentRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;

import jakarta.transaction.Transactional;

@Service
public class CustomerRoleAssignmentService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserClient roleClient;

    @Autowired
    private CustomerRoleAssignmentRepository roleAssignmentRepository;

    @Transactional
    public CustomerRoleAssignmentDTO assignRole(CustomerRoleAssignmentDTO dto) {
        boolean customerExists = customerRepository.existsByCustomerIdAndIsDeleteFalse(dto.getCustomerId());
        if (!customerExists) {
            throw new DataNotFoundException("Invalid Customer ID.");
        }

        ResponseWrapper<Boolean> roleValidation = roleClient.getRoleById(dto.getRoleId());
        if (roleValidation == null || Boolean.FALSE.equals(roleValidation.getData())) {
            throw new DataNotFoundException("Invalid Role ID or role is inactive.");
        }

        // 3️⃣ Prepare Entity
        CustomerRoleAssignment entity = new CustomerRoleAssignment();
        entity.setCustomerId(dto.getCustomerId());
        entity.setRoleId(dto.getRoleId());
        entity.setAssignedBy(dto.getAssignedBy());
        entity.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : new Date());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setIsActive(true);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(dto.getCreatedBy());

        CustomerRoleAssignment saved = roleAssignmentRepository.save(entity);
        return toDTO(saved);
    }

    private CustomerRoleAssignmentDTO toDTO(CustomerRoleAssignment entity) {
        CustomerRoleAssignmentDTO dto = new CustomerRoleAssignmentDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    public List<CustomerRoleAssignmentDTO> getActiveRolesByCustomerId(Integer customerId) {
        List<CustomerRoleAssignment> roles = roleAssignmentRepository.findActiveRolesByCustomer(customerId);

        return roles.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void deactivateRoleAssignment(Integer roleAssignmentId, Integer adminUserId) {
        CustomerRoleAssignment assignment = roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(roleAssignmentId)
            .orElseThrow(() -> new DataNotFoundException("Role assignment not found or already deleted."));

        assignment.setIsActive(false);
        assignment.setUpdatedBy(adminUserId);
        roleAssignmentRepository.save(assignment);
    }
    
    @Transactional
    public void softDeleteRoleAssignment(Integer roleAssignmentId, Integer adminUserId) {
        CustomerRoleAssignment assignment = roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(roleAssignmentId)
            .orElseThrow(() -> new DataNotFoundException("Role assignment not found or already deleted."));

        assignment.setIsDelete(true);
        assignment.setUpdatedBy(adminUserId);
        roleAssignmentRepository.save(assignment);
    }

    @Transactional
    public void expireOutdatedRoles() {
        Date today = new Date(); // represents current system date

        List<CustomerRoleAssignment> expiredAssignments =
            roleAssignmentRepository.findExpiredActiveAssignments(today);

        for (CustomerRoleAssignment assignment : expiredAssignments) {
            assignment.setIsActive(false);
        }

        roleAssignmentRepository.saveAll(expiredAssignments);
    }






}

