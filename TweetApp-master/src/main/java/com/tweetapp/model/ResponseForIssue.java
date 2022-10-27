package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseForIssue {
    String message;
    LocalDateTime timestamp;
    HttpStatus status;

}
