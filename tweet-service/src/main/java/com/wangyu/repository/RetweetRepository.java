package com.wangyu.repository;

import java.util.Optional;

import com.wangyu.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    Optional<Retweet> findByTweetIdAndUserId(Long tweetId, Long userId);
}
