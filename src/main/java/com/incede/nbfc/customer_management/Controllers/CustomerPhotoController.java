package com.incede.nbfc.customer_management.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incede.nbfc.customer_management.DTOs.CustomerPhotoDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;
import com.incede.nbfc.customer_management.Services.CustomerPhotoService;

import java.util.List;

import org.springframework.http.MediaType;



@RestController
@RequestMapping("/v1/customers/customer-photos")
public class CustomerPhotoController {
	
	private final CustomerPhotoService customerPhotoService;
	
	public CustomerPhotoController(CustomerPhotoService customerPhotoService) {
		this.customerPhotoService=customerPhotoService;
	}
	
	@PostMapping(value ="/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseWrapper<Integer>> createCustomerImage(@RequestPart("details")  CustomerPhotoDto customerDto,
																		@RequestPart("image") MultipartFile imageFile){
		 Integer customerPhoto = customerPhotoService.createdCustomerPhoto(customerDto, imageFile);
		 return ResponseEntity.ok(ResponseWrapper.created(customerPhoto,"customer Photo details saved successfully"));
	}
	
	@PostMapping("/")
	public ResponseEntity<ResponseWrapper<Integer>> addCustomerImage( @PathVariable CustomerPhotoDto customerDto){
		 Integer customerPhoto = customerPhotoService.addCustomerPhoto(customerDto);
		 return ResponseEntity.ok(ResponseWrapper.created(customerPhoto,"customer Photo details saved successfully"));
	}
	
	
	@GetMapping("/get/{customerId}")
	public ResponseEntity<ResponseWrapper<List<CustomerPhotoDto>>> getCustomerPhotos( @PathVariable Integer customerId){
		List<CustomerPhotoDto> resultDto = customerPhotoService.getCustomerPhotosForCustomerId(customerId);
		 return ResponseEntity.ok(ResponseWrapper.success(resultDto,"customer Photo details retrived successfully"));

	}
	
	@PostMapping("/verify/{photoId}")
	public ResponseEntity<ResponseWrapper<Integer>> verifyCustomerPhotos( @PathVariable Integer photoId, @RequestBody CustomerPhotoDto customerDto){
		Integer resultDto = customerPhotoService.verifyCustomerPhotosForPhotoId(photoId, customerDto);
		 return ResponseEntity.ok(ResponseWrapper.success(resultDto,"customer Photo details verified successfully"));

	}
	
	@PostMapping("/delete/{photoId}/updatedBy/{updatedBy}")
	public ResponseEntity<ResponseWrapper<Integer>> deleteCustomerPhotos( @PathVariable Integer photoId, Integer updatedBy){
		Integer resultDto = customerPhotoService.deleteCustomerPhotosForPhotoId(photoId, updatedBy);
		 return ResponseEntity.ok(ResponseWrapper.success(resultDto,"customer Photo details deleted successfully"));

	}
	

}
