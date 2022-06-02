package com.example.twitter.repository.projection.tweet;

import java.time.LocalDateTime;

public interface RetweetProjection {
    Long getId();
    LocalDateTime getRetweetDate();
    TweetUserProjection getTweet();
}
