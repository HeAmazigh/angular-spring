package com.amazigh.hettal.springusers.dto;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
  private Date timestamp;
  private String message;
  private String details;
  private HttpStatus httpStatus;

  public ErrorDetails(Date timestamp, String message, String details, HttpStatus httpStatu) {
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
    this.httpStatus = httpStatu;
  }

  public ErrorDetails(Date timestamp, String message, String details) {
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
