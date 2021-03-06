package com.example.twitter.dto.response.user;

import com.example.twitter.dto.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerUserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String about;
    private ImageResponse avatar;
}