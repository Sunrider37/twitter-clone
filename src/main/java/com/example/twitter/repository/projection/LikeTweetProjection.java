package com.example.twitter.repository.projection;

import com.example.twitter.repository.projection.tweet.TweetProjection;

import java.time.LocalDateTime;

public interface LikeTweetProjection {

    Long getId();
    LocalDateTime getBookmarkDate();
    TweetProjection getTweet();
}
