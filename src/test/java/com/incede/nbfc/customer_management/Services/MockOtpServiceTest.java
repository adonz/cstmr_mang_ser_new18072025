package com.incede.nbfc.customer_management.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MockOtpServiceTest {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Duration otpExpiry = Duration.ofMinutes(5);
    private final Map<String, LocalDateTime> otpTimestamps = new ConcurrentHashMap<>();
    private final Set<String> verifiedNumbers = ConcurrentHashMap.newKeySet();

    public void generateOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        otpStore.put(mobile, otp);
        otpTimestamps.put(mobile, LocalDateTime.now());
        System.out.println("🧪 [MOCK OTP] Sent to " + mobile + ": " + otp);
    }

    public boolean verifyOtp(String mobile, String otp) {
        if (!otpStore.containsKey(mobile)) return false;

        String storedOtp = otpStore.get(mobile);
        LocalDateTime createdAt = otpTimestamps.getOrDefault(mobile, LocalDateTime.MIN);

        if (!storedOtp.equals(otp)) return false;
        if (createdAt.plus(otpExpiry).isBefore(LocalDateTime.now())) return false;

        verifiedNumbers.add(mobile);
        otpStore.remove(mobile); // Optional: invalidate after use
        return true;
    }

    public boolean isVerified(String mobile) {
        return verifiedNumbers.contains(mobile);
    }

    // ----- Test helper methods -----

    /**
     * Get the OTP for a mobile number (for testing purposes only).
     */
    public String getOtpForTest(String mobile) {
        return otpStore.get(mobile);
    }

    /**
     * Set OTP creation time manually (for testing expiry).
     */
    public void setOtpTimestampForTest(String mobile, LocalDateTime timestamp) {
        otpTimestamps.put(mobile, timestamp);
    }
}
