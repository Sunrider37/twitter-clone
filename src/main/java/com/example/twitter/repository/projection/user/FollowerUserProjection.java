package com.example.twitter.repository.projection.user;

import com.example.twitter.repository.projection.ImageProjection;

public interface FollowerUserProjection {

    Long getId();
    String getFullName();
    String getUsername();
    String getAbout();
    ImageProjection getAvatar();
}
