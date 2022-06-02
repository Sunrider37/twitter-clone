package com.example.twitter.dto.response.notification;

import com.example.twitter.dto.response.TweetResponse;
import com.example.twitter.model.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationReplyResponse {
    private Long tweetId;
    private NotificationType notificationType;
    private TweetResponse tweet;
}