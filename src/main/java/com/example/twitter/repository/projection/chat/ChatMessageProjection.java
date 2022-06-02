package com.example.twitter.repository.projection.chat;

import com.example.twitter.repository.projection.ImageProjection;

import java.time.LocalDateTime;

public interface ChatMessageProjection {
    Long getId();
    String getText();
    LocalDateTime getDate();
    ChatAuthorProjection getAuthor();
    ChatTweetProjection getTweet();
    ChatProjection getChat();

    interface ChatAuthorProjection {
        Long getId();
    }

    interface ChatTweetProjection {
        Long getId();
        String getText();
        LocalDateTime getDateTime();
        TweetUserProjection getUser();

        interface TweetUserProjection {
            Long getId();
            String getFullName();
            String getUsername();
            ImageProjection getAvatar();
        }
    }

    interface ChatProjection {
        Long getId();
    }
}