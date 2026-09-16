package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BookmarkTest {

    @Test
    public void newBookmarkStoresTweetIdAndUserId() {
        Bookmark bookmark = new Bookmark(1L, 2L);

        assertEquals(Long.valueOf(1L), bookmark.getTweetId());
        assertEquals(Long.valueOf(2L), bookmark.getUserId());
        assertNotNull(bookmark.getCreatedAt());
    }
}
