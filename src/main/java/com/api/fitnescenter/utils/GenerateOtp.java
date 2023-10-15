package com.api.fitnescenter.utils;

import java.security.SecureRandom;

public class GenerateOtp {
    public String generateOTP() {
        // Generate a 6-digit OTP Random
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
