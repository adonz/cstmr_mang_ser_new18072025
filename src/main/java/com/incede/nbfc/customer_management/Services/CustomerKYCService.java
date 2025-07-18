package com.incede.nbfc.customer_management.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incede.nbfc.customer_management.DTOs.CustomerKYCDTO;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.FeignClients.MasterDataClient;
import com.incede.nbfc.customer_management.FeignClientsModels.DocumentRequirementConfigurationDto;
import com.incede.nbfc.customer_management.Models.CustomerKYC;
import com.incede.nbfc.customer_management.Repositories.CustomerKYCRepository;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Utils.CommonUtils;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerKYCService {

    @Autowired
    private CustomerKYCRepository repository;

    @Autowired
    private MasterDataClient masterDataClient;

    public CustomerKYCDTO uploadKYCDocument(CustomerKYCDTO dto, MultipartFile document) {
        if (repository.existsByCustomerIdAndDocumentIdAndIdNumber(
                dto.getCustomerId(), dto.getDocumentId(), dto.getIdNumber())) {
            throw new ConflictException("This document number is already registered for the customer.");
        }

        if (document == null || document.isEmpty()) {
            throw new BadRequestException("Uploaded document is empty or missing.");
        }

        // 📂 Temporary local file save
        String uploadBasePath = "C:\\Customer management System\\customerManagementSystem\\uploads";
        File directory = new File(uploadBasePath);
        if (!directory.exists()) directory.mkdirs();

        String originalFilename = Paths.get(document.getOriginalFilename()).getFileName().toString();
        String filename = UUID.randomUUID() + "_" + originalFilename;
        String filepath = uploadBasePath + File.separator + filename;

        try {
            document.transferTo(new File(filepath));
        } catch (IOException | IllegalStateException e) {
            throw new BadRequestException("Failed to store uploaded file: " + e.getMessage());
        }

        Integer fakeDocumentFileId = 10001;

        CustomerKYC entity = new CustomerKYC();
        entity.setCustomerId(dto.getCustomerId());
        entity.setDocumentId(dto.getDocumentId());
        entity.setIdNumber(dto.getIdNumber());
        entity.setPlaceOfIssue(dto.getPlaceOfIssue());
        entity.setIssuingAuthority(dto.getIssuingAuthority());
        entity.setValidFrom(dto.getValidFrom());
        entity.setValidTo(dto.getValidTo());
        entity.setDocumentFileId(fakeDocumentFileId);
        entity.setIdentity(UUID.randomUUID());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setIsActive(true);
        entity.setIsVerified(false);

        CustomerKYC saved = repository.save(entity);
        return toDTO(saved);
    }

    public List<Map<String, Object>> getKYCForCustomer(Integer customerId, Integer documentId, Boolean isVerified) {
        var kycList = repository.findFilteredKYCs(customerId, documentId, isVerified);
        return kycList.stream().map(k -> {
            Map<String, Object> map = new HashMap<>();
            map.put("documentId", k.getDocumentId());
            map.put("idNumber", k.getIdNumber());
            map.put("placeOfIssue", k.getPlaceOfIssue()); // Add this line
            map.put("validFrom", k.getValidFrom());
            map.put("validTo", k.getValidTo());
            map.put("isVerified", k.getIsVerified());
            map.put("documentFileId", k.getDocumentFileId());
            return map;
        }).toList();
    }

    @Transactional
    public void verifyKYC(Integer kycId, Integer userId) {
        CustomerKYC kyc = repository.findActiveByKycId(kycId)
            .orElseThrow(() -> new DataNotFoundException("KYC document not found, inactive or deleted."));

        if (Boolean.TRUE.equals(kyc.getIsVerified())) {
            throw new ConflictException("KYC document is already verified.");
        }

        kyc.setIsVerified(true);
        kyc.setUpdatedBy(userId);
        repository.save(kyc);
    }

    @Transactional
    public void softDeleteKYC(Integer kycId, Integer userId) {
        CustomerKYC kyc = repository.findByKycIdAndIsDeleteFalse(kycId)
            .orElseThrow(() -> new DataNotFoundException("KYC record not found or already deleted."));

        kyc.setIsDelete(true);
        kyc.setIsActive(false);
        kyc.setUpdatedBy(userId);
        repository.save(kyc);
    }

    @Transactional
    public void updateKYCDocument(Integer kycId, Integer userId,
                                  MultipartFile newDocument,
                                  Date validFrom,
                                  Date validTo) {

        CustomerKYC kyc = repository.findByKycIdAndIsDeleteFalse(kycId)
                .orElseThrow(() -> new DataNotFoundException("KYC record not found or deleted."));

        if (newDocument != null && !newDocument.isEmpty()) {
            String uploadBasePath = "C:\\Customer management System\\customerManagementSystem\\uploads";
            File directory = new File(uploadBasePath);
            if (!directory.exists()) directory.mkdirs();

            String originalFilename = Paths.get(newDocument.getOriginalFilename()).getFileName().toString();
            String filename = UUID.randomUUID() + "_" + originalFilename;
            String filepath = uploadBasePath + File.separator + filename;

            try {
                newDocument.transferTo(new File(filepath));
            } catch (IOException | IllegalStateException e) {
                throw new BadRequestException("Error while updating KYC document file.");
            }
            kyc.setDocumentFileId(10001); // ⚠️ still using hardcoded file ID
        }

        if (validFrom != null) kyc.setValidFrom(validFrom);
        if (validTo != null) kyc.setValidTo(validTo);

        kyc.setUpdatedBy(userId);
        repository.save(kyc);
    }

    public List<DocumentRequirementConfigurationDto> getAllDocumentTypes() {
        ResponseWrapper<List<DocumentRequirementConfigurationDto>> response = masterDataClient.getAll();
        if (response == null || response.getData() == null) {
            throw new DataNotFoundException("Failed to fetch documentTypes from Master Data");
        }
        return response.getData();
    }

    private CustomerKYCDTO toDTO(CustomerKYC saved) {
        CustomerKYCDTO dto = new CustomerKYCDTO();
        dto.setKycId(saved.getKycId());
        dto.setCustomerId(saved.getCustomerId());
        dto.setDocumentId(saved.getDocumentId());
        dto.setIdNumber(saved.getIdNumber());
        dto.setPlaceOfIssue(saved.getPlaceOfIssue());
        dto.setIssuingAuthority(saved.getIssuingAuthority());
        dto.setDocumentFileId(saved.getDocumentFileId());
        dto.setIsVerified(saved.getIsVerified());
        dto.setIsActive(saved.getIsActive());
        dto.setIdentity(saved.getIdentity());
        dto.setCreatedBy(saved.getCreatedBy());

        dto.setCreatedAt(saved.getCreatedAt());
        dto.setUpdatedAt(saved.getUpdatedAt());
        dto.setValidFrom(saved.getValidFrom() != null ? CommonUtils.formatDate(saved.getValidFrom()) : null);
        dto.setValidTo(saved.getValidTo() != null ? CommonUtils.formatDate(saved.getValidTo()) : null);

        return dto;
    }
}
