package com.example.twitter.repository.projection.user;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public interface SameFollower {
    Long getId();
    String getFullName();
    String getUsername();

    @Value("#{T(com.example.twitter.repository.projection.user.SameFollower).convertToAvatar(target.img_id, target.img_src)}")
    Map<String, Object> getAvatar();

    static Map<String, Object> convertToAvatar(Long id, String src) {
        return Map.of("id", id,"src", src);
    }
}