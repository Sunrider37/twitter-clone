package com.example.twitter.repository.projection.lists;

import com.example.twitter.repository.projection.ImageProjection;

import java.time.LocalDateTime;

public interface ListUserProjection {
    Long getId();
    String getName();
    String getDescription();
    LocalDateTime getPinnedDate();
    String getAltWallpaper();
    ImageProjection getWallpaper();
    ListOwnerProjection getListOwner();
    boolean getIsPrivate();
}