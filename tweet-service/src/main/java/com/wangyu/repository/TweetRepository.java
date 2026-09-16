package com.wangyu.repository;

import java.util.List;
import java.util.Optional;

import com.wangyu.entity.Tweet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    <T> Optional<T> getTweetById(Long id, Class<T> type);

    <T> List<T> findByAuthorIdInOrderByCreatedAtDesc(List<Long> authorIds, Pageable pageable, Class<T> type);

    long countByAuthorIdIn(List<Long> authorIds);
}
