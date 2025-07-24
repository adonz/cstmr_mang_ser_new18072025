package com.incede.nbfc.customer_management.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incede.nbfc.customer_management.DTOs.CustomerPhotoDto;
import com.incede.nbfc.customer_management.Exceptions.BadRequestException.BadRequestException;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerPhotoModel;
import com.incede.nbfc.customer_management.Repositories.CustomerPhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CustomerPhotoService {
	
	private final CustomerPhotoRepository customerPhotoRepository;
	
	public CustomerPhotoService(CustomerPhotoRepository customerPhotoRepository) {
		this.customerPhotoRepository=customerPhotoRepository;
	}
	@Value("${upload.dir}")
	public String UploadFolder;
	
	@Transactional
	public Integer createdCustomerPhoto(CustomerPhotoDto customerDto, MultipartFile imageFile) {
		 try {
			 
			 MultipartFile ver_imagePath = validateImage(imageFile);
			 Optional<CustomerPhotoModel> lastPhoto = customerPhotoRepository
					    .findTopByCustomerIdOrderByCaptureTimeDesc(customerDto.getCustomerId());
			 if (lastPhoto.isPresent()) {
				    Duration duration = Duration.between(lastPhoto.get().getCaptureTime(), LocalDateTime.now());
				    if (duration.toMinutes() < 2) {
				        throw new BusinessException("Duplicate photo upload within 2 minutes is not allowed.");
				    }
				}
			 
			 Path uploadPath = Paths.get(UploadFolder);
			 Files.createDirectories(uploadPath);
			 
			 String imageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
			 Path imagePath = uploadPath.resolve(imageName);
	         Files.write(imagePath, imageFile.getBytes());
	         
	         String imagePathForDb =UploadFolder.replace("\\", "/") + "/" + imageName;
	         
	         CustomerPhotoModel CustomerPhoto = new CustomerPhotoModel();
	         CustomerPhoto.setCustomerId(ValidateFieldInteger(customerDto.getCustomerId(),"Customer ID"));
	        // CustomerPhoto.setPhotoFilePath(imagePathForDb);
	         CustomerPhoto.setLatitude(validateLatitude(customerDto.getLatitude()));
	         CustomerPhoto.setLongitude(validateLongitude(customerDto.getLongitude()));
	         CustomerPhoto.setAccuracy(customerDto.getAccuracy() !=null? customerDto.getAccuracy():null);
	         CustomerPhoto.setCaptureTime(LocalDateTime.now());
	         CustomerPhoto.setCaptureDevice(customerDto.getCaptureDevice() !=null?customerDto.getCaptureDevice():null );
	         CustomerPhoto.setCaptureBy(ValidateFieldInteger(customerDto.getCaptureBy(),"Captured By"));
	         CustomerPhoto.setLocationDescription(customerDto.getLocationDescription()!=null? customerDto.getLocationDescription():null);
	         CustomerPhoto.setIsVerified(false);
	         CustomerPhoto.setVerifiedBy(customerDto.getVerifiedBy()!=null?customerDto.getVerifiedBy():null);
	         CustomerPhoto.setVerifiedAt(customerDto.getVerifiedBy()!=null?LocalDateTime.now():null);
	         CustomerPhoto.setCreatedBy(ValidateFieldInteger(customerDto.getCreatedBy(),"created By"));
	         
	         CustomerPhotoModel saved = customerPhotoRepository.save(CustomerPhoto);
	         return  convertToDto(saved).getPhotoId();

		 }
		 catch(IOException e){
			   throw new BusinessException("Image upload failed: " + e.getMessage());
		 }
		 
	}
	
	public Integer addCustomerPhoto(CustomerPhotoDto customerDto) {
		try {
			 Optional<CustomerPhotoModel> lastPhoto = customerPhotoRepository
					    .findTopByCustomerIdOrderByCaptureTimeDesc(customerDto.getCustomerId());
			 if (lastPhoto.isPresent()) {
				    Duration duration = Duration.between(lastPhoto.get().getCaptureTime(), LocalDateTime.now());
				    if (duration.toMinutes() < 2) {
				        throw new BusinessException("Duplicate photo upload within 2 minutes is not allowed.");
				    }
				}
	         CustomerPhotoModel CustomerPhoto = new CustomerPhotoModel();
	         CustomerPhoto.setCustomerId(ValidateFieldInteger(customerDto.getCustomerId(),"Customer ID"));
	         CustomerPhoto.setPhotoFileId(customerDto.getPhotoFileId());
	         CustomerPhoto.setLatitude(validateLatitude(customerDto.getLatitude()));
	         CustomerPhoto.setLongitude(validateLongitude(customerDto.getLongitude()));
	         CustomerPhoto.setAccuracy(customerDto.getAccuracy() !=null? customerDto.getAccuracy():null);
	         CustomerPhoto.setCaptureTime(LocalDateTime.now());
	         CustomerPhoto.setCaptureDevice(customerDto.getCaptureDevice() !=null?customerDto.getCaptureDevice():null );
	         CustomerPhoto.setCaptureBy(ValidateFieldInteger(customerDto.getCaptureBy(),"Captured By"));
	         CustomerPhoto.setLocationDescription(customerDto.getLocationDescription()!=null? customerDto.getLocationDescription():null);
	         CustomerPhoto.setIsVerified(false);
	         CustomerPhoto.setVerifiedBy(customerDto.getVerifiedBy()!=null?customerDto.getVerifiedBy():null);
	         CustomerPhoto.setVerifiedAt(customerDto.getVerifiedBy()!=null?LocalDateTime.now():null);
	         CustomerPhoto.setCreatedBy(ValidateFieldInteger(customerDto.getCreatedBy(),"created By"));
	         
	         CustomerPhotoModel saved = customerPhotoRepository.save(CustomerPhoto);
	         return  convertToDto(saved).getPhotoId();
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	@Transactional
	public CustomerPhotoDto getCustomerDtoByPhotoId(Integer PhotoId) {
		try {
			CustomerPhotoModel photoData = customerPhotoRepository.findByPhotoIdAndIsDeleteFalse(PhotoId);
			if(photoData == null) throw new BadRequestException(" Data Not Found");
			return convertToDto(photoData);
		}
		catch(BusinessException e) {
			throw e;
		}
	}
	
	
	// convert to dto methode
	public CustomerPhotoDto convertToDto(CustomerPhotoModel model) {
	    CustomerPhotoDto dto = new CustomerPhotoDto();

	    dto.setPhotoId(model.getPhotoId());
	    dto.setCustomerId(model.getCustomerId());
	   // dto.setPhotoFilePath(model.getPhotoFilePath());
	    dto.setPhotoFileId(model.getPhotoFileId());
	    dto.setLatitude(model.getLatitude());
	    dto.setLongitude(model.getLongitude());
	    dto.setAccuracy(model.getAccuracy());
	    dto.setCaptureTime(model.getCaptureTime());
	    dto.setCaptureDevice(model.getCaptureDevice());
	    dto.setCaptureBy(model.getCaptureBy());
	    dto.setLocationDescription(model.getLocationDescription());
	    dto.setIsVerified(model.getIsVerified());
	    dto.setVerifiedBy(model.getVerifiedBy());
	    dto.setVerifiedAt(model.getVerifiedAt());
	    dto.setCreatedBy(model.getCreatedBy());
	    dto.setIsDelete(model.getIsDelete());
	    dto.setCreatedAt(model.getCreatedAt());
	    dto.setUpdatedAt(model.getUpdatedAt() !=null? model.getUpdatedAt():model.getCreatedAt());
	    dto.setUpdatedBy(model.getUpdatedBy() !=null? model.getUpdatedBy():model.getCreatedBy());

	    return dto;
	}
	
	// image validation
	public  MultipartFile validateImage(MultipartFile imageFile) {
		if(imageFile == null || imageFile.isEmpty()) {
			throw new BusinessException("Plese Upload an Profile Image");
		}
	   String contentType = imageFile.getContentType();
	    if (contentType == null || !contentType.startsWith("image/")) {
	        throw new BusinessException("Only image files are allowed (jpg, png, etc).");
	    }
	    long maxSize = 10 * 1024 * 1024;  
	    if (imageFile.getSize() > maxSize) {
	        throw new BusinessException("Image size should not exceed 10MB.");
	    }
	    
	    return imageFile;
	    
	}
	// integer validation
	public Integer ValidateFieldInteger(Integer integerfield, String name) {
 		if(integerfield == null ) {
			throw new BusinessException(name + "cannot be null or empty");
		}
		try {
			return  integerfield;
		}catch(NumberFormatException e) {
			throw new BusinessException(integerfield + "must be a valid Integer");
		}
	}
	
	// latitude validation
	public Double validateLatitude(Double latitude) {
	    if (latitude == null ) {
	        throw new BusinessException("Latitude  cannot be null");
	    }

	    if (latitude < -90 || latitude > 90) {
	        throw new BusinessException("Latitude must be between -90 and 90");
	    }
	    return latitude;

	}
	
	// longitude validation
	public Double validateLongitude( Double longitude) {
	    if (longitude == null) {
	        throw new BusinessException("Longitude cannot be null");
	    }

	    if (longitude < -180 || longitude > 180) {
	        throw new BusinessException("Longitude must be between -180 and 180");
	    }
	    return longitude;
	}

	
	// string validation
	public  String ValidateFielsString(String stringField, String name) {
		if(stringField == null || stringField.trim().isEmpty()) {
			throw new BusinessException(name +" cannot be null or empty");
		}
		stringField= stringField.trim().replaceAll("\\s{2,}", " ");
		return  stringField;
	}


	// get customer Photos
	@Transactional(readOnly=true)
	public List<CustomerPhotoDto> getCustomerPhotosForCustomerId(Integer customerId) {
		 List<CustomerPhotoModel> pageResult = customerPhotoRepository.findByCustomerIdAndIsDeleteFalse(customerId);
		return pageResult
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}


	// verify customer photo
	@Transactional
	public Integer verifyCustomerPhotosForPhotoId(Integer photoId, CustomerPhotoDto customerDto) {
		try {
			 CustomerPhotoModel verifyCustomerPhoto = customerPhotoRepository.findByPhotoIdAndIsDeleteFalseAndIsVerifiedFalse(photoId);
			 if(verifyCustomerPhoto ==null) {
				 throw new BusinessException("no details found for id:"+photoId);
			 }
			 verifyCustomerPhoto.setIsVerified(true);
			 verifyCustomerPhoto.setVerifiedBy(ValidateFieldInteger(customerDto.getVerifiedBy(), "verified By"));
			 verifyCustomerPhoto.setVerifiedAt(LocalDateTime.now());
			 verifyCustomerPhoto.setUpdatedBy(ValidateFieldInteger(customerDto.getUpdatedBy(),"updated By"));
			 verifyCustomerPhoto.setUpdatedAt(LocalDateTime.now());
			 customerPhotoRepository.save(verifyCustomerPhoto);
			 
			 CustomerPhotoDto resultDto =   convertToDto(verifyCustomerPhoto);
			 return resultDto.getPhotoId();
		}
		catch(BusinessException e){
			   throw e;
 
		}
 		 
	}


	@Transactional
	public Integer deleteCustomerPhotosForPhotoId(Integer photoId, Integer updatedBy) {
		try {
			 CustomerPhotoModel deleteCustomerPhoto = customerPhotoRepository.findByPhotoIdAndIsDeleteFalse(photoId);
			 if(deleteCustomerPhoto == null) {
				 throw new BusinessException("No data found for id: "+photoId);
			 }
			 deleteCustomerPhoto.setIsDelete(true);
			 deleteCustomerPhoto.setUpdatedAt(LocalDateTime.now());
			 deleteCustomerPhoto.setUpdatedBy(updatedBy);
			 
			 return photoId;
		}
		catch(BusinessException e) {
			throw e;
		}
 		
	}

}
