package com.incede.nbfc.customer_management.Services;

 import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

 import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerAddressesDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerAddressesModel;
import com.incede.nbfc.customer_management.Repositories.CustomerAddressesRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class CustomerAddressesService {

	private final CustomerAddressesRepository customerAddressesRepository;
	
	public CustomerAddressesService(CustomerAddressesRepository customerAddressesRepository ) {
		this.customerAddressesRepository=customerAddressesRepository;
	}
	
	@Transactional
	public Integer createCustomerAddressDetails(Integer customerId, CustomerAddressesDto customerDto){
		try {
			CustomerAddressesModel addressModel = new CustomerAddressesModel();
			CustomerAddressesModel createdData = createdCustomerAddressDetails(addressModel,customerDto);
			String ver_addressType = createdData.getAddressType();
			if(!ver_addressType.equalsIgnoreCase("Present") && !ver_addressType.equalsIgnoreCase("Permanent")) {
				throw new BusinessException( "Address tye should be in 'Permanent' or 'Present'");
			}
			String ver_pincode =  createdData.getPincode();
			if(!ValidatePincode(ver_pincode)) {
				throw new BusinessException("In Valid Pincode");
			}
			
			CustomerAddressesModel resultEntity =customerAddressesRepository.save(addressModel);
			CustomerAddressesDto createdId = convertToDto(resultEntity);
			return createdId.getAddressId();
 		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional
	public Integer updatedCustomerAddressDetails(Integer customerId, Integer addressId, CustomerAddressesDto customerDto ) {
		try {
			CustomerAddressesModel existingModel = customerAddressesRepository.findByCustomerIdAndAddressId(customerId,addressId);
			if(existingModel == null) {
				throw new BusinessException("Address DetailsNot Found !!");
			}
			existingModel.setCustomerId(customerDto.getCustomerId() != null ? customerDto.getCustomerId() : existingModel.getCustomerId());
			existingModel.setAddressType(customerDto.getAddressType() != null ? customerDto.getAddressType() : existingModel.getAddressType());
			existingModel.setDoorNumber(customerDto.getDoorNumber() != null ? customerDto.getDoorNumber() : existingModel.getDoorNumber());
			existingModel.setAddressLineOne(customerDto.getAddressLineOne() != null ? customerDto.getAddressLineOne() : existingModel.getAddressLineOne());
			existingModel.setAddressLineTwo(customerDto.getAddressLineTwo() != null ? customerDto.getAddressLineTwo() : existingModel.getAddressLineTwo());
			existingModel.setLandMark(customerDto.getLandMark() != null ? customerDto.getLandMark() : existingModel.getLandMark());
			existingModel.setPlaceName(customerDto.getPlaceName() != null ? customerDto.getPlaceName() : existingModel.getPlaceName());
			existingModel.setCity(customerDto.getCity() != null ? customerDto.getCity() : existingModel.getCity());
			existingModel.setDistrict(customerDto.getDistrict() != null ? customerDto.getDistrict() : existingModel.getDistrict());
			existingModel.setStateName(customerDto.getStateName() != null ? customerDto.getStateName() : existingModel.getStateName());
			existingModel.setCountry(customerDto.getCountry() != null ? customerDto.getCountry() : existingModel.getCountry());
			existingModel.setPincode(customerDto.getPincode() != null ? customerDto.getPincode() : existingModel.getPincode());
			existingModel.setIsActive(customerDto.getIsActive() != null ? customerDto.getIsActive() : existingModel.getIsActive());
			existingModel.setIdentity(customerDto.getIdentity() != null ? customerDto.getIdentity() : existingModel.getIdentity());
 			existingModel.setUpdatedBy(customerDto.getUpdatedBy() != null ? customerDto.getUpdatedBy() : existingModel.getUpdatedBy());
 			
 			CustomerAddressesModel existingEntity=customerAddressesRepository.save(existingModel);
 			CustomerAddressesDto UpdaedDataId  = convertToDto(existingEntity);
 			return UpdaedDataId.getAddressId();
  		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	public CustomerAddressesModel createdCustomerAddressDetails(CustomerAddressesModel  addressModel, CustomerAddressesDto customerDto ) {
	
		addressModel.setCustomerId(ValidateFieldInteger(customerDto.getCustomerId(),"customer id"));
		addressModel.setAddressType(ValidateFielsString(customerDto.getAddressType(), "Address type"));
		addressModel.setDoorNumber(customerDto.getDoorNumber());
		addressModel.setAddressLineOne(customerDto.getAddressLineOne());
		addressModel.setAddressLineTwo(customerDto.getAddressLineTwo());
		addressModel.setLandMark(customerDto.getLandMark());
		addressModel.setPlaceName(customerDto.getPlaceName());
		addressModel.setCity(customerDto.getCity());
		addressModel.setDistrict(customerDto.getDistrict());
		addressModel.setStateName(customerDto.getStateName());
		addressModel.setCountry(customerDto.getCountry() !=null ? customerDto.getCountry() :1);
		addressModel.setPincode(ValidateFielsString(customerDto.getPincode(),"Pincode"));
		addressModel.setIsActive(true);
		addressModel.setIdentity(UUID.randomUUID());
		addressModel.setCreatedBy(ValidateFieldInteger(customerDto.getCreatedBy(), "created By"));
		addressModel.setUpdatedBy(customerDto.getUpdatedBy() !=null ? customerDto.getUpdatedBy() : customerDto.getCreatedBy());
		
		return addressModel;
	}
	
		
 
	
	@Transactional
	public String softDeleteCustomerAddressDetails(Integer customerId, Integer addressId) {
		try {
			CustomerAddressesModel AddressDetails = customerAddressesRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(customerId,addressId);
			if(AddressDetails == null) {
				throw new BusinessException("No customer details found !!");
			}
			AddressDetails.setIsDelete(true);
			AddressDetails.setUpdatedBy(customerId);
			return addressId +"customer details deleted successfully";
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = true)
	public CustomerAddressesDto getCustomerAddressDetailsByCustomerIdAndAddressId(Integer customerId, Integer addressId){
		try {
			CustomerAddressesModel AddressDetails = customerAddressesRepository.findByCustomerIdAndAddressIdAndIsDeleteFalse(customerId,addressId);
			if(AddressDetails == null) {
				throw new BusinessException("details not forund");
			}
			return convertToDto(AddressDetails);
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = true)
	public Page<CustomerAddressesDto> getAllCustomerAddreddDetails(Integer customerId , int page, int size){
		try {
		    Pageable pageable = PageRequest.of(page, size);

		    Page<CustomerAddressesModel> ListOfAddressDetails = customerAddressesRepository.findByCustomerId(customerId , pageable);
			if(ListOfAddressDetails.isEmpty()) {
				return new PageImpl<>(Collections.emptyList(), pageable, 0);
			}
			
			List<CustomerAddressesDto> addressDtos =  ListOfAddressDetails.stream()
		            .map(this::convertToDto)
		            .collect(Collectors.toList());
		    
			return new PageImpl<>(addressDtos, pageable, ListOfAddressDetails.getTotalElements());
			
		}catch(BusinessException e) {
			throw e;
		}
	}
	
	
	public CustomerAddressesDto convertToDto(CustomerAddressesModel addressModel) {
	    CustomerAddressesDto addressDto = new CustomerAddressesDto();
	    
	    addressDto.setAddressId(addressModel.getAddressId());
	    addressDto.setCustomerId(addressModel.getCustomerId());
	    addressDto.setAddressType(addressModel.getAddressType());
	    addressDto.setDoorNumber(addressModel.getDoorNumber());
	    addressDto.setAddressLineOne(addressModel.getAddressLineOne());
	    addressDto.setAddressLineTwo(addressModel.getAddressLineTwo());
	    addressDto.setLandMark(addressModel.getLandMark());
	    addressDto.setPlaceName(addressModel.getPlaceName());
	    addressDto.setCity(addressModel.getCity());
	    addressDto.setDistrict(addressModel.getDistrict());
	    addressDto.setStateName(addressModel.getStateName());
	    addressDto.setCountry(addressModel.getCountry());
	    addressDto.setPincode(addressModel.getPincode());
	    addressDto.setIsActive(addressModel.getIsActive());
	    addressDto.setIdentity(addressModel.getIdentity());
 	    addressDto.setCreatedBy(addressModel.getCreatedBy());
	    addressDto.setUpdatedBy(addressModel.getUpdatedBy());
	    addressDto.setCreatedAt(addressModel.getCreatedAt());
	    addressDto.setUpdatedAt(addressModel.getUpdatedAt());
	    addressDto.setIsDelete(addressModel.getIsDelete());

	    return addressDto;
	}
	
	
	public Integer ValidateFieldInteger(Integer integerfield, String name) {
 		if(integerfield == null ) {
			throw new BusinessException(name + "cannot be null or empty");
		}
		try {
			return  integerfield;
		}catch(NumberFormatException e) {
			throw new BusinessException(integerfield + "must be a valid Integer");
		}
	}
	

	public  String ValidateFielsString(String stringField, String name) {
		if(stringField == null || stringField.trim().isEmpty()) {
			throw new BusinessException(name +" cannot be null or empty");
		}
		stringField= stringField.trim().replaceAll("\\s{2,}", " ");
		return  stringField;
	}
	
	public boolean ValidatePincode(String pincCode) {
		String  inputfield = pincCode.trim().toUpperCase();
		String pincodeRGX = "^^[1-9][0-9]{5}$";
		return inputfield.matches(pincodeRGX);
	}

}
