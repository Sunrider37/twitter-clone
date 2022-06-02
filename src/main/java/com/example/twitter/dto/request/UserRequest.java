package com.example.twitter.dto.request;

import com.example.twitter.dto.response.ImageResponse;
import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String about;
    private String location;
    private String website;
    private ImageResponse avatar;
    private ImageResponse wallpaper;
}