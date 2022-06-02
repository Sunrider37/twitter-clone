package com.example.twitter.repository.projection.lists;

import com.example.twitter.repository.projection.ImageProjection;

public interface ListMemberProjection {
    Long getId();
    String getFullName();
    String getUsername();
    String getAbout();
    ImageProjection getAvatar();
    boolean getIsPrivateProfile();
}
