package com.amazigh.hettal.springusers.exception;

import jakarta.persistence.criteria.CriteriaBuilder;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(){
        super();
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
