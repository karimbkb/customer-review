package com.karimbkb.customerreview.exceptions;

import com.karimbkb.customerreview.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;

@ControllerAdvice
public class ReviewExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ReviewNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleReviewNotFoundException(ReviewNotFoundException e) {
    ExceptionResponse response = new ExceptionResponse();
    response.setErrorCode("NOT_FOUND");
    response.setErrorMessage(e.getMessage());
    response.setTimestamp(LocalDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ReviewDescriptionNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleReviewDescriptionNotFoundException(
      ReviewDescriptionNotFoundException e) {
    ExceptionResponse response = new ExceptionResponse();
    response.setErrorCode("NOT_FOUND");
    response.setErrorMessage(e.getMessage());
    response.setTimestamp(LocalDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConcurrentModificationException.class)
  public ResponseEntity<ExceptionResponse> handleConcurrentModificationException(
          ConcurrentModificationException e) {
    ExceptionResponse response = new ExceptionResponse();
    response.setErrorCode("CONCURRENT_MODIFICATION");
    response.setErrorMessage(e.getMessage());
    response.setTimestamp(LocalDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

//  @ExceptionHandler(UnauthorizedException.class)
//  public ResponseEntity<ExceptionResponse> unauthorizedException(UnauthorizedException ex) {
//    ExceptionResponse response=new ExceptionResponse();
//    response.setErrorCode("UNAUTHORIZED");
//    response.setErrorMessage(ex.getMessage());
//    response.setTimestamp(LocalDateTime.now());
//
//    return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
//  }
}
