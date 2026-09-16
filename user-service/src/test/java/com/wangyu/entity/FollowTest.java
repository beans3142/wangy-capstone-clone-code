package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FollowTest {

    @Test
    public void newFollowStoresFollowerAndFollowingIds() {
        Follow follow = new Follow(1L, 2L);

        assertEquals(Long.valueOf(1L), follow.getFollowerId());
        assertEquals(Long.valueOf(2L), follow.getFollowingId());
        assertNotNull(follow.getCreatedAt());
    }
}
