package com.wangyu.repository;

import java.time.LocalDateTime;

public interface TweetProjection {

    Long getId();

    Long getAuthorId();

    String getContent();

    String getImageUrl();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    long getLikeCount();

    long getRetweetCount();
}
