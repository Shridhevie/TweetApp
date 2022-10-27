package com.tweetapp.exception;

import com.tweetapp.model.ResponseForIssue;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
       // log.error("errors {}",errors);
        return errors;
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseForIssue> invalidTokenException(InvalidTokenException invalidTokenException) {
        //log.error(invalidTokenException.getLocalizedMessage());
        return new ResponseEntity<>(
                new ResponseForIssue(invalidTokenException.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TweetNotFoundException.class)
    public ResponseEntity<ResponseForIssue> tweetNotFoundException(TweetNotFoundException ex) {
        //log.error(invalidTokenException.getLocalizedMessage());
        return new ResponseEntity<>(
                new ResponseForIssue(ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ResponseForIssue> microServiceUnavailableException( ) {
        //log.error("user service unavailable");
        return new ResponseEntity<>(
                new ResponseForIssue("MicroServiceUnavailable", LocalDateTime.now(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        //log.error("{}",ex.getStackTrace());
        //log.error("{}",ex);
        return new ResponseEntity<>(
                new ResponseForIssue(ex.getLocalizedMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
