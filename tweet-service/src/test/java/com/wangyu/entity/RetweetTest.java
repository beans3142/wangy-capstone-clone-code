package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RetweetTest {

    @Test
    public void newRetweetStoresTweetIdAndUserId() {
        Retweet retweet = new Retweet(1L, 2L);

        assertEquals(Long.valueOf(1L), retweet.getTweetId());
        assertEquals(Long.valueOf(2L), retweet.getUserId());
        assertNotNull(retweet.getCreatedAt());
    }
}
