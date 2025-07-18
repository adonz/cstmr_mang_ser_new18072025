package com.incede.nbfc.customer_management.FeignClientsModels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NationalityDTO {

	private Integer nationalityId;
	
    @NotBlank(message = "Nationality must not be empty")
    private String nationality;
}
