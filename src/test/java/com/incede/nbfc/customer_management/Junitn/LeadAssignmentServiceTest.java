package com.incede.nbfc.customer_management.Junitn;


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
import com.incede.nbfc.customer_management.Services.LeadAssignmentConfigService;
import com.incede.nbfc.customer_management.Services.LeadAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LeadAssignmentServiceTest {

    @Mock private LeadAssignmentRepository leadRepository;
    @Mock private LeadMasterRepository leadMasterRepository;
    @Mock private UserClient userClient;
    @Mock private LeadAssignmentConfigService configService;

    @InjectMocks private LeadAssignmentService service;

    private LeadAssignmentDTO dto;
    private LeadAssignment entity;

    @BeforeEach
    void setup() {
        dto = new LeadAssignmentDTO();
        dto.setLeadId(101);
        dto.setAssignedTo(1001);
        dto.setCreatedBy(500);

        entity = new LeadAssignment();
        entity.setLeadId(101);
        entity.setAssignedTo(1001);
        entity.setCreatedBy(500);
        entity.setAssignedOn(LocalDateTime.now());
        entity.setIsDelete(false);
    }

    @Test
    void testAssignLeadSuccessWhenMultipleAssignmentsAllowed() {
        when(configService.isMultiAssigneeAllowed()).thenReturn(true);
        when(leadRepository.save(any())).thenReturn(entity);

        LeadAssignmentDTO result = service.assignLead(dto);
        assertEquals(101, result.getLeadId());
        verify(leadRepository).save(any());
    }

    @Test
    void testAssignLeadFailsWhenAlreadyAssigned() {
        when(configService.isMultiAssigneeAllowed()).thenReturn(false);
        when(leadRepository.countByLeadIdAndDeletedFlagFalse(101)).thenReturn(1);

        assertThrows(ConflictException.class, () -> service.assignLead(dto));
    }

    @Test
    void testReassignLeadSuccess() {
        when(leadMasterRepository.existsByLeadIdAndIsDeleteFalse(101)).thenReturn(true);
        when(leadRepository.findByLeadIdAndIsDeleteFalse(101)).thenReturn(List.of(entity));
        when(userClient.isValidUser(2001)).thenReturn(ResponseWrapper.success(true));
        when(leadRepository.save(any())).thenReturn(entity);

        LeadAssignmentDTO result = service.reassignLead(101, 2001, 500);
        assertEquals(101, result.getLeadId());
        verify(leadRepository).save(any());
    }

    @Test
    void testReassignLeadFailsWhenLeadNotFound() {
        when(leadMasterRepository.existsByLeadIdAndIsDeleteFalse(999)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.reassignLead(999, 2001, 500));
    }

    @Test
    void testReassignLeadFailsWhenNewAssigneeIsInvalid() {
        when(leadMasterRepository.existsByLeadIdAndIsDeleteFalse(101)).thenReturn(true);
        when(leadRepository.findByLeadIdAndIsDeleteFalse(101)).thenReturn(List.of(entity));
        when(userClient.isValidUser(2001)).thenReturn(ResponseWrapper.success(false));

        assertThrows(DataNotFoundException.class, () -> service.reassignLead(101, 2001, 500));
    }

    @Test
    void testGetAssignmentHistorySuccess() {
        entity.setAssignedTo(1001);
        when(leadRepository.findAllByLeadIdOrderByAssignedOnDesc(101)).thenReturn(List.of(entity));
        when(userClient.getUserDetails(1001)).thenReturn(ResponseWrapper.success(new UserInfoDTO(1, "rajiv.kumar", "Rajiv")
));

        List<LeadAssignmentHistoryDTO> result = service.getAssignmentHistory(101);
        assertEquals(1, result.size());
        assertEquals("Rajiv", result.get(0).getAssignedToName());
    }

    @Test
    void testGetAssignmentHistoryFailsWhenEmpty() {
        when(leadRepository.findAllByLeadIdOrderByAssignedOnDesc(1234)).thenReturn(Collections.emptyList());
        assertThrows(DataNotFoundException.class, () -> service.getAssignmentHistory(1234));
    }

    @Test
    void testSoftDeleteAssignmentSuccess() {
        entity.setIsDelete(false);
        when(leadRepository.findById(10)).thenReturn(Optional.of(entity));

        service.softDeleteAssignment(10, 777);

        assertTrue(entity.getIsDelete());
        assertEquals(777, entity.getUpdatedBy());
        verify(leadRepository).save(entity);
    }

    @Test
    void testSoftDeleteFailsIfAlreadyDeleted() {
        entity.setIsDelete(true);
        when(leadRepository.findById(10)).thenReturn(Optional.of(entity));
        assertThrows(ConflictException.class, () -> service.softDeleteAssignment(10, 999));
    }

    @Test
    void testSoftDeleteFailsIfAssignmentNotFound() {
        when(leadRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.softDeleteAssignment(999, 888));
    }
}
