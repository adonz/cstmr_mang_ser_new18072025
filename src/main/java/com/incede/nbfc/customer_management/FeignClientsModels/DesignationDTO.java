package com.incede.nbfc.customer_management.FeignClientsModels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationDTO {

    private Integer designationId;
    
    @NotBlank(message="designation name should not be blank")
    private String designationName;
	
}
