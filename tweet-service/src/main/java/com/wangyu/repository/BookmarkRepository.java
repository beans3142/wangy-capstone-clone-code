package com.wangyu.repository;

import java.util.List;
import java.util.Optional;

import com.wangyu.entity.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByTweetIdAndUserId(Long tweetId, Long userId);

    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByUserId(Long userId);
}
