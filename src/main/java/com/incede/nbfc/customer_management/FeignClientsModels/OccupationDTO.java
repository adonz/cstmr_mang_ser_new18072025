package com.incede.nbfc.customer_management.FeignClientsModels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccupationDTO {

	private Integer occupationId;
	
    @NotBlank(message = "Occupation Name must not be blank")
    private String occupationName;
}
