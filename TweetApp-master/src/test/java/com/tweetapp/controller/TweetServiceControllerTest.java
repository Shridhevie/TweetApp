package com.tweetapp.controller;

import com.tweetapp.config.AuthFeign;
import com.tweetapp.exception.InvalidTokenException;
import com.tweetapp.model.ResponseTweet;
import com.tweetapp.model.TweetDetails;
import com.tweetapp.service.TweetServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class TweetServiceControllerTest {

    @Mock
    TweetServices tweetServices;

    @InjectMocks
    TweetServiceController tweetServiceController;

    @Mock
    AuthFeign authFeign;


   /* @Test
    void getAllTweets() throws InvalidTokenException,NullPointerException
    {

        ResponseTweet responseTweet=new ResponseTweet(1,"@Naga123","This is my Tweet",null,"Naga123", new ArrayList<String>(),new ArrayList<String>(),null,null,true);
        List<ResponseTweet> list=new ArrayList<>();
        list.add(responseTweet);
        when(authFeign.getValidity("Bearer token").getBody().isValid()).thenReturn(true);
        when(tweetServices.getAllTweets()).thenReturn(list);
        assertEquals(200, tweetServiceController.getAllTweets("Bearer token", "Naga123").getStatusCodeValue());
    }*/

   //@Test
    void getTweetsByUsername()
   {

   }
}
