package com.example.twitter.repository;

import com.example.twitter.model.LikeTweet;
import com.example.twitter.repository.projection.LikeTweetProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeTweetRepository extends JpaRepository<LikeTweet,Long> {
    Page<LikeTweetProjection> findByUserId(Long userId, Pageable pageable);
}
