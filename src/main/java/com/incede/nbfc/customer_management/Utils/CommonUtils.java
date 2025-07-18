package com.incede.nbfc.customer_management.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

	public static String trimString(String input) {
        return input == null ? null : input.trim();
    }

	public static Date formatDate(Date date) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String formatted = sdf.format(date);
	        return sdf.parse(formatted);
	    } catch (Exception e) {
	        return null; // or throw a custom exception if needed
	    }
	}
}
