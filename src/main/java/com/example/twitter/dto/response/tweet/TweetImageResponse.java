package com.example.twitter.dto.response.tweet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetImageResponse {
    private Long tweetId;
    private Long imageId;
    private String src;
}