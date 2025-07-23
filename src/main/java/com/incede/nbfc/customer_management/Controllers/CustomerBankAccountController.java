package com.incede.nbfc.customer_management.Controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerBankAccountDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerBankAccountService;

import org.springframework.data.domain.Page;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://192.168.1.50:5173")
@RestController
@RequestMapping("/v1/customer")
public class CustomerBankAccountController {
	
	private final CustomerBankAccountService customerBankAccountService;
	
	public CustomerBankAccountController(CustomerBankAccountService customerBankAccountService) {
		this.customerBankAccountService=customerBankAccountService;
	}

	// create or update customer account details 
	@PostMapping("/bank-account/")
	public ResponseEntity<ResponseWrapper<String>> createOrUpdateCustomerAccountDetails(
			@Valid @RequestBody   CustomerBankAccountDto accountDto){
		    boolean updateId = accountDto.getBankAccountId() !=null;
		    Integer resultDto = customerBankAccountService.createOrUpdateCustomerAccountDetails(accountDto);
		   ResponseWrapper <String> response = updateId 
				   ? ResponseWrapper.success("ID" + resultDto +"updated successfully")
				    : ResponseWrapper.created("ID" + resultDto +"created successfully");
		   return updateId
		   			? ResponseEntity.ok(response)
	                : ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/bank-account/{accountId}")
	public ResponseEntity<ResponseWrapper<CustomerBankAccountDto>> getCustomerAccountDetailsByAccountId(@PathVariable Integer accountId){
		CustomerBankAccountDto resultDto = customerBankAccountService.getCustomerBankDetailsByBankAccountId(accountId);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto));
	}
	
	@GetMapping("/bank-account/get/Active-Accounts")
	public ResponseEntity<ResponseWrapper< Page<CustomerBankAccountDto>>> getAllActiveCustomers(
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "10") int size){
				Page<CustomerBankAccountDto> accountList = customerBankAccountService.getAllCustomerBankAccountDetails(page, size);
				return ResponseEntity.ok(ResponseWrapper.success(accountList));
			}
	
	@PostMapping("/bank-account/delete")
	public ResponseEntity<ResponseWrapper< String>> deleteCustomerAccountDetailsByAccountId(@RequestBody CustomerBankAccountDto bankAccountDto){
		 String resultDto = customerBankAccountService.softDeleteCustomersBankAccountDetails(bankAccountDto);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto));
	}
	
	
	@PutMapping("/bank-account/update")
	public ResponseEntity<ResponseWrapper<Integer>> updateCustomerAccountDetailsByAccountId(@RequestBody CustomerBankAccountDto bankAccountDto){
		  Integer resultDto = customerBankAccountService.updateBankAccountDeatils(bankAccountDto);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto, "updated successfully"));
	}
	
	
	@PostMapping("/bank-account/verify")
	public ResponseEntity<ResponseWrapper<Integer>> VerifiCustomerAccountDetails(@RequestBody CustomerBankAccountDto bankAccountDto){
		  Integer resultDto = customerBankAccountService.verifyBankAccountDeatils(bankAccountDto);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto, "verified success fully"));
	}
	
	@PostMapping("/bank-account/primary")
	public ResponseEntity<ResponseWrapper<Integer>> primaryCustomerAccountDetails(@RequestBody CustomerBankAccountDto bankAccountDto){
		  Integer resultDto = customerBankAccountService.primaryBankAccountDeatils(bankAccountDto);
	    return ResponseEntity.ok(ResponseWrapper.success(resultDto, "account seted as primary bank account successfully"));
	}

}
