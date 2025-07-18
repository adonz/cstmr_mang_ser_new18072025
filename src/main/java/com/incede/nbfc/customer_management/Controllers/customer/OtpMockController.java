package com.incede.nbfc.customer_management.Controllers.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incede.nbfc.customer_management.Services.MockOtpService;



@RestController
@RequestMapping("/mock/otp")

public class OtpMockController {



	    private final MockOtpService mockOtpService;

	    public OtpMockController (MockOtpService mockOtpService ) {
	    	this.mockOtpService =mockOtpService;
	    }
	    @PostMapping("/generate")
	    public ResponseEntity<String> generate(@RequestParam String mobile) {
	    	mockOtpService.generateOtp(mobile);
	        return ResponseEntity.ok("OTP generated (check logs)");
	    }

	    @PostMapping("/verify")
	    public ResponseEntity<String> verify(@RequestParam String mobile, @RequestParam String otp) {
	        boolean valid = mockOtpService.verifyOtp(mobile, otp);
	        return valid
	                ? ResponseEntity.ok("OTP verified successfully")
	                : ResponseEntity.status(401).body("Invalid or expired OTP");
	    }
	}

