package com.amazigh.hettal.springusers.exception;

public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException(String message){
        super(message);
    }
}
