package com.incede.nbfc.customer_management.Junitn;



import com.incede.nbfc.customer_management.DTOs.*;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.MasterDataClient;
import com.incede.nbfc.customer_management.FeignClients.OrganisationClients;
import com.incede.nbfc.customer_management.FeignClientsModels.DesignationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.NationalityDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.OccupationDTO;
import com.incede.nbfc.customer_management.Models.CustomerAdditionalDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerAdditionalDetailsRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerAdditionalDetailsService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAdditionalDetailsServiceTest {

    @Mock private CustomerAdditionalDetailsRepository repository;
    @Mock private MasterDataClient masterDataClient;
    @Mock private OrganisationClients organisationClients;

    @InjectMocks private CustomerAdditionalDetailsService service;

    private CustomerAdditionalDetailsDTO dto;
    private CustomerAdditionalDetails entity;

    @BeforeEach
    void setUp() {
        dto = new CustomerAdditionalDetailsDTO(1, 10, 20, 75000, 30,
                "Employer Ltd", "Referral", 400, 500, "Salary",
                true, true, "PEP Type", "PEP Relation", "Verification", 99);

        entity = new CustomerAdditionalDetails();
        entity.setCustomerId(1);
        entity.setNationalityId(10);
        entity.setOccupationId(20);
        entity.setSalary(75000);
        entity.setDesignationId(30);
        entity.setEmployerDetails("Employer Ltd");
        entity.setReferralSource("Referral");
        entity.setCanvasserEmployeeId(400);
        entity.setReferCustomerId(500);
        entity.setSourceOfIncome("Salary");
        entity.setOwnAnyAssets(true);
        entity.setPepStatus(true);
        entity.setPepCategory("PEP Type");
        entity.setPepRelationshipType("PEP Relation");
        entity.setPepVerificationSource("Verification");
        entity.setCreatedBy(99);
        entity.setUpdatedBy(101);
        entity.setIsDelete(false);
    }

    @Test
    void testSaveSuccess() {
        when(repository.save(any())).thenReturn(entity);
        CustomerAdditionalDetails result = service.save(dto);

        assertNotNull(result);
        assertEquals("Employer Ltd", result.getEmployerDetails());
        verify(repository).save(any());
    }

    @Test
    void testUpdateDetailsSuccess() {
        when(repository.findByCustomerIdAndIsDeleteFalse(1)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        dto.setEmployerDetails("Updated Employer");
        service.updateDetails(1, dto);

        assertEquals("Updated Employer", entity.getEmployerDetails());
        verify(repository).save(entity);
    }

    @Test
    void testUpdateDetailsNotFound() {
        when(repository.findByCustomerIdAndIsDeleteFalse(999)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.updateDetails(999, dto));
    }

    @Test
    void testSoftDeleteSuccess() {
        when(repository.findByCustomerIdAndIsDeleteFalse(1)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        service.softDelete(1, 777);

        assertTrue(entity.getIsDelete());
        assertEquals(777, entity.getUpdatedBy());
        verify(repository).save(entity);
    }

    @Test
    void testSoftDeleteNotFound() {
        when(repository.findByCustomerIdAndIsDeleteFalse(888)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.softDelete(888, 123));
    }

    @Test
    void testGetDetailsSuccess() {
        when(repository.findByCustomerIdAndIsDeleteFalse(1)).thenReturn(Optional.of(entity));

        List<NationalityDTO> natList = List.of(new NationalityDTO(10, "India"));
        List<OccupationDTO> occList = List.of(new OccupationDTO(20, "Engineer"));
        List<DesignationDTO> desigList = List.of(new DesignationDTO(30, "Manager"));

        when(masterDataClient.getAllNationality()).thenReturn(ResponseWrapper.success(natList));
        when(masterDataClient.getAllOccupationalDetails()).thenReturn(ResponseWrapper.success(occList));
        when(masterDataClient.getAllActiveDesignation()).thenReturn(ResponseWrapper.success(desigList));

        CustomerAdditionalDetailsDTO result = service.getDetails(1);

        assertEquals("India", result.getNationality());
        assertEquals("Engineer", result.getOccupationName());
        assertEquals("Manager", result.getDesignationName());
    }

    @Test
    void testGetDetailsNotFound() {
        when(repository.findByCustomerIdAndIsDeleteFalse(2)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getDetails(2));
    }

    @Test
    void testGetAllDetails() {
        when(repository.findByIsDeleteFalse()).thenReturn(List.of(entity));
        when(masterDataClient.getAllNationality()).thenReturn(ResponseWrapper.success(new ArrayList<>()));
        when(masterDataClient.getAllOccupationalDetails()).thenReturn(ResponseWrapper.success(new ArrayList<>()));
        when(masterDataClient.getAllActiveDesignation()).thenReturn(ResponseWrapper.success(new ArrayList<>()));

        List<CustomerAdditionalDetailsDTO> result = service.getAllDetails();

        assertEquals(1, result.size());
    }

    @Test
    void testFilterDetailsSuccess() {
        when(repository.findByIsDeleteFalse()).thenReturn(List.of(entity));
        when(masterDataClient.getAllNationality()).thenReturn(ResponseWrapper.success(List.of(new NationalityDTO(10, "India"))));
        when(masterDataClient.getAllOccupationalDetails()).thenReturn(ResponseWrapper.success(List.of(new OccupationDTO(20, "Engineer"))));
        when(masterDataClient.getAllActiveDesignation()).thenReturn(ResponseWrapper.success(List.of(new DesignationDTO(30, "Manager"))));

        List<CustomerAdditionalDetailsDTO> filtered = service.filterDetails(10, 20, 30, 500, 400);
        assertEquals(1, filtered.size());
    }

    @Test
    void testGetAllNationalities_Failure() {
        when(masterDataClient.getAllNationality()).thenReturn(ResponseWrapper.failure("Error", null));
        assertThrows(BadRequestException.class, service::getAllNationalities);
    }

    @Test
    void testGetAllOccupations_Failure() {
        when(masterDataClient.getAllOccupationalDetails()).thenReturn(ResponseWrapper.failure("Error", null));
        assertThrows(BadRequestException.class, service::getAllOccupations);
    }

    @Test
    void testGetAllStaffs_Failure() {
        when(organisationClients.getAllStaffByTenant()).thenReturn(ResponseWrapper.failure("Error", null));
        assertThrows(BadRequestException.class, service::getAllStaffs);
    }

    @Test
    void testGetAllDesignations_Failure() {
        when(masterDataClient.getAllActiveDesignation()).thenReturn(ResponseWrapper.failure("Error", null));
        assertThrows(BadRequestException.class, service::getAllDesignations);
    }
}
