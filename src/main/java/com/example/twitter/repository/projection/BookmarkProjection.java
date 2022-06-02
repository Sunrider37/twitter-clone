package com.example.twitter.repository.projection;

import com.example.twitter.repository.projection.tweet.TweetProjection;

import java.time.LocalDateTime;

public interface BookmarkProjection {
    Long getId();
    LocalDateTime getLikeTweetDate();
    TweetProjection getTweet();
}
