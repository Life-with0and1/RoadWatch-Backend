package com.example.user.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 4;

    public static String generateOTP(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<OTP_LENGTH;i++){
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
}
