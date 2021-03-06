package com.example.twitter.service;

import com.example.twitter.model.Image;
import com.example.twitter.model.User;
import com.example.twitter.repository.projection.BookmarkProjection;
import com.example.twitter.repository.projection.LikeTweetProjection;
import com.example.twitter.repository.projection.NotificationInfoProjection;
import com.example.twitter.repository.projection.tweet.TweetImageProjection;
import com.example.twitter.repository.projection.tweet.TweetProjection;
import com.example.twitter.repository.projection.tweet.TweetUserProjection;
import com.example.twitter.repository.projection.tweet.TweetsProjection;
import com.example.twitter.repository.projection.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserProfileProjection getUserById(Long userId);

    List<UserProjection> getUsers();

    List<UserProjection> getRelevantUsers();

    List<UserProjection> searchUsersByUsername(String username);

    Boolean startUseTwitter();

    Page<TweetUserProjection> getUserTweets(Long userId, Pageable pageable);

    Page<LikeTweetProjection> getUserLikedTweets(Long userId, Pageable pageable);

    Page<TweetProjection> getUserMediaTweets(Long userId, Pageable pageable);

    Page<TweetUserProjection> getUserRetweetsAndReplies(Long userId, Pageable pageable);

    Map<String, Object> getUserNotifications();

    NotificationInfoProjection getUserNotificationById(Long notificationId);

    Page<TweetsProjection> getNotificationsFromTweetAuthors(Pageable pageable);

    Page<BookmarkProjection> getUserBookmarks(Pageable pageable);

    Boolean processUserBookmarks(Long tweetId);

    Image uploadImage(MultipartFile multipartFile);

    List<TweetImageProjection> getUserTweetImages(Long userId);

    AuthUserProjection updateUserProfile(User userInfo);

    List<UserProjection> getFollowers(Long userId);

    List<UserProjection> getFollowing(Long userId);

    Map<String, Object> processFollow(Long userId);

    List<BaseUserProjection> overallFollowers(Long userId);

    UserProfileProjection processFollowRequestToPrivateProfile(Long userId);

    String acceptFollowRequest(Long userId);

    String declineFollowRequest(Long userId);

    Boolean processSubscribeToNotifications(Long userId);

    Long processPinTweet(Long tweetId);

    List<BlockedUserProjection> getBlockList();

    Boolean processBlockList(Long userId);

    List<MutedUserProjection> getMutedList();

    Boolean processMutedList(Long userId);

    UserDetailProjection getUserDetails(Long userId);

    List<FollowerUserProjection> getFollowerRequests();
}
