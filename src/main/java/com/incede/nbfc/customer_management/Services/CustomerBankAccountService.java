package com.incede.nbfc.customer_management.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incede.nbfc.customer_management.DTOs.CustomerBankAccountDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerBankAccountModel;
import com.incede.nbfc.customer_management.Repositories.CustomerBankAccountRepository;


@Service
public class CustomerBankAccountService {
	
	private final CustomerBankAccountRepository customerAccountRepository;
	
	public  CustomerBankAccountService(CustomerBankAccountRepository customerAccountRepository) {
		
		this.customerAccountRepository=customerAccountRepository;
	}

	@Transactional
	public  Integer createOrUpdateCustomerAccountDetails( CustomerBankAccountDto accountDto) {
		CustomerBankAccountModel createdData;
		CustomerBankAccountModel customeraccount;
		CustomerBankAccountDto entityDto;
		try {			
  
				CustomerBankAccountModel customerBankAccountDetails =new CustomerBankAccountModel();	
				   createdData = createBankAccountDetails(customerBankAccountDetails,accountDto);
					CustomerBankAccountModel  bankAccount = customerAccountRepository.findByAccountNumber(accountDto.getAccountNumber());
					if(bankAccount !=null && bankAccount.getAccountNumber().equals(createdData.getAccountNumber())) {
						throw new BusinessException("Account number already existe, Account Number should be unique");
					}
					CustomerBankAccountModel   upiId = customerAccountRepository.findByUpiId(accountDto.getUpiId());
					if(upiId !=null && upiId.getUpiId().equals(accountDto.getUpiId())) {
						throw new BusinessException("Upi ID already exists in , Upi Id should be unique");
					}
					Integer accountType= createdData.getAccountType();
					if(accountType != 1 && accountType !=2) {
						throw new BusinessException("account type must be between 1 and 2");
					}
					if(!ValidateIFSCcode(createdData.getIfscCode())) {
						throw new BusinessException("Invalid Ifsc code format");
					}
				   
				
			CustomerBankAccountModel entityObject =customerAccountRepository.save(createdData);
			  entityDto= convertToDto(entityObject);
		}
		catch(BusinessException e) {
			throw e;
		}
		return  entityDto.getBankAccountId() ;
 		
	}
	
	
	public CustomerBankAccountModel createBankAccountDetails(CustomerBankAccountModel accountModel,CustomerBankAccountDto accountDto ) {
		accountModel.setBankId(ValidateFieldInteger(accountDto.getBankId(), "Bank Id"));
		accountModel.setCustomerId(ValidateFieldInteger(accountDto.getCustomerId(),"Customer Id"));
		accountModel.setAccountHolderName(ValidateFieldString(accountDto.getAccountHolderName(),  "Account Holder Name"));
		accountModel.setAccountStatus(ValidateFieldString(accountDto.getAccountStatus(),  "Account Status"));
		accountModel.setBankProofFileId(ValidateFieldInteger(accountDto.getBankProofFileId(),  "bank proof Id"));
		accountModel.setPaymentMode(ValidateFieldString(accountDto.getPaymentMode(),  "Payment Methode"));
		accountModel.setBankName(ValidateFieldString(accountDto.getBankName(),"Bank Name"));
		accountModel.setBranchName(ValidateFieldString(accountDto.getBranchName(), "Branch Name"));
		accountModel.setIfscCode(ValidateFieldString(accountDto.getIfscCode(), "Ifsc Code"));
		accountModel.setUpiId(accountDto.getUpiId());
		accountModel.setCreatedBy(ValidateFieldInteger(accountDto.getCreatedBy(), "Created By"));
		accountModel.setAccountType(ValidateFieldInteger(accountDto.getAccountType(),"Account Type"));
		accountModel.setAccountNumber(ValidateFieldString(accountDto.getAccountNumber(),"Account Number"));
		accountModel.setIdentity( UUID.randomUUID());
		accountModel.setIsActive(accountDto.getIsActive()!= null ? accountDto.getIsActive():true);
		accountModel.setIsPrimary(ValidateIsPrimary(accountDto.getCustomerId()));
		
		return accountModel;

	}
	
	@Transactional
	public  Integer updateBankAccountDeatils(CustomerBankAccountDto accountDto) {
		try {
			CustomerBankAccountModel accountModel = customerAccountRepository.findByBankAccountId(accountDto.getBankAccountId());
			if(accountModel ==null) {
				throw new BusinessException("details not found for id :"+ accountDto.getBankAccountId());
			}
			accountModel.setUpdatedBy(ValidateFieldInteger(accountDto.getUpdatedBy(),"Updated By"));
			accountModel.setBankName(accountDto.getBankName() !=null? accountDto.getBankName() :accountModel.getBankName());
			accountModel.setBranchName( accountDto.getBranchName() !=null?  accountDto.getBranchName():accountModel.getBranchName());
			accountModel.setIfscCode( accountDto.getIfscCode() !=null?accountDto.getIfscCode():accountModel.getIfscCode());
			accountModel.setUpiId(accountDto.getUpiId()!=null?accountDto.getUpiId():accountModel.getUpiId());
			accountModel.setIsActive(accountDto.getIsActive()!= null ? accountDto.getIsActive():accountModel.getIsActive());
			accountModel.setIsPrimary(ValidateIsPrimary(accountModel.getCustomerId()));
			CustomerBankAccountModel existingentityObject =customerAccountRepository.save(accountModel);
			
			return convertToDto(existingentityObject).getBankAccountId();
		}
		catch(BusinessException e) {
			throw e;
		}
	
	}
	
	
	
	@Transactional(readOnly = true)
	public CustomerBankAccountDto getCustomerBankDetailsByBankAccountId(Integer bankAccountId) {
		try {
			CustomerBankAccountModel customerBankAccount = customerAccountRepository.findByBankAccountIdAndIsDeleteFalse(bankAccountId);
			System.out.println(customerBankAccount);
				if(customerBankAccount ==null) {
					throw new BusinessException("Customer with account ID "+bankAccountId+"not exists");
				}
				return convertToDto(customerBankAccount);
	
		}catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = true)
	public  Page<CustomerBankAccountDto> getAllCustomerBankAccountDetails(int page, int size){
		Pageable pageble = PageRequest.of(page, size);
		Page<CustomerBankAccountModel> pageResult = customerAccountRepository.findByIsDeleteFalse(pageble);
		 return pageResult.map(this::convertToDto);
	}
	
	@Transactional
	public String  softDeleteCustomersBankAccountDetails(CustomerBankAccountDto accountDetailsDto) {
		try {
			Integer AccountId = ValidateFieldInteger(accountDetailsDto.getBankAccountId(), "Account Id");
			Integer updateedBy = ValidateFieldInteger(accountDetailsDto.getUpdatedBy(),"Updated By");
			CustomerBankAccountModel userAccountDetails = customerAccountRepository.findByBankAccountIdAndIsDeleteFalse(AccountId);
			if(userAccountDetails == null) {
				throw new BusinessException("Bank details not found for id"+AccountId);
			}
			userAccountDetails.setIsDelete(true);
			userAccountDetails.setUpdatedBy(accountDetailsDto.getUpdatedBy());
			customerAccountRepository.save(userAccountDetails);
			
			return "Customer Account with Id" +AccountId +"deleted successfully" ;
		}catch(BusinessException e) {
			throw e;
		}
	}
	
	// Penny drop verification should be added external api
	@Transactional
	public Integer verifyBankAccountDeatils(CustomerBankAccountDto bankAccountDto) {
		try {
			Integer ver_bankAccountId = ValidateFieldInteger(bankAccountDto.getBankAccountId(),  "BankAccount Id");
			CustomerBankAccountModel verifyAccountDetails = customerAccountRepository.findByBankAccountIdAndIsActiveTrue(ver_bankAccountId);
			verifyAccountDetails.setUpdatedBy(ValidateFieldInteger(bankAccountDto.getUpdatedBy(), "Updated By"));
			verifyAccountDetails.setUpdatedAt(LocalDateTime.now());
			verifyAccountDetails.setVerifiedAt(LocalDateTime.now());
			verifyAccountDetails.setVerifiedBy(ValidateFieldInteger(bankAccountDto.getVerifiedBy(),"Vrified By"));
			verifyAccountDetails.setIsVerified(bankAccountDto.getIsVerified()?bankAccountDto.getIsVerified():false);
			
			CustomerBankAccountModel verifiedResult = customerAccountRepository.save(verifyAccountDetails);
			
			return convertToDto(verifiedResult).getBankAccountId();
		}
		catch( BusinessException e) {
			throw e;
		}
		 
	}
	// primary bank account details
	@Transactional
	public Integer primaryBankAccountDeatils(CustomerBankAccountDto bankAccountDto) {
		try {
			 String ver_bankAccountNumber = ValidateFieldString(bankAccountDto.getAccountNumber(),"bank AccountNumber"); 
			 CustomerBankAccountModel primaryAccount = customerAccountRepository.findByAccountNumber(ver_bankAccountNumber);
			 if (primaryAccount == null) {
		            throw new BusinessException("Bank account not found for number: " + ver_bankAccountNumber);
		        }
			 Integer customerAccounts = primaryAccount.getCustomerId();
			 Optional<CustomerBankAccountModel> existingPrimaryAccount =
				        customerAccountRepository.findByCustomerIdAndIsPrimaryTrue(customerAccounts);

				existingPrimaryAccount.ifPresent(account -> {
				    account.setIsPrimary(false);
				    customerAccountRepository.save(account);  
				});
			 primaryAccount.setIsPrimary(true);
			 CustomerBankAccountModel accountModel = customerAccountRepository.save(primaryAccount);
			 
			 return convertToDto(accountModel).getBankAccountId();
		}
		catch(BusinessException e) {
			
			throw e;
		}
	 
	}
	
	
	
	
	public CustomerBankAccountDto convertToDto(CustomerBankAccountModel accountModel) {
		CustomerBankAccountDto bankAccountDetailsDto = new CustomerBankAccountDto();
		bankAccountDetailsDto.setBankAccountId(accountModel.getBankAccountId());
		bankAccountDetailsDto.setAccountHolderName(accountModel.getAccountHolderName());
		bankAccountDetailsDto.setAccountNumber(accountModel.getAccountNumber());
		bankAccountDetailsDto.setBankId(accountModel.getBankId());
		bankAccountDetailsDto.setCustomerId(accountModel.getCustomerId());
		bankAccountDetailsDto.setBranchName(accountModel.getBranchName());
		bankAccountDetailsDto.setIfscCode(accountModel.getIfscCode());
		bankAccountDetailsDto.setUpiId(accountModel.getUpiId());
		bankAccountDetailsDto.setBankProofFileId(accountModel.getBankProofFileId());
		bankAccountDetailsDto.setAccountType(accountModel.getAccountType());
		bankAccountDetailsDto.setAccountStatus(accountModel.getAccountStatus());
		bankAccountDetailsDto.setPaymentMode(accountModel.getPaymentMode());
		bankAccountDetailsDto.setIdentity(accountModel.getIdentity());
		bankAccountDetailsDto.setIsActive(accountModel.getIsActive());
		bankAccountDetailsDto.setCreatedAt(accountModel.getCreatedAt());
		bankAccountDetailsDto.setCreatedBy(accountModel.getCreatedBy());
		bankAccountDetailsDto.setUpdatedAt(accountModel.getUpdatedAt() !=null ? accountModel.getUpdatedAt():accountModel.getCreatedAt());
		bankAccountDetailsDto.setUpdatedBy(accountModel.getUpdatedBy() !=null ? accountModel.getUpdatedBy():accountModel.getCreatedBy()  );
		bankAccountDetailsDto.setIsDelete(accountModel.getIsDelete());
		bankAccountDetailsDto.setIsPrimary(accountModel.getIsPrimary());
		
		return bankAccountDetailsDto;
	}
	
	public Boolean ValidateIsPrimary(Integer CustomerId ) {
	    Optional<CustomerBankAccountModel> existingPrimary = customerAccountRepository.findByCustomerIdAndIsPrimaryTrue(CustomerId);
		if(existingPrimary.isPresent()) {
			throw new BusinessException("Customer can use only one bank account at a time");
		}
		return true;
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
	

	public  String ValidateFieldString(String stringField, String name) {
		if(stringField == null || stringField.trim().isEmpty()) {
			throw new BusinessException(name +" cannot be null or empty");
		}
		stringField= stringField.trim().replaceAll("\\s{2,}", " ");
		return  stringField;
	}
	
	public boolean ValidateIFSCcode(String ifscCode) {
		String  inputfield = ifscCode.trim().toUpperCase();
		String ifscRgx = "^[A-Z]{4}0[A-Z0-9]{6}$";
		return inputfield.matches(ifscRgx);
	}




 
}
