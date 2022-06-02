package com.example.twitter.dto.response.notification;

import com.example.twitter.dto.response.ImageResponse;
import com.example.twitter.dto.response.TweetResponse;
import com.example.twitter.dto.response.UserResponse;
import com.example.twitter.model.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationInfoResponse {
    private Long id;
    private LocalDateTime date;
    private NotificationType notificationType;
    private UserResponse user;
    private TweetResponse tweet;
}