package com.example.twitter.dto.response.lists;

import com.example.twitter.dto.response.ImageResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMemberResponse {
    private Long id;
    private String fullName;
    private String username;
    private String about;
    private ImageResponse avatar;

    @JsonProperty("isPrivateProfile")
    private boolean isPrivateProfile;
}