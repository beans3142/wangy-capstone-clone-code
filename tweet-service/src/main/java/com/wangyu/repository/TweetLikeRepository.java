package com.wangyu.repository;

import java.util.Optional;

import com.wangyu.entity.TweetLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetLikeRepository extends JpaRepository<TweetLike, Long> {

    Optional<TweetLike> findByTweetIdAndUserId(Long tweetId, Long userId);
}
