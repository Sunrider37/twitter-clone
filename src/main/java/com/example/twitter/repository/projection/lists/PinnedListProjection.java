package com.example.twitter.repository.projection.lists;

import com.example.twitter.repository.projection.ImageProjection;

import java.time.LocalDateTime;

public interface PinnedListProjection {
    Long getId();
    String getName();
    LocalDateTime getPinnedDate();
    String getAltWallpaper();
    ImageProjection getWallpaper();
    boolean getIsPrivate();
}