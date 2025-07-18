package com.incede.nbfc.customer_management.Models;

import java.util.UUID;

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
@Table(name="customer_addresses", schema="customers")
public class CustomerAddressesModel extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="address_id")
	private Integer addressId;
	
	@Column(name="customer_id", nullable= false)
	private Integer customerId;
	
	@Column(name="address_type", nullable = false)
	private String addressType;
	
	@Column(name="door_number")
	private String doorNumber;
	
	@Column(name="address_line1")
	private String addressLineOne;
	
	@Column(name="address_line2")
	private String addressLineTwo;
	
	@Column(name="landmark")
	private String  landMark;
	
	@Column(name="place_name")
	private String placeName;
	
	@Column(name="city")
	private String city;
	
	@Column(name="district")
	private String district;
	
	@Column(name="state_name")
	private String stateName;
	
	@Column(name="country")
	private Integer country;
	
	@Column(name="pincode")
	private String pincode;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="identity")
	private UUID identity;
	

}
