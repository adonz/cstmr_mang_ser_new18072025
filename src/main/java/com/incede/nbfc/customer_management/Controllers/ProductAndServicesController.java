package com.incede.nbfc.customer_management.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.Services.ProductAndServicesService;

@RestController
@RequestMapping("/v1/customer")
public class ProductAndServicesController {

	private final ProductAndServicesService productAndServicesService;
	
	public ProductAndServicesController(ProductAndServicesService productAndServicesService) {
		this.productAndServicesService=productAndServicesService;
	}
}
