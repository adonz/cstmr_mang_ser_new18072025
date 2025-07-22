package com.incede.nbfc.customer_management.Enums;

import lombok.Data;

@Data
public class CustomerFreezeActionsEnum {
	public static final String ACTIVE = "ACTIVE";
    public static final String LIFTED = "LIFTED";
    
    public static final String TYPE_PARTIAL = "PARTIAL";
    public static final String TYPE_FULL = "FULL";


    private CustomerFreezeActionsEnum() {
        // private constructor to prevent instantiation
    }

}
