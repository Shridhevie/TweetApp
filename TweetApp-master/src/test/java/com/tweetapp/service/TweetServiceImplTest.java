package com.tweetapp.service;

import com.tweetapp.model.TweetDetails;
import com.tweetapp.repository.TweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TweetServiceImplTest {

    @Mock
    TweetServices tweetServices;

    @Mock
    private TweetRepository tweetRepository;

  // @Test
    void addTweetfail()
    {
        TweetDetails tweetDetails=new TweetDetails();
        tweetDetails.setMessage("this is my first tweet but length of the tweet is exceed 144 length so error message is should be displayed as \"Tweet Message length should not exceed 144 characters\".");
        assertEquals(null,tweetServices.addTweet("Naga123",tweetDetails));
    }

    //@Test
    void addTweetPass()
    {
        TweetDetails tweetDetails=new TweetDetails();
        tweetDetails.setMessage("this is my first tweet.");
        tweetDetails.setStatus(true);
        when(tweetRepository.save(tweetDetails)).thenReturn(tweetDetails);
       // assertEquals(,tweetServices.addTweet("Naga123",tweetDetails));

    }
}
