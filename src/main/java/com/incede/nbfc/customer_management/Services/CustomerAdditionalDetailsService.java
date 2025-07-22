package com.incede.nbfc.customer_management.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalDetailsDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.MasterDataClient;
import com.incede.nbfc.customer_management.FeignClients.OrganisationClients;
import com.incede.nbfc.customer_management.FeignClientsModels.DesignationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.NationalityDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.OccupationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.StaffDto;
import com.incede.nbfc.customer_management.Models.CustomerAdditionalDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerAdditionalDetailsRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;

import jakarta.transaction.Transactional;

@Service
public class CustomerAdditionalDetailsService {

    private final CustomerAdditionalDetailsRepository customerAdditionalDetailsRepository;
    private final MasterDataClient masterDataClient;
    private final OrganisationClients organisationClents;

    public CustomerAdditionalDetailsService(
            CustomerAdditionalDetailsRepository customerAdditionalDetailsRepository,
            MasterDataClient masterDataClient,
            OrganisationClients organisationClients) {
        this.customerAdditionalDetailsRepository = customerAdditionalDetailsRepository;
        this.masterDataClient = masterDataClient;
        this.organisationClents = organisationClients;
    }

    @Transactional
    public CustomerAdditionalDetails save(CustomerAdditionalDetailsDTO dto) {
        try {
            CustomerAdditionalDetails entity = toEntity(dto);
            return customerAdditionalDetailsRepository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Failed to save customer additional details.");
        }
    }

    @Transactional
    public void updateDetails(Integer customerId, CustomerAdditionalDetailsDTO dto) {
        CustomerAdditionalDetails existing = customerAdditionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer details not found or deleted."));

        try {
            existing.setNationalityId(dto.getNationalityId());
            existing.setOccupationId(dto.getOccupationId());
            existing.setSalary(dto.getSalary());
            existing.setDesignationId(dto.getDesignationId());
            existing.setEmployerDetails(dto.getEmployerDetails());
            existing.setReferralSource(dto.getReferralSource());
            existing.setCanvasserEmployeeId(dto.getCanvasserEmployeeId());
            existing.setReferCustomerId(dto.getReferCustomerId());
            existing.setSourceOfIncome(dto.getSourceOfIncome());
            existing.setOwnAnyAssets(dto.getOwnAnyAssets());
            existing.setPepStatus(dto.getPepStatus());
            existing.setPepCategory(dto.getPepCategory());
            existing.setPepRelationshipType(dto.getPepRelationshipType());
            existing.setPepVerificationSource(dto.getPepVerificationSource());
            existing.setUpdatedBy(dto.getUpdatedBy());
            customerAdditionalDetailsRepository.save(existing);
        } catch (Exception e) {
            throw new BusinessException("Unable to update additional details.");
        }
    }

    @Transactional
    public void softDelete(Integer customerId, Integer userId) {
        CustomerAdditionalDetails existing = customerAdditionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer details not found or already deleted."));

        try {
            existing.setIsDelete(true);
            existing.setUpdatedBy(userId);
            customerAdditionalDetailsRepository.save(existing);
        } catch (Exception e) {
            throw new BusinessException("Unable to delete additional details.");
        }
    }

    public CustomerAdditionalDetailsDTO getDetails(Integer customerId) {
        CustomerAdditionalDetails entity = customerAdditionalDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer additional details not found."));
        return toDTO(entity);
    }

    public List<CustomerAdditionalDetailsDTO> getAllDetails() {
        try {
            List<CustomerAdditionalDetails> entities = customerAdditionalDetailsRepository.findByIsDeleteFalse();
            return entities.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Failed to fetch customer additional details.");
        }
    }

    public List<CustomerAdditionalDetailsDTO> filterDetails(
            Integer nationalityId,
            Integer occupationId,
            Integer designationId,
            Integer referCustomerId,
            Integer canvasserEmployeeId) {
        List<CustomerAdditionalDetails> all = customerAdditionalDetailsRepository.findByIsDeleteFalse();

        return all.stream()
                .filter(d -> nationalityId == null || nationalityId.equals(d.getNationalityId()))
                .filter(d -> occupationId == null || occupationId.equals(d.getOccupationId()))
                .filter(d -> designationId == null || designationId.equals(d.getDesignationId()))
                .filter(d -> referCustomerId == null || referCustomerId.equals(d.getReferCustomerId()))
                .filter(d -> canvasserEmployeeId == null || canvasserEmployeeId.equals(d.getCanvasserEmployeeId()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<NationalityDTO> getAllNationalities() {
        ResponseWrapper<List<NationalityDTO>> response = masterDataClient.getAllNationality();
        if (response == null || response.getData() == null) {
            throw new BadRequestException("Failed to fetch nationalities from Master Data.");
        }
        return response.getData();
    }

    public List<OccupationDTO> getAllOccupations() {
        ResponseWrapper<List<OccupationDTO>> response = masterDataClient.getAllOccupationalDetails();
        if (response == null || response.getData() == null) {
            throw new BadRequestException("Failed to fetch occupations from Master Data.");
        }
        return response.getData();
    }

    public List<StaffDto> getAllStaffs() {
        ResponseWrapper<List<StaffDto>> response = organisationClents.getAllStaffByTenant();
        if (response == null || response.getData() == null) {
            throw new BadRequestException("Failed to fetch staff data from Organisation Service.");
        }
        return response.getData();
    }

    public List<DesignationDTO> getAllDesignations() {
        ResponseWrapper<List<DesignationDTO>> response = masterDataClient.getAllActiveDesignation();
        if (response == null || response.getData() == null) {
            throw new BadRequestException("Failed to fetch designations from Master Data.");
        }
        return response.getData();
    }

    private CustomerAdditionalDetails toEntity(CustomerAdditionalDetailsDTO dto) {
        CustomerAdditionalDetails entity = new CustomerAdditionalDetails();
        entity.setCustomerId(dto.getCustomerId());
        entity.setNationalityId(dto.getNationalityId());
        entity.setOccupationId(dto.getOccupationId());
        entity.setSalary(dto.getSalary());
        entity.setDesignationId(dto.getDesignationId());
        entity.setEmployerDetails(dto.getEmployerDetails());
        entity.setReferralSource(dto.getReferralSource());
        entity.setCanvasserEmployeeId(dto.getCanvasserEmployeeId());
        entity.setReferCustomerId(dto.getReferCustomerId());
        entity.setSourceOfIncome(dto.getSourceOfIncome());
        entity.setOwnAnyAssets(dto.getOwnAnyAssets());
        entity.setPepStatus(dto.getPepStatus());
        entity.setPepCategory(dto.getPepCategory());
        entity.setPepRelationshipType(dto.getPepRelationshipType());
        entity.setPepVerificationSource(dto.getPepVerificationSource());
        entity.setCreatedBy(dto.getCreatedBy());
        return entity;
    }

    private CustomerAdditionalDetailsDTO toDTO(CustomerAdditionalDetails entity) {
        CustomerAdditionalDetailsDTO dto = new CustomerAdditionalDetailsDTO(
                entity.getCustomerId(),
                entity.getNationalityId(),
                entity.getOccupationId(),
                entity.getSalary(),
                entity.getDesignationId(),
                entity.getEmployerDetails(),
                entity.getReferralSource(),
                entity.getCanvasserEmployeeId(),
                entity.getReferCustomerId(),
                entity.getSourceOfIncome(),
                entity.getOwnAnyAssets(),
                entity.getPepStatus(),
                entity.getPepCategory(),
                entity.getPepRelationshipType(),
                entity.getPepVerificationSource(),
                entity.getCreatedBy()
        );

        List<NationalityDTO> nationalities = getAllNationalities();
        List<OccupationDTO> occupations = getAllOccupations();
        List<DesignationDTO> designations = getAllDesignations();

        dto.setNationality(
                nationalities.stream()
                        .filter(n -> n.getNationalityId().equals(entity.getNationalityId()))
                        .map(NationalityDTO::getNationality)
                        .findFirst().orElse(null)
        );

        dto.setOccupationName(
                occupations.stream()
                        .filter(o -> o.getOccupationId().equals(entity.getOccupationId()))
                        .map(OccupationDTO::getOccupationName)
                        .findFirst().orElse(null)
        );

        dto.setDesignationName(
                designations.stream()
                        .filter(d -> d.getDesignationId().equals(entity.getDesignationId()))
                        .map(DesignationDTO::getDesignationName)
                        .findFirst().orElse(null)
        );

        return dto;
    }
}
