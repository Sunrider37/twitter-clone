package com.example.twitter.service;

import com.example.twitter.repository.projection.TagProjection;
import com.example.twitter.repository.projection.tweet.TweetProjection;

import java.util.List;

public interface TagService {
    List<TagProjection> getTags();

    List<TagProjection> getTrends();

    List<TweetProjection> getTweetsByTag(String tagName);
}