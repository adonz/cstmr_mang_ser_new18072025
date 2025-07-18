package com.incede.nbfc.customer_management.Models;

import java.time.LocalDateTime;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="customer_photos", schema="customers")
public class CustomerPhotoModel extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="photo_id")
	private Integer photoId;
	
	@Column(name="customer_id")
	private Integer customerId;
	
	@Column(name="photo_file_path", nullable=false)
	private String photoFilePath;
	
	@Column(name="latitude", nullable = false)
	private Double latitude;
	
	@Column(name="longitude", nullable = false)
	private Double longitude;
	
	@Column(name="accuracy")
	private Double accuracy;
	
	@Column(name="capture_time")
	private LocalDateTime captureTime;
	
	@Column(name="capture_device")
	private String captureDevice;
	
	@Column(name="captured_by", nullable = false)
	private Integer captureBy;
	
	@Column(name="location_desc")
	private String LocationDescription;
	
	@Column(name="is_verified")
	private Boolean isVerified;
	
	@Column(name="verified_by")
	private Integer verifiedBy;
	
	@Column(name="verified_at")
	private LocalDateTime verifiedAt;
	

}
