package com.example.twitter.repository.projection.tweet;

import com.example.twitter.repository.projection.ImageProjection;

public interface TweetAuthorProjection {

    AuthorProjection getTweetAuthor();

    interface AuthorProjection {
        Long getId();
        String getUsername();
        String getFullName();
        ImageProjection getAvatar();
    }
}
