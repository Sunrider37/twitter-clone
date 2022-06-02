package com.example.twitter.repository;

import com.example.twitter.model.Bookmark;
import com.example.twitter.repository.projection.BookmarkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Page<BookmarkProjection> findByUser(Long userId, Pageable pageable);
}
