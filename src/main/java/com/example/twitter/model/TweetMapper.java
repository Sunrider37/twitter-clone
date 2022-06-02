package com.example.twitter.model;

import com.example.twitter.dto.request.TweetDeleteRequest;
import com.example.twitter.dto.request.TweetRequest;
import com.example.twitter.dto.request.VoteRequest;
import com.example.twitter.dto.response.TweetHeaderResponse;
import com.example.twitter.dto.response.TweetResponse;
import com.example.twitter.dto.response.UserResponse;
import com.example.twitter.dto.response.notification.NotificationReplyResponse;
import com.example.twitter.dto.response.notification.NotificationResponse;
import com.example.twitter.mapper.BasicMapper;
import com.example.twitter.repository.projection.tweet.TweetProjection;
import com.example.twitter.repository.projection.tweet.TweetsProjection;
import com.example.twitter.repository.projection.user.UserProjection;
import com.example.twitter.service.TweetService;
import com.example.twitter.service.impl.TweetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TweetMapper {

    private final BasicMapper basicMapper;
    private final TweetService tweetService;

    public <T, S> TweetHeaderResponse<S> getTweetHeaderResponse(Page<T> pageableTweets, Class<S> type) {
        List<S> tweetResponses = basicMapper.convertToResponseList(pageableTweets.getContent(), type);
        return constructTweetHeaderResponse(tweetResponses, pageableTweets.getTotalPages());
    }

    public <T, S> TweetHeaderResponse<S> getTweetHeaderResponse(List<T> tweets, Integer totalPages, Class<S> type) {
        List<S> tweetResponses = basicMapper.convertToResponseList(tweets, type);
        return constructTweetHeaderResponse(tweetResponses, totalPages);
    }

    private <S> TweetHeaderResponse<S> constructTweetHeaderResponse(List<S> tweetResponses, Integer totalPages) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("page-total-count", String.valueOf(totalPages));
        return new TweetHeaderResponse<S>(tweetResponses, responseHeaders);
    }

    public TweetHeaderResponse<TweetResponse> getTweets(Pageable pageable) {
        Page<TweetProjection> tweets = tweetService.getTweets(pageable);
        return getTweetHeaderResponse(tweets, TweetResponse.class);
    }

    public TweetHeaderResponse<TweetResponse> getMediaTweets(Pageable pageable) {
        Page<TweetProjection> tweets = tweetService.getMediaTweets(pageable);
        return getTweetHeaderResponse(tweets, TweetResponse.class);
    }

    public TweetHeaderResponse<TweetResponse> getTweetsWithVideo(Pageable pageable) {
        Page<TweetProjection> tweets = tweetService.getTweetsWithVideo(pageable);
        return getTweetHeaderResponse(tweets, TweetResponse.class);
    }

    public TweetHeaderResponse<TweetResponse> getFollowersTweets(Pageable pageable) {
        Page<TweetProjection> tweets = tweetService.getFollowersTweets(pageable);
        return getTweetHeaderResponse(tweets, TweetResponse.class);
    }

    public List<TweetResponse> getScheduledTweets() {
        List<TweetProjection> tweets = tweetService.getScheduledTweets();
        return basicMapper.convertToResponseList(tweets, TweetResponse.class);
    }

    public TweetResponse getTweetById(Long tweetId) {
        TweetProjection tweet = tweetService.getTweetById(tweetId);
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public List<TweetResponse> getRepliesByTweetId(Long tweetId) {
        List<TweetProjection> tweets = tweetService.getRepliesByTweetId(tweetId);
        return basicMapper.convertToResponseList(tweets, TweetResponse.class);
    }

    public List<UserResponse> getLikedUsersByTweetId(Long tweetId) {
        List<UserProjection> users = tweetService.getLikedUsersByTweetId(tweetId);
        return basicMapper.convertToResponseList(users, UserResponse.class);
    }

    public List<UserResponse> getRetweetedUsersByTweetId(Long tweetId) {
        List<UserProjection> users = tweetService.getRetweetedUsersByTweetId(tweetId);
        return basicMapper.convertToResponseList(users, UserResponse.class);
    }

    public TweetResponse createTweet(TweetRequest tweetRequest) {
        TweetProjection tweet = tweetService.createNewTweet(basicMapper.convertToEntity(tweetRequest, Tweet.class));
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public TweetResponse createPoll(TweetRequest tweetRequest) {
        TweetProjection tweet = tweetService.createPoll(tweetRequest.getPollDateTime(), tweetRequest.getChoices(), basicMapper.convertToEntity(tweetRequest, Tweet.class));
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public TweetResponse updateScheduledTweet(TweetRequest tweetRequest) {
        TweetProjection tweet = tweetService.updateScheduledTweet(basicMapper.convertToEntity(tweetRequest, Tweet.class));
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public String deleteScheduledTweets(TweetDeleteRequest tweetRequest) {
        return tweetService.deleteScheduledTweets(tweetRequest.getTweetsIds());
    }

    public TweetResponse deleteTweet(Long tweetId) {
        Tweet tweet = tweetService.deleteTweet(tweetId);
        TweetResponse tweetResponse = basicMapper.convertToResponse(tweet, TweetResponse.class);
        tweetResponse.setTweetDeleted(true);
        return tweetResponse;
    }

    public NotificationResponse likeTweet(Long tweetId) {
        Map<String, Object> notificationDetails = tweetService.likeTweet(tweetId);
        NotificationResponse notification = basicMapper.convertToResponse(notificationDetails.get("notification"), NotificationResponse.class);
        notification.getTweet().setNotificationCondition((Boolean) notificationDetails.get("isTweetLiked"));
        return notification;
    }

    public NotificationResponse retweet(Long tweetId) {
        Map<String, Object> notificationDetails = tweetService.retweet(tweetId);
        NotificationResponse notification = basicMapper.convertToResponse(notificationDetails.get("notification"), NotificationResponse.class);
        notification.getTweet().setNotificationCondition((Boolean) notificationDetails.get("isTweetRetweeted"));
        return notification;
    }

    public List<TweetResponse> searchTweets(String text) {
        List<TweetProjection> tweets = tweetService.searchTweets(text);
        return basicMapper.convertToResponseList(tweets, TweetResponse.class);
    }

    public NotificationReplyResponse replyTweet(Long tweetId, TweetRequest tweetRequest) {
        TweetProjection tweet = tweetService.replyTweet(tweetId, basicMapper.convertToEntity(tweetRequest, Tweet.class));
        TweetResponse replyTweet = basicMapper.convertToResponse(tweet, TweetResponse.class);
        NotificationReplyResponse notificationReplyResponse = new NotificationReplyResponse();
        notificationReplyResponse.setTweetId(tweetId);
        notificationReplyResponse.setNotificationType(NotificationType.REPLY);
        notificationReplyResponse.setTweet(replyTweet);
        return notificationReplyResponse;
    }

    public TweetResponse quoteTweet(Long tweetId, TweetRequest tweetRequest) {
        TweetProjection tweet = tweetService.quoteTweet(tweetId, basicMapper.convertToEntity(tweetRequest, Tweet.class));
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public TweetResponse changeTweetReplyType(Long tweetId, ReplyType replyType) {
        TweetProjection tweet = tweetService.changeTweetReplyType(tweetId, replyType);
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public TweetResponse voteInPoll(VoteRequest voteRequest) {
        TweetProjection tweet = tweetService.voteInPoll(voteRequest.getTweetId(), voteRequest.getPollId(), voteRequest.getPollChoiceId());
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }
}