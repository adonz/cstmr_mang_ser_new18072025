package com.incede.nbfc.customer_management.GetStepperData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.StepperDto.StepperDetailsDto;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetRedisCacheData {
	
	 private final RedisTemplate<String, Object> redisTemplate;
	 private final ObjectMapper  objectMapper;
	 
	 public GetRedisCacheData(RedisTemplate<String, Object> redisTemplate,ObjectMapper  objectMapper) {
		 this.redisTemplate =redisTemplate;
		 this.objectMapper=objectMapper;
	 }
	 
	 public <T> T getStepperData(String sessionId,  Class<T> dtoClass) {
		 Object rawData = redisTemplate.opsForValue().get(sessionId);
		 
		 if(rawData == null) {
			 throw new BusinessException( "no data found for id "+sessionId);
		 }
		 
		 StepperDetailsDto  stepperData = objectMapper.convertValue(rawData, StepperDetailsDto.class);
		 
		 Map<String, Map<String, Object>> steps = stepperData.getStepperData();
	      
	        
	        Object stepData = steps.get(sessionId);
	        
	        return objectMapper.convertValue(stepData, dtoClass);
	 }
}
