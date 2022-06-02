package com.example.twitter.repository;

import com.example.twitter.model.Retweet;
import com.example.twitter.repository.projection.tweet.RetweetProjection;
import com.example.twitter.repository.projection.tweet.RetweetsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    @Query("SELECT r AS retweet FROM Retweet r WHERE r.user.id = :userId ORDER BY r.retweetDate DESC")
    List<RetweetsProjection> findRetweetsByUserId(Long userId);
}
