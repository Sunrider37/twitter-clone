package com.example.twitter.dto.request;

import com.example.twitter.model.Image;
import com.example.twitter.model.LinkCoverSize;
import com.example.twitter.model.ReplyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TweetRequest {
    private Long id;
    private String text;
    private String addressedUsername;
    private Long addressedId;
    private ReplyType replyType;
    private LinkCoverSize linkCoverSize;
    private List<Image> images;
    private Long pollDateTime;
    private List<String> choices;
    private LocalDateTime scheduledDate;
}