package com.example.twitter.repository;

import com.example.twitter.model.BackgroundColorType;
import com.example.twitter.model.ColorSchemeType;
import com.example.twitter.model.Tweet;
import com.example.twitter.model.User;
import com.example.twitter.repository.projection.tweet.TweetAuthorProjection;
import com.example.twitter.repository.projection.user.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<UserProjection> findByActiveTrueAndIdNot(Long id);

    @Query("SELECT user.password FROM User user WHERE user.id = :userId")
    String getUserPasswordById(Long userId);

    Optional<User> findByEmail(String email);

    List<User> findByIdIn(List<Long> ids);

    @Query("SELECT user FROM User user WHERE user.id = :userId")
    Optional<UserProfileProjection> getUserProfileById(Long userId);
    @Query("SELECT new com.example.twitter.repository.projection.user.UserPrincipalProjection(user.id, user.email, user.password, user.activationCode) " +
            "from User user where user.email = :email")
    Optional<UserPrincipalProjection> findUserPrincipalByEmail(String email);
    @Query("select user from User user where user.email = :email")
    Optional<AuthUserProjection> findAuthUserByEmail(String email);

    @Query("SELECT user FROM User user WHERE user.email = :email")
    Optional<UserCommonProjection> findCommonUserByEmail(String email);

    @Query("select user from User user WHERE user.activationCode = :code")
    Optional<UserCommonProjection> findCommonUserByActivationCode(String code);

    Optional<AuthUserProjection> findByPasswordResetCode(String code);

    List<UserProjection> findTop5ByActiveTrue();
    @Query("SELECT u.id AS id, u.fullName AS fullName, u.username AS username, u.about AS about, u.avatar AS avatar, " +
            "u.privateProfile AS privateProfile, u.mutedDirectMessages AS mutedDirectMessages " +
            "FROM User u " +
            "WHERE UPPER(u.fullName) LIKE UPPER(CONCAT('%',:name,'%')) AND u.active = true " +
            "OR UPPER(u.username) LIKE UPPER(CONCAT('%',:name,'%')) AND u.active = true")
    List<UserProjection> findByFullNameOrUsername(String name);

    @Modifying
    @Query("UPDATE User user SET user.profileStarted = true WHERE user.id = :userId")
    void updateProfileStarted(Long userId);

    @Query("SELECT CASE WHEN count(user) > 0 THEN true ELSE false END FROM User user WHERE user.id = :userId")
    boolean isUserExist(Long userId);

    @Query("select notification.tweet.user as tweetAuthor from User user " +
            "left join user.notifications notification left join notification.tweet.user.subscribers subscriber " +
            "where user.id = :userId and notification.notificationType = 'TWEET' and subscriber.id = :userId")
    List<TweetAuthorProjection> getNotificationsTweetAuthors(Long userId);
    @Modifying
    @Query("update User user set user.active = true where user.id = :userId")
    void updateActiveUserProfile(Long userId);

    @Query("SELECT CASE WHEN count(follower) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "LEFT JOIN user.followers follower " +
            "WHERE user.id = :authUserId " +
            "AND follower.id = :userId")
    boolean isUserFollowByOtherUser(Long authUserId, Long userId);
    @Query("SELECT CASE WHEN count(blockedUser) > 0 THEN true ELSE false END FROM User user " +
            "LEFT JOIN user.userBlockedList blockedUser " +
            "WHERE user.id = :userId " +
            "AND blockedUser.id = :blockedUserId")
    boolean isUserBlocked(Long userId, Long blockedUserId);

    @Query("SELECT CASE WHEN count(userMuted) > 0 THEN true ELSE false END FROM User user " +
            "LEFT JOIN user.userMutedList userMuted " +
            "WHERE user.id = :userId " +
            "AND userMuted.id = :mutedUserId")
    boolean isUserMuted(Long userId, Long mutedUserId);
    @Query("select f.id as id, f.fullName as fullName, f.username as username, f.about as about, " +
            "f.avatar as avatar from User user left join user.followerRequests f where user.id = :userId")
    List<FollowerUserProjection> getFollowerRequests(Long userId);
    @Query("select case when count(followerRequest) > 0 then true else false end from User user " +
            "left join user.followerRequests followerRequest where user.id = :userId " +
            "and followerRequest.id = :authUserId")
    boolean isMyProfileWaitingForApprove(Long userId, Long authUserId);

    @Query("select user from User user where user.id = :userId")
    AuthUserProjection findAuthUserById(Long userId);
@Query("select f.id as id, f.fullName as fullName, f.username as username, f.about as about, " +
        "f.avatar as avatar, f.privateProfile as privateProfile, f.mutedDirectMessages as mutedDirectMessages " +
        "from User user left join user.followers f where user.id = :userId")
    List<UserProjection> getFollowersById(Long userId);

    @Query("SELECT f.id AS id, f.fullName AS fullName, f.username AS username, f.about AS about, f.avatar AS avatar, " +
            "f.privateProfile AS privateProfile, f.mutedDirectMessages AS mutedDirectMessages " +
            "FROM User user " +
            "LEFT JOIN user.following f " +
            "WHERE user.id = :userId")
    List<UserProjection> getFollowingById(Long userId);

    @Query(value = "SELECT users.id as id, users.full_name as fullName, users.username as username, users.about as about, " +
            "users.private_profile as isPrivateProfile, images.id as img_id, images.src as img_src " +
            "FROM users " +
            "LEFT JOIN user_avatar ON users.id = user_avatar.user_id " +
            "LEFT JOIN images ON user_avatar.avatar_id = images.id " +
            "WHERE users.id IN ( " +
            "SELECT user_subscriptions.subscriber_id FROM users " +
            "JOIN user_subscriptions ON users.id = user_subscriptions.user_id " +
            "WHERE users.id = ?1) " +
            "INTERSECT " +
            "SELECT users.id as id, users.full_name as fullName, users.username as username, users.about as about, " +
            "users.private_profile as isPrivateProfile, images.id as img_id, images.src as img_src " +
            "FROM users " +
            "LEFT JOIN user_avatar ON users.id = user_avatar.user_id " +
            "LEFT JOIN images ON user_avatar.avatar_id = images.id " +
            "WHERE users.id IN ( " +
            "SELECT user_subscriptions.subscriber_id FROM users " +
            "JOIN user_subscriptions ON users.id = user_subscriptions.user_id " +
            "WHERE users.id = ?2)", nativeQuery = true)
    List<BaseUserProjection> getSameFollowers(Long userId, Long authUserId, Class<BaseUserProjection> type);
    @Query("SELECT b.id AS id, b.fullName AS fullName, b.username AS username, b.about AS about, b.avatar AS avatar, " +
            "b.privateProfile AS isPrivateProfile " +
            "FROM User user " +
            "LEFT JOIN user.userBlockedList b " +
            "WHERE user.id = :userId")
    List<BlockedUserProjection> getUserBlockListById(Long userId);
    @Query("SELECT m.id as id, m.fullName as fullName, m.username as username, " +
            "m.about as about, m.avatar as avatar, m.privateProfile as isPrivateProfile " +
            "FROM User user " +
            "LEFT JOIN user.userMutedList m " +
            "WHERE user.id = :userId")
    List<MutedUserProjection> getUserMuteListById(Long userId);
    @Query("SELECT user FROM User user WHERE user.id = :userId")
    Optional<UserDetailProjection> getUserDetails(Long userId);

    @Modifying
    @Query("UPDATE User user SET user.email = :email WHERE user.id = :userId")
    void updateEmail(String email, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.password = :password WHERE user.id = :userId")
    void updatePassword(String password, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.passwordResetCode = :passwordResetCode WHERE user.id = :userId")
    void updatePasswordResetCode(String passwordResetCode, Long userId);


    @Modifying
    @Query("UPDATE User user SET user.activationCode = :activationCode WHERE user.id = :userId")
    void updateActivationCode(String activationCode, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.username = :username WHERE user.id = :userId")
    void updateUsername(String username, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.countryCode = :countryCode, user.phone = :phone WHERE user.id = :userId")
    void updatePhone(String countryCode, Long phone, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.country = :country WHERE user.id = :userId")
    void updateCountry(String country, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.gender = :gender WHERE user.id = :userId")
    void updateGender(String gender, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.language = :language WHERE user.id = :userId")
    void updateLanguage(String language, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.mutedDirectMessages = :mutedDirectMessages WHERE user.id = :userId")
    void updateDirectMessageRequests(boolean mutedDirectMessages, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.privateProfile = :privateProfile WHERE user.id = :userId")
    void updatePrivateProfile(boolean privateProfile, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.colorScheme = :colorSchemeType WHERE user.id = :userId")
    void updateColorScheme(ColorSchemeType colorSchemeType, Long userId);

    @Modifying
    @Query("UPDATE User user SET user.backgroundColor = :backgroundColorType WHERE user.id = :userId")
    void updateBackgroundColor(BackgroundColorType backgroundColorType, Long userId);

    List<User> findByUnreadMessages_Tweet(Tweet tweet);
    @Query("select follower.id from User user left join user.followers follower where user.id = :userId")
    List<Long> getUserFollowersIds(Long userId);

    @Query("SELECT user FROM User user " +
            "LEFT JOIN user.followers follower " +
            "WHERE user.id = :userId " +
            "AND (user.privateProfile = false OR follower.id = :authUserId)")
    Optional<User> getValidUser(Long userId, Long authUserId);
}
