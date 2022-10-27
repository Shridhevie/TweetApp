package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ResponseTweet {
    private long id;
    private String handleName;
    private String message;
    private LocalDateTime time;
    private String username;
    private List<String> likes;
    private List<String> replies;
    private String fname;
    private String lname;
    private boolean Status;

    
}
