package com.incede.nbfc.customer_management.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incede.nbfc.customer_management.DTOs.CustomerPhotoDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.CustomerPhotoModel;
import com.incede.nbfc.customer_management.Repositories.CustomerPhotoRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerPhotoServiceTest {

	@Mock
	private CustomerPhotoRepository photoRepo;
	
	@InjectMocks
	private CustomerPhotoService photoService;
	
    private CustomerPhotoDto customerPhotoDto;

	private CustomerPhotoModel photoModel;
	
    @BeforeEach
    void setUp() {
    	customerPhotoDto = new CustomerPhotoDto();
    	customerPhotoDto.setPhotoId(123);
        customerPhotoDto.setCustomerId(1001);
        customerPhotoDto.setPhotoFileId(501);
        customerPhotoDto.setLatitude(10.123456);
        customerPhotoDto.setLongitude(76.123456);
        customerPhotoDto.setAccuracy(5.5);
        customerPhotoDto.setCaptureTime(LocalDateTime.of(2025, 7, 23, 12, 0));
        customerPhotoDto.setCaptureDevice("Pixel 6");
        customerPhotoDto.setCaptureBy(101);
        customerPhotoDto.setLocationDescription("Front Gate");
        customerPhotoDto.setCreatedBy(999);
        customerPhotoDto.setUpdatedBy(1001);

         photoModel = new CustomerPhotoModel();
        photoModel.setPhotoId(123);  
        photoModel.setCustomerId(1001);
        photoModel.setPhotoFileId(501);
        photoModel.setLatitude(10.123456);
        photoModel.setLongitude(76.123456);
        photoModel.setAccuracy(5.5);
        photoModel.setCaptureTime(LocalDateTime.of(2025, 7, 23, 12, 0));
        photoModel.setCaptureDevice("Pixel 6");
        photoModel.setCaptureBy(101);
        photoModel.setLocationDescription("Front Gate");
        photoModel.setIsVerified(false);
        photoModel.setVerifiedBy(null);
        photoModel.setVerifiedAt(null);
        photoModel.setUpdatedBy(1001);
    }
    
    @Test
    void addCustomerPhoto_shouldSaveAndReturnPhotoId() {
         when(photoRepo.findTopByCustomerIdOrderByCaptureTimeDesc(1001)).thenReturn(Optional.empty());
        when(photoRepo.save(any(CustomerPhotoModel.class))).thenReturn(photoModel);

        Integer photoId = photoService.addCustomerPhoto(customerPhotoDto);

        assertNotNull(photoId);
        assertEquals(123, photoId);
        verify(photoRepo, times(1)).save(any(CustomerPhotoModel.class));
    }
    
    @Test
    void sofeDeleteCustomerPhoto_success() {
        when(photoRepo.findByPhotoIdAndIsDeleteFalse(123)).thenReturn(photoModel);
         Integer result = photoService.deleteCustomerPhotosForPhotoId(123, 1001);
         assertEquals(photoModel.getPhotoId(),result);
         assertEquals(1001, photoModel.getUpdatedBy());

    }
    
    @Test
    void softDeleteCustomerPhoto_fails() {
    	when(photoRepo.findByPhotoIdAndIsDeleteFalse(123)).thenReturn(null);
    	BusinessException thrown = assertThrows(BusinessException.class,
    			()->photoService.deleteCustomerPhotosForPhotoId( 123,1001));
    	assertEquals("No data found for id: "+photoModel.getPhotoId(),thrown.getMessage());
    }
    
    void getphotoDetailsByPhotoID() {
    	when(photoRepo.findByPhotoIdAndIsDeleteFalse(123)).thenReturn(photoModel);
    	CustomerPhotoDto result = photoService.getCustomerDtoByPhotoId(123);
    	assertEquals("Pixel 6",  photoModel.getCaptureDevice());
    	assertEquals(10.123456, photoModel.getLatitude());
    	assertEquals(76.123456, photoModel.getLongitude());
    }
    
}
