package com.incede.nbfc.customer_management.StepperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.incede.nbfc.customer_management.DTOs.CustomerAdditionalReferenceValueDto;
import com.incede.nbfc.customer_management.DTOs.CustomerAddressesDto;
import com.incede.nbfc.customer_management.DTOs.CustomerBankAccountDto;
import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.DTOs.CustomerPhotoDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.GetStepperData.GetRedisCacheData;
import com.incede.nbfc.customer_management.Services.CustomerAdditionalReferenceValueService;
import com.incede.nbfc.customer_management.Services.CustomerAddressesService;
import com.incede.nbfc.customer_management.Services.CustomerBankAccountService;
import com.incede.nbfc.customer_management.Services.CustomerCategoryMappingService;
import com.incede.nbfc.customer_management.Services.CustomerGroupMappingService;
import com.incede.nbfc.customer_management.Services.CustomerOptionalDetailsService;
import com.incede.nbfc.customer_management.Services.CustomerPhotoService;
import com.incede.nbfc.customer_management.Services.LeadMasterService;
import com.incede.nbfc.customer_management.StepperDto.StepperDetailsDto;

 
@Service
public class StepperDetailsService {
	
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	private final CustomerAddressesService addressService;
	private final CustomerBankAccountService bankaccountService;
	private final CustomerPhotoService cuatomerPhotoService;
	private final CustomerOptionalDetailsService optionalDetailsService;
	private final CustomerAdditionalReferenceValueService additionalReferenceValue;
	private final CustomerCategoryMappingService categoryMappingService;
	private final CustomerGroupMappingService groupMappingService;
	private final GetRedisCacheData getRedisCacheData;
	private final LeadMasterService leadMasterService;
	
	public StepperDetailsService(RedisTemplate<String, Object> redisTemplate ,
			ObjectMapper objectMapper,  
			CustomerAddressesService addressService,
			CustomerBankAccountService bankaccountService,
			GetRedisCacheData getRedisCacheData,
			CustomerPhotoService cuatomerPhotoService,
			CustomerOptionalDetailsService optionalDetailsService,
			CustomerAdditionalReferenceValueService additionalReferenceValue,
			CustomerCategoryMappingService categoryMappingService,
			CustomerGroupMappingService groupMappingService,
			LeadMasterService leadMasterService
			) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
		this.addressService =addressService;
		this.bankaccountService = bankaccountService;
		this.getRedisCacheData = getRedisCacheData;
		this.cuatomerPhotoService = cuatomerPhotoService;
		this.optionalDetailsService = optionalDetailsService;
		this.additionalReferenceValue = additionalReferenceValue;
		this.categoryMappingService = categoryMappingService;
		this.groupMappingService = groupMappingService;
		this.leadMasterService = leadMasterService;
	}

	
	// save address details to redis 
	@Transactional
	public Integer SaveAddressDetails(String stepperId, String  sessionId,  CustomerAddressesDto   customerAddressDetails) {
		try {
			String key = stepperId;
	        Object rawData = redisTemplate.opsForValue().get(key);

	        StepperDetailsDto stepperData;
	        if (rawData == null) {
	            stepperData = new StepperDetailsDto();
	            stepperData.setStepperId(stepperId);
	            stepperData.setSessionId(sessionId);
	            stepperData.setStepperData(new HashMap<>());
	        } 
	         else {
	            stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
	        }
	        String stepperName = "address";
	        if (stepperData.getStepperData().containsKey(stepperName)) {
	            throw new BusinessException(
	                "Step '" + stepperName + "' already exists for stepperId " + stepperId
	            );
	        }
	        Map<String, Object> addressMap = objectMapper.convertValue(customerAddressDetails, Map.class);
	        stepperData.getStepperData().put(stepperName, addressMap);
			redisTemplate.opsForValue().set(key, stepperData);
			
			return customerAddressDetails.getCustomerId() ;
			
		}catch(Exception e) {
			throw new BusinessException( "error in caching"+e);
		}
 		 
	}
	
	// get address details from redis cache
	@Transactional
	public CustomerAddressesDto getCustomerAddressDetailsByStepperIdAndSessionId( String  sessionId,
			 String  stepperId , String key) {
		 try {
		        Object rawData = redisTemplate.opsForValue().get(sessionId);
		        if(rawData instanceof Map<?, ?> rawMap) {
			        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
			        if (stepperData == null) {
		                throw new RuntimeException("No stepperData found in Redis for sessionId: " + sessionId);
		            }
			        Map<?, ?> addressMap = (Map<?, ?>) stepperData.get(key);
			        ObjectMapper mapper = new ObjectMapper();
			         mapper.registerModule(new JavaTimeModule());
			         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			         CustomerAddressesDto addressDto = mapper.convertValue(addressMap, CustomerAddressesDto.class);
			         return addressDto;
		        }else {
			        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);

		        }
		 }catch(BusinessException e) {
			 throw e;
		 }
		 
	}

	// save account details to redis cache
	@Transactional
	public Integer saveCustomerAccountDetails(CustomerBankAccountDto accountDto, String sessionId,String stepperId ) {
		try { 
			String key = stepperId;
	        Object rawData = redisTemplate.opsForValue().get(key);

	        StepperDetailsDto stepperData ;
	        if (rawData == null) {
	            stepperData = new StepperDetailsDto();
	            stepperData.setStepperId(stepperId);
	            stepperData.setSessionId(sessionId);
	            stepperData.setStepperData(new HashMap<>());
	        } 
	         else {
	            stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
	        }
	        String stepperName = "BankAccount";
	        if (stepperData.getStepperData().containsKey(stepperName)) {
	            throw new BusinessException(
	                "Step '" + stepperName + "' already exists for stepperId " + stepperId
	            );
	        }
	        Map<String, Object>  bankAccount = objectMapper.convertValue(accountDto, Map.class);
	        stepperData.getStepperData().put(stepperName, bankAccount);
			redisTemplate.opsForValue().set(key, stepperData);
			
			return accountDto.getBankId();
		}
		catch(BusinessException e) {
			throw  e;
		}catch (Exception e) {
	        throw new BusinessException("Unexpected error: " + e.getMessage());
	    }
 		 
	}

	// get account details from redis cache
	@Transactional
	public CustomerBankAccountDto getCustomerBankAccountDetailsByStepperIdAndSessionId(	String sessionId,String stepperId,
		 String key) {
		 try {
		        Object rawData = redisTemplate.opsForValue().get(sessionId);
		        if (rawData == null) {
		            throw new RuntimeException("No Redis data found for sessionId: " + sessionId);
		        }
		        System.out.println("Redis rawData class: " + rawData.getClass());
		        System.out.println("Redis rawData: " + rawData);
		        
		        if(rawData instanceof Map<?, ?> rawMap) {
			        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
			        if (stepperData == null) {
		                throw new RuntimeException("No stepperData found in Redis for sessionId: " + sessionId);
		            }
			        Map<?, ?> BankAccountMap = (Map<?, ?>) stepperData.get(key);
			        ObjectMapper mapper = new ObjectMapper();
			         mapper.registerModule(new JavaTimeModule());
			         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			         CustomerBankAccountDto bankAccountDto = mapper.convertValue(BankAccountMap, CustomerBankAccountDto.class);
			         return bankAccountDto;
		        }else {
			        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);

		        }
		 }catch(BusinessException e) {
			 throw e;
		 }
	}
	
	// save customer photo details in redis cache
	@Transactional
	public Integer saveCustomerPhotoDetails(CustomerPhotoDto photoDto, String sessionId,String stepperId ) {
		try { 
			String key = stepperId;
	        Object rawData = redisTemplate.opsForValue().get(key);

	        StepperDetailsDto stepperData ;
	        if (rawData == null) {
	            stepperData = new StepperDetailsDto();
	            stepperData.setStepperId(stepperId);
	            stepperData.setSessionId(sessionId);
	            stepperData.setStepperData(new HashMap<>());
	        } 
	         else {
	            stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
	        }
	        String stepperName = "CustomerPhoto";
	        if (stepperData.getStepperData().containsKey(stepperName)) {
	            throw new BusinessException(
	                "Step '" + stepperName + "' already exists for stepperId " + stepperId
	            );
	        }
	        Map<String, Object>  photoDetails = objectMapper.convertValue(photoDto, Map.class);
	        stepperData.getStepperData().put(stepperName, photoDetails);
			redisTemplate.opsForValue().set(key, stepperData);
			
			return photoDto.getPhotoFileId();
		}
		catch(BusinessException e) {
			throw  e;
		}catch (Exception e) {
	        throw new BusinessException("Unexpected error: " + e.getMessage());
	    }
 		 
	}
	
	// get photo details from redis
	@Transactional
	public CustomerPhotoDto getCustomerPhotoDetailsByStepperIdAndSessionId(	String sessionId,String stepperId,
		 String key) {
		 try {
		        Object rawData = redisTemplate.opsForValue().get(sessionId);
		        if (rawData == null) {
		            throw new RuntimeException("No Redis data found for sessionId: " + sessionId);
		        }
		        System.out.println("Redis rawData class: " + rawData.getClass());
		        System.out.println("Redis rawData: " + rawData);
		        
		        if(rawData instanceof Map<?, ?> rawMap) {
			        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
			        if (stepperData == null) {
		                throw new RuntimeException("No stepperData found in Redis for sessionId: " + sessionId);
		            }
			        Map<?, ?> BankAccountMap = (Map<?, ?>) stepperData.get(key); //CustomerPhoto
			        ObjectMapper mapper = new ObjectMapper();
			         mapper.registerModule(new JavaTimeModule());
			         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			         CustomerPhotoDto photoDto = mapper.convertValue(BankAccountMap, CustomerPhotoDto.class);
			         return photoDto;
		        }else {
			        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);

		        }
		 }catch(BusinessException e) {
			 throw e;
		 }
	}
	
	// save customer optional information to redis cache
	@Transactional
	public Integer saveCustomerOptionalInformationDetails(CustomerOptionalDetailsDto optionalDetailsDto, String sessionId,String stepperId ) {
		try { 
			String key = stepperId;
	        Object rawData = redisTemplate.opsForValue().get(key);

	        StepperDetailsDto stepperData ;
	        if (rawData == null) {
	            stepperData = new StepperDetailsDto();
	            stepperData.setStepperId(stepperId);
	            stepperData.setSessionId(sessionId);
	            stepperData.setStepperData(new HashMap<>());
	        } 
	         else {
	            stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
	        }
	        String stepperName = "OptionalDetails";
	        if (stepperData.getStepperData().containsKey(stepperName)) {
	            throw new BusinessException(
	                "Step '" + stepperName + "' already exists for stepperId " + stepperId
	            );
	        }
	        Map<String, Object>  photoDetails = objectMapper.convertValue(optionalDetailsDto, Map.class);
	        stepperData.getStepperData().put(stepperName, photoDetails);
			redisTemplate.opsForValue().set(key, stepperData);
			
			return optionalDetailsDto.getCustomerId();
		}
		catch(BusinessException e) {
			throw  e;
		}catch (Exception e) {
	        throw new BusinessException("Unexpected error: " + e.getMessage());
	    }
 		 
	}
	
	// get customer Optional Details
	@Transactional
	public CustomerOptionalDetailsDto getCustomerOptionalDetailsByStepperIdAndSessionId(	String sessionId,String stepperId,
			 String key) {
			 try {
			        Object rawData = redisTemplate.opsForValue().get(sessionId);
			        if (rawData == null) {
			            throw new RuntimeException("No Redis data found for sessionId: " + sessionId);
			        }
			        System.out.println("Redis rawData class: " + rawData.getClass());
			        System.out.println("Redis rawData: " + rawData);
			        
			        if(rawData instanceof Map<?, ?> rawMap) {
				        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
				        if (stepperData == null) {
			                throw new RuntimeException("No stepperData found in Redis for sessionId: " + sessionId);
			            }
				        Map<?, ?> BankAccountMap = (Map<?, ?>) stepperData.get(key); //OptionalDetails
				        ObjectMapper mapper = new ObjectMapper();
				         mapper.registerModule(new JavaTimeModule());
				         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				         CustomerOptionalDetailsDto optionalDetailsDto = mapper.convertValue(BankAccountMap, CustomerOptionalDetailsDto.class);
				         return optionalDetailsDto;
			        }else {
				        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);

			        }
			 }catch(BusinessException e) {
				 throw e;
			 }
		}
	
	//save customer additional reference info
	@Transactional
	public Integer saveAdditionalInformationDetails(CustomerAdditionalReferenceValueDto additionalInfoDto, String sessionId,String stepperId ) {
		try { 
			String key = stepperId;
	        Object rawData = redisTemplate.opsForValue().get(key);

	        StepperDetailsDto stepperData ;
	        if (rawData == null) {
	            stepperData = new StepperDetailsDto();
	            stepperData.setStepperId(stepperId);
	            stepperData.setSessionId(sessionId);
	            stepperData.setStepperData(new HashMap<>());
	        } 
	         else {
	            stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
	        }
	        String stepperName = "AdditionalInfo";
	        if (stepperData.getStepperData().containsKey(stepperName)) {
	            throw new BusinessException(
	                "Step '" + stepperName + "' already exists for stepperId " + stepperId
	            );
	        }
	        Map<String, Object>  photoDetails = objectMapper.convertValue(additionalInfoDto, Map.class);
	        stepperData.getStepperData().put(stepperName, photoDetails);
			redisTemplate.opsForValue().set(key, stepperData);
			
			return additionalInfoDto.getCustomerAdditionalReferenceValueId();
		}
		catch(BusinessException e) {
			throw  e;
		}catch (Exception e) {
	        throw new BusinessException("Unexpected error: " + e.getMessage());
	    }
		
	}
	
	// get customer additional information
	@Transactional
	public CustomerAdditionalReferenceValueDto getCustomerAdditionalInformationDetailsByStepperIdAndSessionId(	String sessionId,String stepperId,
			 String key) {
			 try {
			        Object rawData = redisTemplate.opsForValue().get(sessionId);
			        if (rawData == null) {
			            throw new RuntimeException("No Redis data found for sessionId: " + sessionId);
			        }
			        System.out.println("Redis rawData class: " + rawData.getClass());
			        System.out.println("Redis rawData: " + rawData);
			        
			        if(rawData instanceof Map<?, ?> rawMap) {
				        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
				        if (stepperData == null) {
			                throw new RuntimeException("No stepperData found in Redis for sessionId: " + sessionId);
			            }
				        Map<?, ?> BankAccountMap = (Map<?, ?>) stepperData.get(key); //AdditionalInfo
				        ObjectMapper mapper = new ObjectMapper();
				         mapper.registerModule(new JavaTimeModule());
				         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				         CustomerAdditionalReferenceValueDto additionalDetailsDto = mapper.convertValue(BankAccountMap, CustomerAdditionalReferenceValueDto.class);
				         return additionalDetailsDto;
			        }else {
				        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);
			        }
			 }catch(BusinessException e) {
				 throw e;
			 }
		}
 		 
	// save all final stepper details
	@Transactional
	public String SaveStepperDetails(String sessionId) {
		
		Object rawData = redisTemplate.opsForValue().get(sessionId);
		System.out.println("Redis raw data: " + rawData);
		
		
		 if (rawData instanceof Map<?, ?> rawMap) {
		        Map<?, ?> stepperData = (Map<?, ?>) rawMap.get("stepperData");
	 
	
		        Map<?, ?> addressMap = (Map<?, ?>) stepperData.get("address");
		        Map<?, ?> bankMap = (Map<?, ?>) stepperData.get("BankAccount");
		        Map<?, ?>  customerPhoto = (Map<?, ?>) stepperData.get("CustomerPhoto");
		        Map<?, ?>   additionalInfo = (Map<?, ?>) stepperData.get("AdditionalInfo");
		        Map<?, ?>   optionalInfo = (Map<?, ?>) stepperData.get("OptionalDetails");
         ObjectMapper mapper = new ObjectMapper();
         mapper.registerModule(new JavaTimeModule());
         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
         CustomerAddressesDto addressDto = mapper.convertValue(addressMap, CustomerAddressesDto.class);
         CustomerBankAccountDto accountDto = mapper.convertValue(bankMap, CustomerBankAccountDto.class);
         CustomerPhotoDto customerPhotoDto = mapper.convertValue(customerPhoto, CustomerPhotoDto.class);
         CustomerAdditionalReferenceValueDto additionalDetailsDto = mapper.convertValue(additionalInfo, CustomerAdditionalReferenceValueDto.class);
         CustomerOptionalDetailsDto optionalDetailsDto = mapper.convertValue(optionalInfo, CustomerOptionalDetailsDto.class);
         
         bankaccountService.createOrUpdateCustomerAccountDetails(accountDto);
         addressService.createCustomerAddressDetails(1001, addressDto);
         cuatomerPhotoService.addCustomerPhoto(customerPhotoDto);
         optionalDetailsService.createCustomerOptionalDetails(optionalDetailsDto);
        // additionalReferenceValue.createCustomerAdditionalInformation(additionalDetailsDto);// additional information information is list should be converted as list
         // as per user story we need to pass customer id customer id but the customer id has not been specified
		return "data saved to table successfully";															// want to write a new service for address
	}
	 else {
		        throw new RuntimeException("Unexpected Redis format for session: " + sessionId);
		    }
	


	}
	
}
