package com.amazigh.hettal.springusers.exception;

import com.amazigh.hettal.springusers.dto.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAddressAlreadyExistsException.class)
  public ResponseEntity<ErrorDetails> handleEmailAddressAlreadyExistsException(EmailAddressAlreadyExistsException exception, WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        exception.getMessage(),
        webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException exception, WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        exception.getMessage(),
        webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAuthenticationException.class)
  public ResponseEntity<ErrorDetails> handleUserAuthenticationException(UserAuthenticationException exception, WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        exception.getMessage(),
        webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorDetails> handleUserAuthenticationException(InvalidPasswordException exception, WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        exception.getMessage(),
        webRequest.getDescription(false)
    );
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

   @ExceptionHandler(HttpMessageNotReadableException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public ResponseEntity<String> parsingError(HttpMessageNotReadableException ex){
     String errorMessage = "Error occurred while parsing the request body: " + ex.getMessage();
     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    // create Map to send errors as a key - value
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex){
    String errorMessage = "Bad credentials";
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex){
    String errorMessage = "Bad credentials";
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  // Handle Global Exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        exception.getMessage(),
        webRequest.getDescription(false)
    );
    return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
