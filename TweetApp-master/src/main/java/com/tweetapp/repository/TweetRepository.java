package com.tweetapp.repository;

import com.tweetapp.model.TweetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TweetRepository extends JpaRepository<TweetDetails,Long> {
    Optional<TweetDetails> findByUsername(String username);

}
