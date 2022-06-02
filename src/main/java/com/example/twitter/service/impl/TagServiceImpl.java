package com.example.twitter.service.impl;

import com.example.twitter.repository.TagRepository;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.projection.TagProjection;
import com.example.twitter.repository.projection.tweet.TweetProjection;
import com.example.twitter.repository.projection.tweet.TweetsProjection;
import com.example.twitter.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TweetRepository tweetRepository;

    @Override
    public List<TagProjection> getTags() {
        return tagRepository.findTop5OrderByTweetsQuantityDesc();
    }

    @Override
    public List<TagProjection> getTrends() {
        return tagRepository.findByOrderByTweetsQuantityDesc();
    }

    @Override
    public List<TweetProjection> getTweetsByTag(String tagName) {
        return tweetRepository.getTweetsByTagName(tagName).stream().map(
                TweetsProjection::getTweet
        ).toList();
    }
}
