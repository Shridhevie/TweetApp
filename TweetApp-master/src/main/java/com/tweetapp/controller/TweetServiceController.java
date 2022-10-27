package com.tweetapp.controller;

import com.tweetapp.config.AppConfigs;
import com.tweetapp.config.AuthFeign;
import com.tweetapp.exception.InvalidTokenException;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.ResponseTweet;
import com.tweetapp.model.TweetDetails;
import com.tweetapp.model.UserData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.tweetapp.service.TweetServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RestController
@RequestMapping("/apps/v1.0/tweets")
@Slf4j
public class TweetServiceController {


    @Autowired
    AuthFeign authFeign;

    @Autowired
    TweetServices tweetservice;

    //@Autowired
    //private KafkaTemplate<String,Long> kafkaTemplate;

    @GetMapping("/all")
    @Operation(summary = "Getting all tweets",description = "A Get request for all tweets",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully fetched all Tweets"),
            @ApiResponse(responseCode = "400",description = "No Tweets Found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> getAllTweets(@RequestHeader("Authorization") String token) throws InvalidTokenException{
        log.info("inside tweet service controller to get all tweets");
        if(authFeign.getValidity(token).getBody().isValid()) {
            List<ResponseTweet> tweetsList=tweetservice.getAllTweets();
            //if(!tweetsList.isEmpty())
                return new ResponseEntity<>(tweetsList, HttpStatus.OK);
           // else
             //   return new ResponseEntity<>("No Tweets!!!,Let's Starts with new tweet ", HttpStatus.OK);

        }
        log.info("inside tweet service controller ,token expired login again");
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");
    }

    @GetMapping("/{username}/tweets")
    @Operation(summary = "Getting all user tweets by username",description = "A Get request for all user tweets by username",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully fetched all user Tweets"),
            @ApiResponse(responseCode = "400",description = "No Tweets Found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> getTweetsByUsername(@RequestHeader("Authorization") String token,@PathVariable String username) throws InvalidTokenException{
        log.info("inside tweet service controller to get all tweets");
        if(authFeign.getValidity(token).getBody().isValid() &&authFeign.getValidity(token).getBody().getUsername().equals(username)) {
            List<ResponseTweet> tweetsList=tweetservice.getTweetsByUsername(username);
            //if(!tweetsList.isEmpty())
                return new ResponseEntity<>(tweetsList, HttpStatus.OK);
           // else
             //   return new ResponseEntity<>("No Tweets!!!,Let's Starts with new tweet ", HttpStatus.OK);

        }

        log.info("inside tweet service controller ,token expired login again");
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");
    }

    @PostMapping("/{username}/addTweet")
    @Operation(summary = "Tweet Added Succesfully",description = "A Post request for tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Tweet Added Successfully along with Tweet Id"),
            @ApiResponse(responseCode = "400",description = "Tweet is not added"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> addTweet(@RequestHeader("Authorization") String token, @PathVariable String username, @RequestBody TweetDetails tweetDetails)throws InvalidTokenException{

        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username))
        {
            String message=tweetservice.addTweet(username, tweetDetails);
            if(message!="Tweet Message length should not exceed 144 characters")
                return new ResponseEntity<>(message,HttpStatus.CREATED);
            else
                return new ResponseEntity<>(message,HttpStatus.FORBIDDEN);
        }
		throw new InvalidTokenException("Token Expired or Invalid , Login again ...");
}

    @PutMapping("/{username}/updateTweet/{id}")
    @Operation(summary = "Updating a Posted Tweet",description = "A Put request for updating tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Tweet Updated Successfully along with Tweet Id"),
            @ApiResponse(responseCode = "400",description = "Tweet not found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> updateTweet(@RequestHeader("Authorization") String token,@PathVariable("username") String username,@PathVariable("id") long id,@RequestBody TweetDetails tweetDetails) throws TweetNotFoundException,InvalidTokenException{

        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username)) {

            String message = tweetservice.updateTweet(username, id, tweetDetails);
            if (message != "Tweet Message length should not exceed 144 characters")
                return new ResponseEntity<>(message, HttpStatus.OK);
            else
                return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");

    }

    @PostMapping("/{username}/like/{id}")
    @Operation(summary = "Like Tweet",description = "A Post request for like the tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Tweet liked Successfully"),
            @ApiResponse(responseCode = "400",description = "Tweet not found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> likeTweet(@RequestHeader("Authorization") String token,@PathVariable("username") String username,@PathVariable("id") long id) throws TweetNotFoundException,InvalidTokenException{

        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username)) {
            return new ResponseEntity<>(tweetservice.likeTweet(username,id),HttpStatus.OK);
        }
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");

    }
    @PutMapping("/{username}/unlike/{id}")
    @Operation(summary = "Unlike Tweet",description = "A Put request for Unlike the tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Tweet Unliked Successfully"),
            @ApiResponse(responseCode = "400",description = "Tweet not found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> unlikeTweet(@RequestHeader("Authorization") String token,@PathVariable("username") String username,@PathVariable("id") long id) throws TweetNotFoundException,InvalidTokenException{

        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username)) {
            return new ResponseEntity<>(tweetservice.unLikeTweet(username,id),HttpStatus.OK);
        }
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");

    }

    @PostMapping("/{username}/reply/{id}")
    @Operation(summary = "Reply Tweet",description = "A Post request for reply the tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Replied Successfully"),
            @ApiResponse(responseCode = "400",description = "Tweet not found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> replyTweet(@RequestHeader("Authorization") String token,@PathVariable("username") String username,@PathVariable("id") long id,@RequestBody String reply) throws TweetNotFoundException,InvalidTokenException{

        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username)) {
            return new ResponseEntity<>(tweetservice.replyTweet(username,id,reply),HttpStatus.OK);
        }
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");

    }

    @DeleteMapping("/{username}/delete/{id}")
    @Operation(summary = "Delete Tweet",description = "A Delete request for deleting the tweet",tags = {"Tweet Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Tweet deleted Successfully"),
            @ApiResponse(responseCode = "400",description = "Tweet not found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object> deleteTweet(@RequestHeader("Authorization") String token,
                                              @PathVariable("username") String username,@PathVariable("id") long id) throws TweetNotFoundException, InvalidTokenException {

    	log.info("deleted");
		log.info("inside tweet service controller to delete tweets");
        /*Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
*/
        if(authFeign.getValidity(token).getBody().isValid()&&authFeign.getValidity(token).getBody().getUsername().equals(username)) {
           // kafkaTemplate.send(AppConfigs.topicName,"delete", id);

            //return new ResponseEntity<Object>("Deleted Successfully",HttpStatus.OK);
           return new ResponseEntity<>(tweetservice.deleteTweet(username,id),HttpStatus.OK);
        }
        throw new InvalidTokenException("Token Expired or Invalid , Login again ...");

    }







}
