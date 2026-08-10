package com.example.user.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String msz){
        super(msz);
    }
}
