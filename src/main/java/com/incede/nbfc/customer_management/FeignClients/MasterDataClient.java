package com.incede.nbfc.customer_management.FeignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.incede.nbfc.customer_management.FeignClientsModels.DesignationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.DocumentRequirementConfigurationDto;
import com.incede.nbfc.customer_management.FeignClientsModels.NationalityDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.OccupationDTO;
import com.incede.nbfc.customer_management.FeignClientsModels.salutationtypeDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;

@FeignClient(name = "master-data-service", url = "${masterdata.service.url}")
public interface MasterDataClient {

	@GetMapping("/v1/masterdata/salutationtype/active")
    ResponseWrapper<List<salutationtypeDto>>getAllActiveSalutations();
	
	@GetMapping("/v1/masterdata/documents/requirement-configurations/")
	ResponseWrapper<List<DocumentRequirementConfigurationDto>> getAll();
	
	@GetMapping("/v1/masterdata/nationality/getAll")
	ResponseWrapper<List<NationalityDTO>> getAllNationality();

	@GetMapping("/v1/masterdata/occupations/active")
	ResponseWrapper<List<OccupationDTO>> getAllOccupationalDetails();
	
	@GetMapping("/v1/masterdata/designations/active")
	ResponseWrapper<List<DesignationDTO>> getAllActiveDesignation();
	
}

