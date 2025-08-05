package com.incede.nbfc.customer_management.StepperDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepperDetailsDto {
    private String sessionId;
	private String stepperId;
	private Map<String,Map<String,Object>> stepperData;
	
}
