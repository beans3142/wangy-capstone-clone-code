package com.wangyu.service;

import java.util.List;
import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.entity.Follow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FollowServiceTest extends AbstractServiceTest {

    @Autowired
    private FollowService followService;

    @Test
    public void followCreatesRelationWhenNotAlreadyFollowing() {
        when(followRepository.existsByFollowerIdAndFollowingId(1L, 2L)).thenReturn(false);
        when(followRepository.save(any(Follow.class))).thenAnswer(invocation -> invocation.getArgument(0));

        followService.follow(1L, 2L);

        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    public void followIsIdempotentWhenAlreadyFollowing() {
        when(followRepository.existsByFollowerIdAndFollowingId(1L, 2L)).thenReturn(true);

        followService.follow(1L, 2L);

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    public void followRejectsSelfFollow() {
        try {
            followService.follow(1L, 1L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    public void unfollowDeletesExistingRelation() {
        Follow follow = new Follow(1L, 2L);
        when(followRepository.findByFollowerIdAndFollowingId(1L, 2L)).thenReturn(Optional.of(follow));

        followService.unfollow(1L, 2L);

        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    public void unfollowIsNoOpWhenRelationDoesNotExist() {
        when(followRepository.findByFollowerIdAndFollowingId(1L, 2L)).thenReturn(Optional.empty());

        followService.unfollow(1L, 2L);

        verify(followRepository, never()).delete(any(Follow.class));
    }

    @Test
    public void getFollowingIdsReturnsIdsFromRepository() {
        when(followRepository.findFollowingIdsByFollowerId(1L)).thenReturn(List.of(2L, 3L));

        List<Long> result = followService.getFollowingIds(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(2L));
        assertTrue(result.contains(3L));
    }

    @Test
    public void getFollowingIdsReturnsEmptyListWhenNoneFollowed() {
        when(followRepository.findFollowingIdsByFollowerId(99L)).thenReturn(List.of());

        List<Long> result = followService.getFollowingIds(99L);

        assertTrue(result.isEmpty());
    }
}
