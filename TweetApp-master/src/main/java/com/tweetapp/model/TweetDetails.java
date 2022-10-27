package com.tweetapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name="tweet_details")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TweetDetails {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String handleName;

    @Column

    private String Message;

    @Column
    private LocalDateTime time;

    @Column
    private String username;

    @ElementCollection
    @CollectionTable(name = "likes",joinColumns = @JoinColumn(name="id"))
    @Column
    private List<String> likes;

    @Column
    @ElementCollection
    @CollectionTable(name="replies",joinColumns =@JoinColumn(name="id"))
    private  List<String> replies;
    

  

    @Column
    @JsonIgnore
    private boolean status;
    

   
}
