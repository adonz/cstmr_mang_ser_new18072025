package com.incede.nbfc.customer_management.StepperController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalReferenceValueDto;
import com.incede.nbfc.customer_management.DTOs.CustomerAddressesDto;
import com.incede.nbfc.customer_management.DTOs.CustomerBankAccountDto;
import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.DTOs.CustomerPhotoDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.StepperService.StepperDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/stepper-data")
public class StepperDetailsController {
	
	private final StepperDetailsService stepperService;
	
	public StepperDetailsController(StepperDetailsService stepperService) {
		this.stepperService = stepperService;
	}
	
	// save address
	@PostMapping("/address/{sessionId}")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerAddress(
			@RequestParam String stepperId,
			@PathVariable  String sessionId ,
			@Valid @RequestBody CustomerAddressesDto customerAddressDetails){
		Integer customerAccount = stepperService.SaveAddressDetails(stepperId,sessionId,customerAddressDetails);
	    return ResponseEntity.ok(ResponseWrapper.success( customerAccount,"Address details Saved To Redis successfully"));

	}
	
	// get address
	@GetMapping("/address/stepperId/{stepperId}/sessionId/{sessionId}/key/{key}")
	public ResponseEntity<ResponseWrapper<CustomerAddressesDto>> getCustomerAddressDetails( @PathVariable  String stepperId, @PathVariable String sessionId ,@PathVariable String key){
		CustomerAddressesDto resultDto = stepperService.getCustomerAddressDetailsByStepperIdAndSessionId(stepperId, sessionId, key);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"Address details retrived successfully")); 
	}
	
	// save bank account
	@PostMapping("/bank-account/{sessionId}")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerAccountDetails(
			@RequestParam String stepperId,
			@PathVariable  String sessionId,
			@Valid @RequestBody   CustomerBankAccountDto accountDto){
		    Integer resultDto = stepperService.saveCustomerAccountDetails(accountDto,sessionId, stepperId);
		    return ResponseEntity.ok(ResponseWrapper.success( resultDto,"Address details Saved To Redis successfully"));

	}
	
	//get bank account
	@GetMapping("/bank-account/stepperId/{stepperId}/sessionId/{sessionId}/key/{key}")
	public ResponseEntity<ResponseWrapper<CustomerBankAccountDto>> getBankAccountDetails( @PathVariable  String stepperId, @PathVariable  String sessionId ,@PathVariable String key){
		CustomerBankAccountDto resultDto = stepperService.getCustomerBankAccountDetailsByStepperIdAndSessionId(stepperId, sessionId, key);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"BankAccount details retrived successfully")); 
	}
	
	
	// save customer geo taged photo
	@PostMapping("/customer-photo/{sessionId}")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerPhotoDetails(
			@RequestParam String stepperId,
			@PathVariable  String sessionId ,
			@Valid @RequestBody CustomerPhotoDto customerPhotoDetails){
		Integer customerPhoto = stepperService.saveCustomerPhotoDetails(  customerPhotoDetails,  sessionId,  stepperId);
	    return ResponseEntity.ok(ResponseWrapper.success( customerPhoto,"Address details Saved To Redis successfully"));

	}
	
	// get customer photo details
	@GetMapping("/customer-photo/stepperId/{stepperId}/sessionId/{sessionId}/key/{key}")
	public ResponseEntity<ResponseWrapper<CustomerPhotoDto>> getcustomerPhotoDetails( @PathVariable  String stepperId, @PathVariable  String sessionId ,@PathVariable String key){
		CustomerPhotoDto resultDto = stepperService.getCustomerPhotoDetailsByStepperIdAndSessionId(stepperId, sessionId, key);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"Geo taged Photo details retrived successfully")); 
	}
	
	// save additional info 
	@PostMapping("/cuatomer-additional-details/{sessionId}")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerAdditionalDetails(
			@RequestParam String stepperId,
			@PathVariable  String sessionId ,
			@Valid @RequestBody CustomerAdditionalReferenceValueDto customerAdditionalDetails){
		Integer customerPhoto = stepperService.saveAdditionalInformationDetails(  customerAdditionalDetails,  sessionId,  stepperId);
	    return ResponseEntity.ok(ResponseWrapper.success( customerPhoto,"Additional details Saved To Redis successfully"));

	}
	// get customer additional details
	@GetMapping("/cuatomer-additional-details/stepperId/{stepperId}/sessionId/{sessionId}/key/{key}")
	public ResponseEntity<ResponseWrapper<CustomerAdditionalReferenceValueDto>> getcustomerAdditionalDetails( @PathVariable  String stepperId, @PathVariable  String sessionId ,@PathVariable String key){
		CustomerAdditionalReferenceValueDto resultDto = stepperService.getCustomerAdditionalInformationDetailsByStepperIdAndSessionId(stepperId, sessionId, key);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"Additional details retrived successfully")); 
	}
	
	// save optional details
	@PostMapping("/customer-optional-details/{sessionId}")
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerOptionalDetails(
			@RequestParam String stepperId,
			@PathVariable  String sessionId ,
			@Valid @RequestBody CustomerOptionalDetailsDto customerOptionalDetails){
		Integer customerPhoto = stepperService.saveCustomerOptionalInformationDetails(  customerOptionalDetails,  sessionId,  stepperId);
	    return ResponseEntity.ok(ResponseWrapper.success( customerPhoto,"Optional details Saved To Redis successfully"));

	}
	
	// get optional details
	@GetMapping("/customer-optional-details/stepperId/{stepperId}/sessionId/{sessionId}/key/{key}")
	public ResponseEntity<ResponseWrapper<CustomerOptionalDetailsDto>> getcustomerOptionalDetails( @PathVariable  String stepperId, @PathVariable  String sessionId ,@PathVariable String key){
		CustomerOptionalDetailsDto resultDto = stepperService.getCustomerOptionalDetailsByStepperIdAndSessionId(stepperId, sessionId, key);
		return ResponseEntity.ok(ResponseWrapper.success(resultDto,"Optional details retrived successfully")); 
	}
	
	// Save All Stepper Details
	@PostMapping("/save-details/{sessionId}")
	public ResponseEntity<ResponseWrapper<String>> SaveStepperDataDetails(@PathVariable String sessionId){
		     String resultDto = stepperService.SaveStepperDetails(sessionId);
		    return ResponseEntity.ok(ResponseWrapper.success( resultDto," Saved to database"));

	}

}
