package com.example.twitter.repository;

import com.example.twitter.model.Tag;
import com.example.twitter.repository.projection.TagProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTagName(String hashTag);

    List<Tag> findByTweets_Id(Long id);

    List<TagProjection> findTop5OrderByTweetsQuantityDesc();

    List<TagProjection> findByOrderByTweetsQuantityDesc();
}
