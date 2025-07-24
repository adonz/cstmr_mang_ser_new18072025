package com.incede.nbfc.customer_management.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerOptionalDetailsDto;
import com.incede.nbfc.customer_management.Models.CustomerOptionalDetailsModel;
import com.incede.nbfc.customer_management.Repositories.CustomerOptionalDetailsRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerOptionalDetailsServiceTest {
	
	@Mock
	private CustomerOptionalDetailsRepository detailsRepository;
	@InjectMocks
	private CustomerOptionalDetailsService detailsService;

	private CustomerOptionalDetailsModel detailsModel;
	
	private CustomerOptionalDetailsDto detailsDto;
	
	
    @BeforeEach
    void setUp() {
    	detailsModel = new CustomerOptionalDetailsModel();
    	detailsModel.setCustomerId(101);
    	detailsModel.setLoanPurposeId(5);
    	detailsModel.setResidentialStatus("Owned");
    	detailsModel.setEducationalLevel("Graduate");
    	detailsModel.setHasHomeLoan(true);
    	detailsModel.setHomeLoanAmount(250000);
    	detailsModel.setHomeLoanCompany("ABC Bank");
    	detailsModel.setLanguageId(2);
    	detailsModel.setCreatedBy(1001);
    }
}
