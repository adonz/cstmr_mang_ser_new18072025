package com.incede.nbfc.customer_management.FeignClientsModels;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequirementConfigurationDto {

    private Integer documentId;
	
	@NotNull(message = "Document Type is mandatory")
    private String documentType;
}