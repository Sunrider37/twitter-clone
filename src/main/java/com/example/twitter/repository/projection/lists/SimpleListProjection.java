package com.example.twitter.repository.projection.lists;

import com.example.twitter.repository.projection.ImageProjection;

public interface SimpleListProjection {
    Long getId();
    String getName();
    String getAltWallpaper();
    ImageProjection getWallpaper();
    boolean getIsPrivate();

}