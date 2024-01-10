package com.amazigh.hettal.springusers.exception;

public class EmailAddressAlreadyExistsException extends RuntimeException{
    public EmailAddressAlreadyExistsException(String message){
        super(message);
    }
}
