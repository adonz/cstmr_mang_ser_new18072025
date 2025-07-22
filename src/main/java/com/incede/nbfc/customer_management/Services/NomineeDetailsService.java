package com.incede.nbfc.customer_management.Services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incede.nbfc.customer_management.DTOs.NomineeDetailsDTO;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Exceptions.ConflictException.ConflictException;
import com.incede.nbfc.customer_management.Exceptions.DataNotFoundException.DataNotFoundException;
import com.incede.nbfc.customer_management.Models.CustomerAddressesModel;
import com.incede.nbfc.customer_management.Models.NomineeDetails;
import com.incede.nbfc.customer_management.Repositories.CustomerAddressesRepository;
import com.incede.nbfc.customer_management.Repositories.CustomerRepository;
import com.incede.nbfc.customer_management.Repositories.NomineeDetailsRepository;

import jakarta.transaction.Transactional;

@Service
public class NomineeDetailsService {

    @Autowired
    private NomineeDetailsRepository nomineeDetailsRepository;

    @Autowired
    private CustomerAddressesRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public NomineeDetailsDTO addNominee(NomineeDetailsDTO dto) {
        boolean isValidCustomer = customerRepository.existsByCustomerIdAndIsDeleteFalse(dto.getCustomerId());
        if (!isValidCustomer) {
            throw new DataNotFoundException("Customer is invalid or inactive.");
        }

        if (Boolean.TRUE.equals(dto.getIsMinor()) &&
            (dto.getGuardianName() == null || dto.getGuardianName().trim().isEmpty())) {
            throw new BusinessException("Guardian name is required for minor nominee.");
        }

        // 🛡️ Safeguard against null from SUM query
        BigDecimal existingShare = nomineeDetailsRepository.getTotalShareByCustomerId(dto.getCustomerId());
        if (existingShare == null) {
            existingShare = BigDecimal.ZERO;
        }

        BigDecimal nomineeShare = dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.ZERO;
        BigDecimal proposedTotal = existingShare.add(nomineeShare);
        if (proposedTotal.compareTo(new BigDecimal("100.00")) > 0) {
            throw new ConflictException("Total nominee share cannot exceed 100%.");
        }

        if (Boolean.TRUE.equals(dto.getIsSameAddress())) {
            CustomerAddressesModel addr = customerAddressRepository.findByCustomerIdAndAddressTypeAndIsDeleteFalse(
                dto.getCustomerId(), "Permanant")
                .orElseThrow(() -> new DataNotFoundException("Customer address not found for inheritance."));

            dto.setHouseNumber(addr.getDoorNumber());
            dto.setAddressLine1(addr.getAddressLineOne());
            dto.setAddressLine2(addr.getAddressLineTwo());
            dto.setLandmark(addr.getLandMark());
            dto.setPlaceName(addr.getPlaceName());
            dto.setCity(addr.getCity());
            dto.setDistrict(addr.getDistrict());
            dto.setStateName(addr.getStateName());
            dto.setCountry(addr.getCountry());
            dto.setPincode(addr.getPincode());
        }

        // Convert and persist
        NomineeDetails entity = toEntity(dto);
        NomineeDetails saved = nomineeDetailsRepository.save(entity);
        return toDTO(saved);
    }
    private NomineeDetails toEntity(NomineeDetailsDTO dto) {
        NomineeDetails entity = new NomineeDetails();
        entity.setCustomerId(dto.getCustomerId());
        entity.setFullName(dto.getFullName());
        entity.setRelationship(dto.getRelationship());
        entity.setDob(dto.getDob());
        entity.setContactNumber(dto.getContactNumber());
        entity.setIsSameAddress(dto.getIsSameAddress());
        entity.setHouseNumber(dto.getHouseNumber());
        entity.setAddressLine1(dto.getAddressLine1());
        entity.setAddressLine2(dto.getAddressLine2());
        entity.setLandmark(dto.getLandmark());
        entity.setPlaceName(dto.getPlaceName());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setStateName(dto.getStateName());
        entity.setCountry(dto.getCountry());
        entity.setPincode(dto.getPincode());
        entity.setPercentageShare(dto.getPercentageShare() != null ? dto.getPercentageShare() : new BigDecimal("100.00"));
        entity.setIsMinor(dto.getIsMinor());
        entity.setGuardianName(dto.getGuardianName());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setIdentity(UUID.randomUUID());
        return entity;
    }

    private NomineeDetailsDTO toDTO(NomineeDetails entity) {
        NomineeDetailsDTO dto = new NomineeDetailsDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    public List<Map<String, Object>> getActiveNominees(Integer customerId) {
        List<NomineeDetails> nominees = nomineeDetailsRepository.findByCustomerIdAndIsDeleteFalse(customerId);

        return nominees.stream().map(n -> {
            Map<String, Object> view = new HashMap<>();
            view.put("fullName", n.getFullName());
            view.put("relationship", n.getRelationship());
            view.put("contactNumber", n.getContactNumber());
            view.put("percentageShare", n.getPercentageShare());
            view.put("city", n.getCity());
            view.put("stateName", n.getStateName());
            view.put("isMinor", n.getIsMinor());
            view.put("guardianName", n.getGuardianName());
            return view;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void updateNominee(Integer nomineeId, NomineeDetailsDTO dto, Integer userId) {
        NomineeDetails nominee = nomineeDetailsRepository.findByNomineeIdAndIsDeleteFalse(nomineeId)
            .orElseThrow(() -> new DataNotFoundException("Nominee not found or already deleted."));

        BigDecimal otherShare = nomineeDetailsRepository.getTotalShareExcludingNominee(dto.getCustomerId(), nomineeId);
        BigDecimal proposedTotal = otherShare.add(dto.getPercentageShare() != null ? dto.getPercentageShare() : BigDecimal.ZERO);
        if (proposedTotal.compareTo(new BigDecimal("100.00")) > 0) {
            throw new ConflictException("Total nominee share cannot exceed 100%.");
        }
        if (Boolean.TRUE.equals(dto.getIsMinor()) &&
            (dto.getGuardianName() == null || dto.getGuardianName().trim().isEmpty())) {
            throw new BusinessException("Guardian name is required for minor nominee.");
        }
        nominee.setFullName(dto.getFullName());
        nominee.setDob(dto.getDob());
        nominee.setContactNumber(dto.getContactNumber());
        nominee.setPercentageShare(dto.getPercentageShare());
        nominee.setIsMinor(dto.getIsMinor());
        nominee.setGuardianName(Boolean.TRUE.equals(dto.getIsMinor()) ? dto.getGuardianName() : null);

        nominee.setUpdatedBy(userId);

        nomineeDetailsRepository.save(nominee);
    }
    
    @Transactional
    public void softDeleteNominee(Integer nomineeId, Integer userId) {
        NomineeDetails nominee = nomineeDetailsRepository.findByNomineeIdAndIsDeleteFalse(nomineeId)
            .orElseThrow(() -> new DataNotFoundException("Nominee not found or already deleted."));

        nominee.setIsDelete(true);
        nominee.setUpdatedBy(userId);

        nomineeDetailsRepository.save(nominee);
    }


}

