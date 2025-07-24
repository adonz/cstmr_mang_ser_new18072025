package com.incede.nbfc.customer_management.Junitn;

import com.incede.nbfc.customer_management.DTOs.CustomerRoleAssignmentDTO;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.UserClient;
import com.incede.nbfc.customer_management.FeignClientsModels.RoleDTO;
import com.incede.nbfc.customer_management.Models.CustomerRoleAssignment;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerRoleAssignmentRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerRoleAssignmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CustomerRoleAssignmentServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private UserClient roleClient;
    @Mock private CustomerRoleAssignmentRepository roleAssignmentRepository;

    @InjectMocks private CustomerRoleAssignmentService service;

    private CustomerRoleAssignmentDTO dto;
    private CustomerRoleAssignment entity;
    private RoleDTO roleDto;

    @BeforeEach
    void setup() {
        dto = new CustomerRoleAssignmentDTO();
        dto.setCustomerId(1);
        dto.setRoleId(2);
        dto.setAssignedBy(3);
        dto.setCreatedBy(4);

        entity = new CustomerRoleAssignment();
        entity.setRoleAssignmentId(100);
        entity.setCustomerId(1);
        entity.setRoleId(2);
        entity.setIsActive(true);
        entity.setIsDelete(false);

        roleDto = new RoleDTO();
        roleDto.setRoleId(2);
        roleDto.setIsActive(true);
    }

    @Test
    void testAssignRoleSuccess() {
        when(customerRepository.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(roleClient.getRoleById(2)).thenReturn(ResponseWrapper.success(roleDto));
        when(roleAssignmentRepository.save(any())).thenReturn(entity);

        CustomerRoleAssignmentDTO result = service.assignRole(dto);

        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
        verify(roleAssignmentRepository).save(any());
    }

    @Test
    void testAssignRoleInvalidCustomer() {
        when(customerRepository.existsByCustomerIdAndIsDeleteFalse(999)).thenReturn(false);
        dto.setCustomerId(999);

        assertThrows(DataNotFoundException.class, () -> service.assignRole(dto));
    }

    @Test
    void testAssignRoleInactiveRole() {
        roleDto.setIsActive(false);
        when(customerRepository.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(roleClient.getRoleById(2)).thenReturn(ResponseWrapper.success(roleDto));

        assertThrows(DataNotFoundException.class, () -> service.assignRole(dto));
    }

    @Test
    void testAssignRoleNoRoleReturned() {
        when(customerRepository.existsByCustomerIdAndIsDeleteFalse(1)).thenReturn(true);
        when(roleClient.getRoleById(2)).thenReturn(ResponseWrapper.success(null));

        assertThrows(DataNotFoundException.class, () -> service.assignRole(dto));
    }

    @Test
    void testGetActiveRolesByCustomerId() {
        when(roleAssignmentRepository.findByCustomerId(1)).thenReturn(List.of(entity));

        List<CustomerRoleAssignmentDTO> result = service.getActiveRolesByCustomerId(1);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getRoleId());
    }

    @Test
    void testDeactivateRoleAssignmentSuccess() {
        when(roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(100)).thenReturn(Optional.of(entity));
        when(roleAssignmentRepository.save(any())).thenReturn(entity);

        service.deactivateRoleAssignment(100, 5);

        assertFalse(entity.getIsActive());
        assertEquals(5, entity.getUpdatedBy());
    }

    @Test
    void testDeactivateRoleAssignmentNotFound() {
        when(roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(999)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.deactivateRoleAssignment(999, 9));
    }

    @Test
    void testSoftDeleteRoleAssignmentSuccess() {
        when(roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(100)).thenReturn(Optional.of(entity));
        when(roleAssignmentRepository.save(any())).thenReturn(entity);

        service.softDeleteRoleAssignment(100, 6);

        assertTrue(entity.getIsDelete());
        assertEquals(6, entity.getUpdatedBy());
    }

    @Test
    void testSoftDeleteRoleAssignmentNotFound() {
        when(roleAssignmentRepository.findByRoleAssignmentIdAndIsDeleteFalse(888)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.softDeleteRoleAssignment(888, 10));
    }

    @Test
    void testExpireOutdatedRoles() {
        when(roleAssignmentRepository.findExpiredActiveAssignments(any())).thenReturn(List.of(entity));

        service.expireOutdatedRoles();

        assertFalse(entity.getIsActive());
        verify(roleAssignmentRepository).saveAll(List.of(entity));
    }
}
