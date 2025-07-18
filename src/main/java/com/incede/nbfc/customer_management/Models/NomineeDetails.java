package com.incede.nbfc.customer_management.Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.incede.nbfc.customer_management.BaseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nominee_details", schema = "customers")
public class NomineeDetails extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nominee_id")
    private Integer nomineeId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "relationship", nullable = false)
    private Integer relationship;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "is_same_address")
    private Boolean isSameAddress = false;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "country")
    private Integer country = 1;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "percentage_share")
    private BigDecimal percentageShare = new BigDecimal("100.00");

    @Column(name = "is_minor")
    private Boolean isMinor = false;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

}
