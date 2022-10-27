package com.tweetapp.service;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.ResponseTweet;

import com.tweetapp.model.TweetDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TweetServices {


    public List<ResponseTweet> getAllTweets();
    public List<ResponseTweet> getTweetsByUsername(String username);
    public String addTweet(String username, TweetDetails tweetDetails);
    public String updateTweet(String username, long id, TweetDetails tweetDetails) throws TweetNotFoundException;
    public String likeTweet(String username,long id)throws TweetNotFoundException;
    public String unLikeTweet(String username,long id)throws TweetNotFoundException;
    public String replyTweet(String username,long id,String reply)throws TweetNotFoundException;
    public String deleteTweet(String username,long id)throws TweetNotFoundException;
}
