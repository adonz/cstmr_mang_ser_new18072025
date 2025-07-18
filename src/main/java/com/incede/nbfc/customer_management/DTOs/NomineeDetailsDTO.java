package com.incede.nbfc.customer_management.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NomineeDetailsDTO {

    private Integer nomineeId;
    private Integer customerId;
    private String fullName;
    private Integer relationship;
    private LocalDate dob;
    private String contactNumber;
    private Boolean isSameAddress;
    private String houseNumber;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String placeName;
    private String city;
    private String district;
    private String stateName;
    private Integer country;
    private String pincode;
    private BigDecimal percentageShare;
    private Boolean isMinor;
    private String guardianName;
    private UUID identity;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
    

}

