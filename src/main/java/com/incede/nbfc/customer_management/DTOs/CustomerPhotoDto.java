package com.incede.nbfc.customer_management.DTOs;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerPhotoDto {
	
 	private Integer photoId;
	
 	private Integer customerId;
	
// 	@NotNull(message="photo file path should not be null")
// 	private String photoFilePath;
 	
 	@NotNull(message="photo file id should not be null")
 	private Integer photoFileId;
	
 	@NotNull(message="latitude should not be null")
 	private Double latitude;
	
 	@NotNull(message="longitude should not be null")
 	private Double longitude;
 	
	private Double accuracy;
	
 	private LocalDateTime captureTime;
	
 	private String captureDevice;
	
 	@NotNull(message="captured by should not be null")
 	private Integer captureBy;
	
 	private String LocationDescription;
	
 	private Boolean isVerified;
	
 	private Integer verifiedBy;
	
 	private LocalDateTime verifiedAt;
 	
 	private Boolean isDelete;
 	
 	private Integer createdBy;
 	
 	private LocalDateTime createdAt;
 	
 	private Integer updatedBy;
 	
 	private LocalDateTime updatedAt;

}
