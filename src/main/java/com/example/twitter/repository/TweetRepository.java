package com.example.twitter.repository;

import com.example.twitter.model.Tweet;
import com.example.twitter.repository.projection.tweet.*;
import com.example.twitter.repository.projection.user.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query("SELECT tweet FROM Tweet tweet WHERE tweet.id = :tweetId")
    Optional<TweetProjection> findTweetById(Long tweetId);

    @Query("SELECT tweet FROM Tweet tweet " +
            "LEFT JOIN tweet.user user " +
            "WHERE user.id IN :userIds " +
            "AND tweet.addressedUsername IS NULL " +
            "ORDER BY tweet.dateTime DESC")
    Page<TweetProjection> findTweetsByUserIds(List<Long> userIds, Pageable pageable);
    @Query("select tweet from Tweet tweet where tweet.addressedUsername is NULL and tweet.scheduledDate is NULL " +
            "order by tweet.dateTime desc")
    Page<TweetProjection> findAllTweets(Pageable pageable);

    @Query("SELECT rp as tweet FROM Tweet t " +
            "LEFT JOIN t.replies rp " +
            "WHERE t.id = :tweetId " +
            "ORDER BY rp.dateTime DESC")
    List<TweetsProjection> getRepliesByTweetId(Long tweetId);

    @Query("SELECT t as tweet FROM Tweet t " +
            "WHERE t.user.id = :userId " +
            "AND t.addressedUsername IS NULL " +
            "AND t.scheduledDate IS NULL " +
            "ORDER BY t.dateTime DESC")
    List<TweetsUserProjection> findTweetsByUserId(Long userId);
    @Query("SELECT pinnedTweet as tweet FROM User user LEFT JOIN user.pinnedTweet pinnedTweet WHERE user.id = :userId")
    Optional<TweetsUserProjection> getPinnedTweetByUserId(Long userId);
    @Query("SELECT t as tweet FROM Tweet t " +
            "WHERE t.user.id = :userId " +
            "AND t.addressedUsername IS NOT NULL " +
            "AND t.scheduledDate IS NULL " +
            "ORDER BY t.dateTime DESC")
    Optional<TweetsUserProjection> findRepliesByUserId(Long userId);
    @Query("SELECT CASE WHEN count(user) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "LEFT JOIN user.likedTweets likedTweet " +
            "WHERE user.id = :userId " +
            "AND likedTweet.tweet.id = :tweetId")
    boolean isUserLikedTweet(Long userId, Long tweetId);

    @Query("SELECT CASE WHEN count(user) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "LEFT JOIN user.retweets retweets " +
            "WHERE user.id = :userId " +
            "AND retweets.tweet.id = :tweetId")
    boolean isUserRetweetedTweet(Long userId, Long tweetId);

    @Query("SELECT CASE WHEN count(user) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "LEFT JOIN user.bookmarks bookmark " +
            "WHERE user.id = :userId " +
            "AND bookmark.tweet.id = :tweetId")
    boolean isUserBookmarkedTweet(Long userId, Long tweetId);
    @Query("SELECT notificationTweet as tweet " +
            "FROM User user " +
            "LEFT JOIN user.notifications notification " +
            "LEFT JOIN notification.tweet notificationTweet " +
            "WHERE user.id = :userId " +
            "AND notification.notificationType = 'TWEET' " +
            "ORDER BY notificationTweet.dateTime DESC")
    List<TweetsProjection> getNotificationsFromTweetAuthors(Long userId);
    @Query("SELECT tweet.id AS tweetId, image.id AS imageId, image.src AS src FROM Tweet tweet " +
            "LEFT JOIN tweet.images image " +
            "WHERE tweet.scheduledDate IS NULL " +
            "AND tweet.user.id = :userId " +
            "AND image.id IS NOT NULL " +
            "ORDER BY tweet.dateTime DESC")
    List<TweetImageProjection> findUserTweetImages(Long userId, Pageable pageable);
    @Query("SELECT tweet FROM Tweet tweet " +
            "LEFT JOIN tweet.images image " +
            "WHERE tweet.scheduledDate IS NULL " +
            "AND (tweet.user.id = :userId AND image.id IS NOT NULL) " +
            "OR (tweet.user.id = :userId AND tweet.text LIKE CONCAT('%','youtu','%')) " +
            "ORDER BY tweet.dateTime DESC")
    Page<TweetProjection> findAllUserMediaTweets(Long userId, Pageable pageable);
    @Query("SELECT user FROM User user " +
            "LEFT JOIN user.likedTweets likedTweet " +
            "LEFT JOIN likedTweet.tweet tweet " +
            "WHERE tweet.id = :tweetId " +
            "ORDER BY likedTweet.likeTweetDate DESC")
    List<UserProjection> getLikedUsersByTweetId(Long tweetId);

    @Query("SELECT user FROM User user " +
            "LEFT JOIN user.retweets retweet " +
            "LEFT JOIN retweet.tweet tweet " +
            "WHERE tweet.id = :tweetId " +
            "ORDER BY retweet.retweetDate DESC")
    List<UserProjection> getRetweetedUsersByTweetId(Long tweetId);
    @Query("SELECT tweet FROM Tweet tweet WHERE tweet.quoteTweet.id = :quoteId")
    List<Tweet> findByQuoteTweetId(Long quoteId);

    @Query("SELECT t as tweet FROM Tweet t " +
            "LEFT JOIN t.user u " +
            "WHERE t.scheduledDate IS NULL " +
            "AND (t.text LIKE CONCAT('%',:text,'%') " +
            "AND UPPER(u.fullName) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(u.username) LIKE UPPER(CONCAT('%',:text,'%'))) " +
            "OR (t.text LIKE CONCAT('%',:text,'%') " +
            "OR UPPER(u.fullName) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(u.username) LIKE UPPER(CONCAT('%',:text,'%'))) " +
            "ORDER BY t.dateTime DESC")
    List<TweetsProjection> findAllByText(String text);
    @Query("select tweet from Tweet tweet where tweet.scheduledDate is null and tweet.images.size != 0 " +
            "order by tweet.dateTime desc")
    Page<TweetProjection> findAllTweetsWithImages(Pageable pageable);

    @Query("select tweet from Tweet tweet where tweet.scheduledDate is null " +
            "and tweet.text like CONCAT('%','youtu','%') order by tweet.dateTime desc")
    Page<TweetProjection> findAllTweetsWithVideo(Pageable pageable);
    @Query("select tweet from Tweet tweet where tweet.user.id = :userId and tweet.scheduledDate is not null " +
            "order by tweet.scheduledDate desc")
    List<TweetProjection> findAllScheduledTweetsByUserId(Long userId);

    @Query("select tagTweet as tweet from Tag tag left join tag.tweets " +
            "tagTweet where tag.tagName = :tagName " +
            "order by tagTweet.dateTime desc")
    List<TweetsProjection> getTweetsByTagName(String tagName);
}
