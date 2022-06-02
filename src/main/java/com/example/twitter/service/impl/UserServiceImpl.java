package com.example.twitter.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.twitter.exception.ApiRequestException;
import com.example.twitter.model.*;
import com.example.twitter.repository.*;
import com.example.twitter.repository.projection.BookmarkProjection;
import com.example.twitter.repository.projection.LikeTweetProjection;
import com.example.twitter.repository.projection.NotificationInfoProjection;
import com.example.twitter.repository.projection.notification.NotificationProjection;
import com.example.twitter.repository.projection.tweet.*;
import com.example.twitter.repository.projection.user.*;
import com.example.twitter.service.AuthenticationService;
import com.example.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final TweetRepository tweetRepository;
    private final RetweetRepository retweetRepository;
    private final ImageRepository imageRepository;
    private final BookmarkRepository bookmarkRepository;

    private final AmazonS3 amazonS3Client;
    private final LikeTweetRepository likeTweetRepository;

    private final NotificationRepository notificationRepository;
    @Value("${amazon.s3.bucket.name}")
    private String bucketName;
    @Override
    public UserProfileProjection getUserById(Long userId) {
        return userRepository.getUserProfileById(userId)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<UserProjection> getUsers(){
        Long userId = authenticationService.getAuthenticatedUserId();
        return userRepository.findByActiveTrueAndIdNot(userId);
    }
@Override
    public List<UserProjection> getRelevantUsers() {
        return userRepository.findTop5ByActiveTrue();
    }
    @Override
    public List<UserProjection> searchUsersByUsername(String text) {
        return userRepository.findByFullNameOrUsername(text);
    }

    @Transactional
    @Override
    public Boolean startUseTwitter() {
        Long userId = authenticationService.getAuthenticatedUserId();
        userRepository.updateProfileStarted(userId);
        return true;
    }
    @Override
    public Page<TweetUserProjection> getUserTweets(Long userId, Pageable pageable){
        checkIfUserExists(userId);
        List<TweetUserProjection> tweets = tweetRepository.findTweetsByUserId(userId).stream()
                .map(TweetsUserProjection::getTweet).toList();
        List<RetweetProjection> retweets = retweetRepository.findRetweetsByUserId(userId)
                .stream().map(RetweetsProjection::getRetweet).toList();
        List<TweetUserProjection> userTweets = combineTweetsArrays(tweets, retweets);
        Optional<TweetsUserProjection> pinnedTweet = tweetRepository.getPinnedTweetByUserId(userId);
        if (pinnedTweet.isPresent()) {
            boolean isTweetExist = userTweets.removeIf(tweet -> tweet.getId().equals(pinnedTweet.get().getTweet().getId()));

            if (isTweetExist) {
                userTweets.add(0, pinnedTweet.get().getTweet());
            }
        }
        return getPageableTweetProjectionList(pageable, userTweets, tweets.size() + retweets.size());
    }

    @Override
    public Page<LikeTweetProjection> getUserLikedTweets(Long userId, Pageable pageable) {
        checkIfUserExists(userId);
        return likeTweetRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<TweetProjection> getUserMediaTweets(Long userId, Pageable pageable) {
        checkIfUserExists(userId);
        return tweetRepository.findAllUserMediaTweets(userId, pageable);
    }
    @Override
    public Page<TweetUserProjection> getUserRetweetsAndReplies(Long userId, Pageable pageable){
        checkIfUserExists(userId);
        List<TweetUserProjection> replies = tweetRepository.findRepliesByUserId(userId).stream().map(TweetsUserProjection::getTweet).toList();
        List<RetweetProjection> retweets = retweetRepository.findRetweetsByUserId(userId).stream()
                .map(RetweetsProjection::getRetweet).toList();
        List<TweetUserProjection> userTweets = combineTweetsArrays(replies, retweets);
        return getPageableTweetProjectionList(pageable, userTweets, replies.size() + retweets.size());
    }
    @Override
    public Map<String,Object> getUserNotifications(){
        User user = authenticationService.getAuthenticatedUser();
        user.setNotificationsCount(0L);
        List<NotificationProjection> notifications = notificationRepository.getNotificationsByUserId(user.getId());
        List<TweetAuthorProjection> tweetAuthors = userRepository.getNotificationsTweetAuthors(user.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notifications);
        response.put("tweetAuthors", tweetAuthors);
        return response;
    }

    @Override
    public NotificationInfoProjection getUserNotificationById(Long notificationId) {
        Long userId = authenticationService.getAuthenticatedUserId();
        return notificationRepository.getUserNotificationById(userId, notificationId)
                .orElseThrow(() -> new ApiRequestException("Notification not found", HttpStatus.NOT_FOUND));
    }


    @Override
    public Page<TweetsProjection> getNotificationsFromTweetAuthors(Pageable pageable) {
        Long userId = authenticationService.getAuthenticatedUserId();
        List<TweetsProjection> tweets = tweetRepository.getNotificationsFromTweetAuthors(userId);
        return getPageableTweetProjectionList(pageable, tweets, tweets.size());
    }

    @Override
    public Page<BookmarkProjection> getUserBookmarks(Pageable pageable) {
        Long userId = authenticationService.getAuthenticatedUserId();
        return bookmarkRepository.findByUser(userId, pageable);
    }

    @Override
    public Boolean processUserBookmarks(Long tweetId) {
        User user = authenticationService.getAuthenticatedUser();
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ApiRequestException("Tweet not found", HttpStatus.NOT_FOUND));
        List<Bookmark> bookmarks = user.getBookmarks();
        Optional<Bookmark> bookmark = bookmarks.stream()
                .filter(b -> b.getTweet().getId().equals(tweet.getId()))
                .findFirst();
        if (bookmark.isPresent()) {
            bookmarks.remove(bookmark.get());
            bookmarkRepository.delete(bookmark.get());
            return false;
        } else {
            Bookmark newBookmark = new Bookmark();
            newBookmark.setTweet(tweet);
            newBookmark.setUser(user);
            bookmarkRepository.save(newBookmark);
            bookmarks.add(newBookmark);
            return true;
        }
    }

    @Override
    @Transactional
    public Image uploadImage(MultipartFile multipartFile) {
        Image image = new Image();
        if (multipartFile != null) {
            File file = new File(multipartFile.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
            image.setSrc(amazonS3Client.getUrl(bucketName, fileName).toString());
            file.delete();
        }
        return imageRepository.save(image);
    }


    @Override
    public List<TweetImageProjection> getUserTweetImages(Long userId) {
        return tweetRepository.findUserTweetImages(userId, PageRequest.of(0, 6));
    }

    @Override
    @Transactional
    public AuthUserProjection updateUserProfile(User userInfo) {
        if(userInfo.getUsername().length() == 0 || userInfo.getUsername().length() > 50){
            throw new ApiRequestException("Incorrect username length", HttpStatus.BAD_REQUEST);
        }
        User user = authenticationService.getAuthenticatedUser();
        if(userInfo.getAvatar() != null){
            user.setAvatar(userInfo.getAvatar());
            imageRepository.save(userInfo.getAvatar());
        }
        if (userInfo.getWebsite() != null){
            user.setWallpaper(userInfo.getWallpaper());
            imageRepository.save(userInfo.getWallpaper());
        }
        user.setUsername(userInfo.getUsername());
        user.setAbout(userInfo.getAbout());
        user.setLocation(userInfo.getLocation());
        user.setWebsite(userInfo.getWebsite());
        user.setProfileCustomized(true);
        return userRepository.findAuthUserById(user.getId());
    }

    @Override
    public List<UserProjection> getFollowers(Long userId) {
        checkIfUserExists(userId);
        checkIfUserBlocked(userId);
        return userRepository.getFollowersById(userId);
    }

    private void checkIfUserBlocked(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        boolean userBlocked = userRepository.isUserBlocked(userId,authUserId);
        if(userBlocked){
            throw new ApiRequestException("User (id: + authUserId + ) is blocked", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<UserProjection> getFollowing(Long userId) {
        checkIfUserExists(userId);
        checkIfUserBlocked(userId);
        return userRepository.getFollowingById(userId);
    }

    @Override
    @Transactional
    public Map<String, Object> processFollow(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        List<User> followers = user.getFollowers();
        Optional<User> follower = followers.stream().filter(f -> f.getId().equals(currentUser.getId()))
                .findFirst();
        boolean isFollower;
        if (follower.isPresent()) {
            followers.remove(follower.get());
            List<User> subscribers = currentUser.getSubscribers();
            Optional<User> subscriber = subscribers.stream()
                    .filter(s -> s.getId().equals(user.getId()))
                    .findFirst();
            subscriber.ifPresent(subscribers::remove);
            isFollower = false;
        } else {
            followers.add(currentUser);
            isFollower = true;
        }
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.FOLLOW);
        notification.setUser(user);
        notification.setUserToFollow(currentUser);
        if (!currentUser.getId().equals(user.getId())) {
            Optional<Notification> userNotification = currentUser.getNotifications().stream()
                    .filter(n -> n.getNotificationType().equals(NotificationType.FOLLOW)
                            && n.getUser().getId().equals(user.getId()))
                    .findFirst();

            if (userNotification.isEmpty()) {
                Notification newNotification = notificationRepository.save(notification);
                currentUser.setNotificationsCount(currentUser.getNotificationsCount() + 1);
                List<Notification> notifications = currentUser.getNotifications();
                notifications.add(newNotification);
                return Map.of("notification", newNotification, "isFollower", isFollower);
            }
        }
        return Map.of("notification", notification, "isFollower", isFollower);

    }

    public boolean isMyProfileWaitingForApprove(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.isMyProfileWaitingForApprove(userId, authUserId);
    }

    @Override
    public List<BaseUserProjection> overallFollowers(Long userId) {
        checkIfUserExists(userId);
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.getSameFollowers(userId,authUserId,BaseUserProjection.class);
    }

    @Override
    @Transactional
    public UserProfileProjection processFollowRequestToPrivateProfile(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId).orElseThrow();
        List<User> followerRequests = currentUser.getFollowerRequests();
        Optional<User> followerRequest = currentUser.getFollowerRequests().stream()
                .filter(follower -> follower.getId().equals(user.getId())).findFirst();
        if(followerRequest.isPresent()){
            followerRequests.remove(followerRequest.get());
        }else{
            followerRequests.add(user);
        }
        return userRepository.getUserProfileById(userId).get();
    }

    @Override
    @Transactional
    public String acceptFollowRequest(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        user.getFollowerRequests().remove(currentUser);
        user.getFollowers().add(currentUser);
        return "User (id:" + userId + ") accepted.";
    }

    public boolean isUserFollowByOtherUser(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.isUserFollowByOtherUser(authUserId, userId);
    }

    @Override
    @Transactional
    public String declineFollowRequest(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        user.getFollowerRequests().remove(currentUser);
        return "User (id: + userId + ) declined.";
    }

    @Override
    public Boolean processSubscribeToNotifications(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        return processUserList(user, currentUser.getSubscribers());
    }

    private Boolean processUserList(User currentUser, List<User> userList) {
        Optional<User> userFromList = userList.stream().filter(user -> user.getId().equals(currentUser.getId()))
                .findFirst();
        if (userFromList.isPresent()) {
            userList.remove(userFromList.get());
            return false;
        } else {
            userList.add(currentUser);
            return true;
        }
    }

    @Override
    @Transactional
    public Long processPinTweet(Long tweetId) {
        User user = authenticationService.getAuthenticatedUser();
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() ->
                new ApiRequestException("Tweet not found", HttpStatus.NOT_FOUND));
        if(user.getPinnedTweet() != null || !user.getPinnedTweet().getId().equals(tweet.getId())){
            user.setPinnedTweet(tweet);
            return tweet.getId();
        }else{
            user.setPinnedTweet(null);
            return 0l;
        }
    }

    @Override
    public List<BlockedUserProjection> getBlockList() {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.getUserBlockListById(authUserId);
    }

    @Override
    @Transactional
    public Boolean processBlockList(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        List<User> userBlockedList = user.getUserBlockedList();
        Optional<User> userFromList = userBlockedList.stream().filter(blockedUser -> blockedUser.getId().equals(currentUser.getId()))
                .findFirst();
        if (userFromList.isPresent()) {
            userBlockedList.remove(userFromList.get());
            return false;
        } else {
            userBlockedList.add(currentUser);
            user.getFollowers().removeIf(follower -> follower.getId().equals(currentUser.getId()));
            user.getFollowing().removeIf(following -> following.getId().equals(currentUser.getId()));
            user.getUserLists().removeIf(list -> list.getMembers().stream()
                    .anyMatch(member -> member.getId().equals(currentUser.getId())));
            return true;
        }
    }

    @Override
    public List<MutedUserProjection> getMutedList() {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.getUserMuteListById(authUserId);
    }

    @Override
    public Boolean processMutedList(Long userId) {
        User user = authenticationService.getAuthenticatedUser();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
        return processUserList(currentUser, user.getUserMutedList());
    }

    @Override
    public UserDetailProjection getUserDetails(Long userId) {
        return userRepository.getUserDetails(userId)
                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
    }

    public boolean isMyProfileBlockedByUser(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.isUserBlocked(userId, authUserId);
    }

    public boolean isUserMutedByMyProfile(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.isUserMuted(authUserId, userId);
    }

    public boolean isUserBlockedByMyProfile(Long userId) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.isUserBlocked(authUserId, userId);
    }

    @Override
    public List<FollowerUserProjection> getFollowerRequests() {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return userRepository.getFollowerRequests(authUserId);
    }

    private <T> Page<T> getPageableTweetProjectionList(Pageable pageable, List<T> tweets, int totalPages) {
        PagedListHolder<T> page = new PagedListHolder<>(tweets);
        page.setPage(pageable.getPageNumber());
        page.setPageSize(pageable.getPageSize());
        return new PageImpl<>(page.getPageList(), pageable, totalPages);
    }

    private List<TweetUserProjection> combineTweetsArrays(List<TweetUserProjection> tweets, List<RetweetProjection> retweets) {
        List<TweetUserProjection> allTweets = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < tweets.size() && j < retweets.size()) {
            if (tweets.get(i).getDateTime().isAfter(retweets.get(j).getRetweetDate())) {
                allTweets.add(tweets.get(i));
                i++;
            } else {
                allTweets.add(retweets.get(j).getTweet());
                j++;
            }
        }
        while (i < tweets.size()) {
            allTweets.add(tweets.get(i));
            i++;
        }
        while (j < retweets.size()) {
            allTweets.add(retweets.get(j).getTweet());
            j++;
        }
        return allTweets;
    }

    private void checkIfUserExists(Long userId) {
        boolean userExist = userRepository.isUserExist(userId);
        if (!userExist) {
            throw new ApiRequestException("User (id:" + userId + ") not found", HttpStatus.NOT_FOUND);
        }
    }
}
