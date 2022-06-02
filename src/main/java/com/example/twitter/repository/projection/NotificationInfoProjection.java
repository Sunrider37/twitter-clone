package com.example.twitter.repository.projection;

import com.example.twitter.model.NotificationType;

import java.time.LocalDateTime;

public interface NotificationInfoProjection {
    Notification getNotification();

    interface Notification {
        Long getId();
        LocalDateTime getDate();
        NotificationType getNotificationType();
        NotificationUserProjection getUser();
        NotificationUserProjection getUserToFollow();
        NotificationTweetProjection getTweet();
    }

    interface NotificationUserProjection {
        Long getId();
        String getUsername();
        ImageProjection getAvatar();
    }

    interface NotificationTweetProjection {
        Long getId();
        String getText();
        NotificationUserProjection getUser();
    }
}
