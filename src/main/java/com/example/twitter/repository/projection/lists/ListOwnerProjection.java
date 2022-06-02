package com.example.twitter.repository.projection.lists;

import com.example.twitter.repository.projection.ImageProjection;

public interface ListOwnerProjection {
    Long getId();
    String getFullName();
    String getUsername();
    ImageProjection getAvatar();
}