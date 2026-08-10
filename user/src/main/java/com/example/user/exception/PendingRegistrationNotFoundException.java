package com.example.user.exception;

public class PendingRegistrationNotFoundException extends RuntimeException{
    public PendingRegistrationNotFoundException(String msz){
        super(msz);
    }
}
