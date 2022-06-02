package com.example.twitter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "tweets")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", initialValue = 100,allocationSize = 1)
    private Long id;

    private String email;
    private String fullName;
    private String password;
    private String username;
    private String location;
    private String about;
    private String website;
    private String countryCode;
    private Long phone;
    @Column(name = "gender")
    private String gender;
    private String country;
    private String language;
    private String birthday;
    private LocalDateTime registrationDate;
    private String activationCode;
    private String passwordResetCode;
    private String role;
    private Long tweetCount;
    private Long mediaTweetCount;
    private Long likeCount;
    private Long notificationsCount;
    @Column(columnDefinition = "boolean default false")
    private boolean active;

    @Column(name = "profile_customized", columnDefinition = "boolean default false")
    private boolean profileCustomized;

    @Column(name = "profile_started", columnDefinition = "boolean default false")
    private boolean profileStarted;

    @Column(name = "muted_direct_messages", columnDefinition = "boolean default false")
    private boolean mutedDirectMessages;

    @Column(name = "private_profile", columnDefinition = "boolean default false")
    private boolean privateProfile;

    @Enumerated(EnumType.STRING)
    private BackgroundColorType backgroundColor;
    @Enumerated(EnumType.STRING)
    private ColorSchemeType colorScheme;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_pinned_tweet", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private Tweet pinnedTweet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_avatar",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "avatar_id"))
    private Image avatar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_wallpaper", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "wallpaper_id"))
    private Image wallpaper;

    @ManyToMany
    private List<Tweet> tweets;

    @OneToMany(mappedBy = "user")
    private List<LikeTweet> likedTweets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Retweet> retweets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany
    private List<Notification> notifications = new ArrayList<>();

    @ManyToMany
    private List<Lists> userLists = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chats = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_muted",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "muted_user_id"))
    private List<User> userMutedList;

    @ManyToMany
    @JoinTable(name = "user_blocked", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "blocked_user_id"))
    private List<User> userBlockedList;

    @ManyToMany
    @JoinTable(name = "unread_messages",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_message_id"))
    private List<ChatMessage> unreadMessages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions",
    joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<User> followers;

    @ManyToMany
    @JoinTable(name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> following;

    @ManyToMany
    @JoinTable(name = "user_follower_requests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followerRequests;

    @ManyToMany
    @JoinTable(name = "subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<User> subscribers;

    public User() {
        this.registrationDate = LocalDateTime.now().withNano(0);
        this.bookmarks = new ArrayList<>();
        this.userLists = new ArrayList<>();
        this.unreadMessages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.backgroundColor = BackgroundColorType.DEFAULT;
        this.colorScheme = ColorSchemeType.BLUE;
    }
}
