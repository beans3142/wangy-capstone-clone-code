package com.wangyu.controller;

import java.util.List;

import com.wangyu.dto.FollowRequest;
import com.wangyu.service.FollowService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FollowControllerTest {

    private FollowService followService;
    private FollowController controller;

    @Before
    public void setUp() {
        followService = mock(FollowService.class);
        controller = new FollowController(followService);
    }

    @Test
    public void followReturns201AndDelegatesToService() {
        ResponseEntity<Void> result = controller.follow(2L, new FollowRequest(1L));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(followService, times(1)).follow(1L, 2L);
    }

    @Test
    public void unfollowReturns204AndDelegatesToService() {
        ResponseEntity<Void> result = controller.unfollow(2L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(followService, times(1)).unfollow(1L, 2L);
    }

    @Test
    public void getFollowingIdsReturns200WithIdList() {
        when(followService.getFollowingIds(1L)).thenReturn(List.of(2L, 3L));

        ResponseEntity<List<Long>> result = controller.getFollowingIds(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    public void getFollowingIdsReturnsEmptyListWhenNoneFollowed() {
        when(followService.getFollowingIds(99L)).thenReturn(List.of());

        ResponseEntity<List<Long>> result = controller.getFollowingIds(99L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }
}
