package com.example.twitter.dto.response.user;

import com.example.twitter.dto.response.ImageResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockedUserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String about;
    private ImageResponse avatar;

    @JsonProperty("isPrivateProfile")
    private boolean isPrivateProfile;

    @JsonProperty("isUserBlocked")
    private boolean isUserBlocked;
}