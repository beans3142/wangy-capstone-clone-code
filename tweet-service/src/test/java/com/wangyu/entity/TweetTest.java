package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TweetTest {

    @Test
    public void newTweetStoresAuthorContentAndImageUrl() {
        Tweet tweet = new Tweet(1L, "hello world", "http://example.com/a.png");

        assertEquals(Long.valueOf(1L), tweet.getAuthorId());
        assertEquals("hello world", tweet.getContent());
        assertEquals("http://example.com/a.png", tweet.getImageUrl());
        assertNotNull(tweet.getCreatedAt());
        assertEquals(tweet.getCreatedAt(), tweet.getUpdatedAt());
    }

    @Test
    public void newTweetAllowsNullImageUrl() {
        Tweet tweet = new Tweet(1L, "no image here", null);

        assertEquals("no image here", tweet.getContent());
    }

    @Test
    public void changeContentUpdatesContentAndTimestamp() throws InterruptedException {
        Tweet tweet = new Tweet(1L, "original", null);
        var createdAt = tweet.getCreatedAt();
        Thread.sleep(5);

        tweet.changeContent("edited");

        assertEquals("edited", tweet.getContent());
        assertEquals(createdAt, tweet.getCreatedAt());
        assertNotEquals(createdAt, tweet.getUpdatedAt());
    }

    @Test
    public void isAuthoredByReturnsTrueForMatchingAuthor() {
        Tweet tweet = new Tweet(1L, "hello", null);

        assertTrue(tweet.isAuthoredBy(1L));
        assertFalse(tweet.isAuthoredBy(2L));
    }
}
