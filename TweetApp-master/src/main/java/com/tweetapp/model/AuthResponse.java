package com.tweetapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

    private String username;
    private boolean valid;
    private String token;

    
}
