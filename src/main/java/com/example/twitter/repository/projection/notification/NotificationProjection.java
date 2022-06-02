package com.example.twitter.repository.projection.notification;

import com.amazonaws.services.budgets.model.NotificationType;
import com.example.twitter.repository.projection.ImageProjection;

import java.time.LocalDateTime;

public interface NotificationProjection {

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

