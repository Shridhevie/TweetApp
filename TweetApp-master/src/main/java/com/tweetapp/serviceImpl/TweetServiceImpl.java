package com.tweetapp.serviceImpl;

import com.tweetapp.config.AppConfigs;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.ResponseTweet;
import com.tweetapp.model.TweetDetails;
import com.tweetapp.model.UserData;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.service.TweetServices;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetServiceImpl implements TweetServices {

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    RestTemplate restTemplate;


    @Value("${authservice.client.url}")
    public String url;
   
    
    



    public List<ResponseTweet> formatData(List<TweetDetails> list) {
        log.info("inside tweet service Implementation to format data");
        List<ResponseTweet> result=new ArrayList<>();
        //RestTemplate restTemplate=new RestTemplate();
        for(TweetDetails tweet:list) {
            UserData user=restTemplate.getForObject(url+"/apps/v1.0/tweets/find/{username}", UserData.class,tweet.getUsername());

           //UserData user=userData.get(0);
            //List<UserData> user=userData.get(1).;
            ResponseTweet tweet1=new ResponseTweet(tweet.getId(),tweet.getHandleName(),tweet.getMessage(),
                    tweet.getTime(),tweet.getUsername(),tweet.getLikes(),tweet.getReplies(),user.getFirstName(),
                    user.getLastName(),tweet.isStatus());
            result.add(tweet1);
        }
        return result;
    }
    @Override
    public List<ResponseTweet> getAllTweets() {
    	System.out.println(url);
        log.info("inside tweet service Implementation to get all tweets");
        List<TweetDetails> tweets=tweetRepository.findAll().stream().filter(o->o.isStatus()).collect(Collectors.toList());
        return formatData(tweets);

    }

    @Override
    public List<ResponseTweet> getTweetsByUsername(String username){
    	log.info("inside tweet service Implementation to get tweets by username");
        List<TweetDetails> tweets=tweetRepository.findAll().stream().filter(o->o.getUsername().equals(username)&&o.isStatus()).collect(Collectors.toList());
        return formatData(tweets);
    }

    @Override
    public String addTweet(String username, TweetDetails tweetDetails)
    {
    	
        if(tweetDetails.getMessage().length()<=144) {
        	log.info("inside tweet service Implementation to add tweet");
            tweetDetails.setUsername(username);
            tweetDetails.setStatus(true);
            tweetDetails.setTime(LocalDateTime.now());
            tweetRepository.save(tweetDetails);
            return "Tweet Added Successfully Tweet Id - " + tweetDetails.getId();
        }
        return "Tweet Message length should not exceed 144 characters";
    }

    @Override
    public String updateTweet(String username,long id, TweetDetails tweetDetails) throws TweetNotFoundException
    {
    	log.info("inside tweet service Implementation to update tweet data");
        if(tweetDetails.getMessage().length()<=144) {
            Optional<TweetDetails> tweet = tweetRepository.findById(id);
            if (tweet.isPresent() && tweet.get().getUsername().equals(username)) {
                tweet.get().setMessage(tweetDetails.getMessage());
               // tweet.get().setHandleName(tweetDetails.getHandleName());
                tweet.get().setTime(LocalDateTime.now());
                tweetRepository.save(tweet.get());
                return "Tweet " + tweet.get().getId() + " Updated Successfully ";
            }
        }
        else {
            return "Tweet Message length should not exceed 144 characters";
        }

        throw  new TweetNotFoundException("No tweet found with Tweet Id :"+tweetDetails.getId());
    }
    @Override
    public String likeTweet(String username,long id)throws TweetNotFoundException{
        Optional<TweetDetails> tweet=tweetRepository.findById(id);
        log.info("inside tweet service Implementation to like tweet {}",id);
        if(!tweet.isPresent()) {
            throw new TweetNotFoundException("No tweet found with Tweet Id :"+id);
        }
        //if(tweet.get().getLikes()!=null)
         //{
                tweet.get().getLikes().add(username);
                tweet.get().setLikes(tweet.get().getLikes());
        // }
         //else
       //  {
                //List<String> list=new ArrayList<>();
                //list.add(username);
                //tweet.get().setLikes(list);
        // }
        tweetRepository.save(tweet.get());
        return "liked the Tweet";
    }
    @Override
    public String unLikeTweet(String username,long id)throws TweetNotFoundException{
        Optional<TweetDetails> tweet=tweetRepository.findById(id);
        log.info("inside tweet service Implementation to unlike tweet");
        if(!tweet.isPresent()) {
            throw new TweetNotFoundException("No tweet found with Tweet Id :"+id);
        }

            tweet.get().getLikes().remove(username);
            tweet.get().setLikes(tweet.get().getLikes());

        tweetRepository.save(tweet.get());
        return "Un-liked the Tweet";
    }

    @Override
    public String replyTweet(String username,long id,String reply)throws TweetNotFoundException{
    	log.info("inside tweet service Implementation to reply tweet");
        Optional<TweetDetails> tweet=tweetRepository.findById(id);
        if(!tweet.isPresent()) {
            throw new TweetNotFoundException("Tweet not found exception");
        }
        UserData user=restTemplate.getForObject(url+"/apps/v1.0/tweets/find/{username}", UserData.class,username);
        reply=user.getFirstName()+" "+user.getLastName()+"-"+reply;
        if(tweet.get().getReplies()!=null)
            tweet.get().getReplies().add(reply);
        else {
           /* List<TweetDetails> l=new ArrayList<TweetDetails>();
            TweetDetails tweetReply=new TweetDetails();
            tweetReply.setMessage(reply);
            tweetReply.setHandleName("@"+username);
            tweetReply.setStatus(true);*/
            List<String> l=new ArrayList<>();
            l.add(reply);
            tweet.get().setReplies(l);
        }
        tweetRepository.save(tweet.get());
        return "Replied to Tweet Successfully";
    }

    @Override
    public String deleteTweet(String username,long id)throws TweetNotFoundException{
    	log.info("inside tweet service Implementation to delete tweet");
        Optional<TweetDetails> tweet=tweetRepository.findById(id);
        if(!tweet.isPresent()) {
            throw new TweetNotFoundException("Tweet not found exception");
        }
        tweet.get().setStatus(false);
        tweetRepository.delete(tweet.get());
        return "Deleting Tweet Successfully";
    }
    /*@KafkaListener(topics = AppConfigs.topicName,groupId = "delete")
    public ResponseEntity<Object> deleteTweetKafka(long id) throws TweetNotFoundException {
		log.info("inside tweet service Implementation to delete tweet");
		Optional<TweetDetails> tweet=tweetRepository.findById(id);
		if(tweet.isEmpty()) {
			throw new TweetNotFoundException("Tweet not found exception");
		}
		tweet.get().setStatus(false);
		tweetRepository.save(tweet.get());
		return new ResponseEntity<Object>("Deleting Tweet Successfully",HttpStatus.OK);
	}
    /*public String deleteTweetKafka(long id) throws TweetNotFoundException {
        log.info("inside tweet service Implementation to delete tweet");
         Optional<TweetDetails> tweet=tweetRepository.findById(id);
        if(!tweet.isPresent()) {
            throw new TweetNotFoundException("Tweet not found exception");
        }
        tweet.get().setStatus(false);
        tweetRepository.delete(tweet.get());
        return "Deleting Tweet Successfully";
    }*/
}
