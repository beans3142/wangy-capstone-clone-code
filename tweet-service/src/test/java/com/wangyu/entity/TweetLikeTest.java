package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TweetLikeTest {

    @Test
    public void newTweetLikeStoresTweetIdAndUserId() {
        TweetLike like = new TweetLike(1L, 2L);

        assertEquals(Long.valueOf(1L), like.getTweetId());
        assertEquals(Long.valueOf(2L), like.getUserId());
        assertNotNull(like.getCreatedAt());
    }
}
