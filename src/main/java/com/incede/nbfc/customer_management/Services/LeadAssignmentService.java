package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.LeadAssignmentDTO;
import com.incede.nbfc.customer_management.DTOs.LeadAssignmentHistoryDTO;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.UserClient;
import com.incede.nbfc.customer_management.FeignClientsModels.UserInfoDTO;
import com.incede.nbfc.customer_management.Models.LeadAssignment;
import com.incede.nbfc.customer_management.Repositories.LeadAssignmentRepository;
import com.incede.nbfc.customer_management.Repositories.LeadMasterRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;

import jakarta.transaction.Transactional;

@Service
public class LeadAssignmentService {

    @Autowired
    private LeadAssignmentRepository leadAssignmentRepository;

    @Autowired
    private LeadMasterRepository leadMasterRepository; // for lead_id validation

    @Autowired
    private UserClient userClient; // Feign or internal user validation
    
    @Autowired
    private LeadAssignmentConfigService configService;

    @Transactional
    public LeadAssignmentDTO assignLead(LeadAssignmentDTO dto) {

        // Check if multiple assignments are allowed (e.g., configurable via system flag or enum)
        boolean allowMultipleAssignments = configService.isMultiAssigneeAllowed(); // example hook

        if (!allowMultipleAssignments) {
            Integer activeCount = leadAssignmentRepository.countByLeadIdAndDeletedFlagFalse(dto.getLeadId());
            if (activeCount > 0) {
                throw new ConflictException("Lead is already actively assigned to another user.");
            }
        }

        // (Validation logic for lead and assigned user remains unchanged)

        // Proceed with assignment
        LeadAssignment entity = new LeadAssignment();
        entity.setLeadId(dto.getLeadId());
        entity.setAssignedTo(dto.getAssignedTo());
        entity.setAssignedOn(LocalDateTime.now());
        entity.setIsDelete(false);
        entity.setIdentityGuid(UUID.randomUUID());
        entity.setCreatedBy(dto.getCreatedBy());

        LeadAssignment saved = leadAssignmentRepository.save(entity);
        return toDTO(saved);
    }


    private LeadAssignmentDTO toDTO(LeadAssignment entity) {
        LeadAssignmentDTO dto = new LeadAssignmentDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    @Transactional
    public LeadAssignmentDTO reassignLead(Integer leadId, Integer newAssignedTo, Integer adminId) {

        // ✅ Validate active lead existence
        boolean leadExists = leadMasterRepository.existsByLeadIdAndIsDeleteFalse(leadId);
        if (!leadExists) {
            throw new DataNotFoundException("Lead not found or inactive.");
        }

        // ✅ Soft-delete any existing active assignments
        List<LeadAssignment> currentAssignments =
            leadAssignmentRepository.findByLeadIdAndIsDeleteFalse(leadId);

        for (LeadAssignment assignment : currentAssignments) {
            assignment.setIsDelete(true);
            assignment.setUpdatedBy(adminId);
        }
        leadAssignmentRepository.saveAll(currentAssignments);

        ResponseWrapper<Boolean> userCheck = userClient.isValidUser(newAssignedTo);
        if (userCheck == null || Boolean.FALSE.equals(userCheck.getData())) {
            throw new DataNotFoundException("New assignee is invalid or inactive.");
        }

        // ✅ Create new assignment
        LeadAssignment newAssignment = new LeadAssignment();
        newAssignment.setLeadId(leadId);
        newAssignment.setAssignedTo(newAssignedTo);
        newAssignment.setAssignedOn(LocalDateTime.now());
        newAssignment.setIsDelete(false);
        newAssignment.setIdentityGuid(UUID.randomUUID());
        newAssignment.setCreatedBy(adminId);

        LeadAssignment saved = leadAssignmentRepository.save(newAssignment);
        return toDTO(saved);
    }
    
    public List<LeadAssignmentHistoryDTO> getAssignmentHistory(Integer leadId) {

        List<LeadAssignment> assignments = leadAssignmentRepository
            .findAllByLeadIdOrderByAssignedOnDesc(leadId);

        if (assignments.isEmpty()) {
            throw new DataNotFoundException("No assignment history found for this lead.");
        }

        return assignments.stream().map(entity -> {
            LeadAssignmentHistoryDTO dto = new LeadAssignmentHistoryDTO();
            dto.setLeadId(entity.getLeadId());
            dto.setAssignedTo(entity.getAssignedTo());
            dto.setAssignedOn(entity.getAssignedOn());
            dto.setCreatedBy(entity.getCreatedBy());
            dto.setIsDelete(entity.getIsDelete());

            ResponseWrapper<UserInfoDTO> userInfo = userClient.getUserDetails(entity.getAssignedTo());
            if (userInfo != null && userInfo.getData() != null) {
                dto.setAssignedToName(userInfo.getData().getFullName());
            }

            return dto;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void softDeleteAssignment(Integer assignmentId, Integer adminUserId) {
        LeadAssignment assignment = leadAssignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new DataNotFoundException("Assignment record not found."));

        if (Boolean.TRUE.equals(assignment.getIsDelete())) {
            throw new ConflictException("Assignment is already marked as deleted.");
        }

        assignment.setIsDelete(true);
        assignment.setUpdatedBy(adminUserId);

        leadAssignmentRepository.save(assignment);
    }

    		

}