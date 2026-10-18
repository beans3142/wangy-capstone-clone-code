package com.wangyu.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.wangyu.HeaderResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.dto.TweetCreateRequest;
import com.wangyu.dto.TweetResponse;
import com.wangyu.dto.TweetUpdateRequest;
import com.wangyu.entity.Tweet;
import com.wangyu.mapper.TweetMapper;
import com.wangyu.repository.TweetProjection;
import com.wangyu.service.TweetService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetControllerTest {

    private TweetService tweetService;
    private TweetController controller;

    @Before
    public void setUp() {
        tweetService = mock(TweetService.class);
        controller = new TweetController(tweetService, new TweetMapper());
    }

    @Test
    public void createTweetReturns201WithBody() {
        Tweet saved = new Tweet(1L, "hello", null);
        when(tweetService.createTweet(1L, "hello", null)).thenReturn(saved);

        var response = controller.createTweet(new TweetCreateRequest(1L, "hello", null));

        assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
        assertEquals("hello", response.getBody().getContent());
    }

    @Test
    public void getTweetReturns200WithBody() {
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getId()).thenReturn(1L);
        when(projection.getContent()).thenReturn("hello");
        when(tweetService.getTweet(1L)).thenReturn(projection);

        var response = controller.getTweet(1L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals("hello", response.getBody().getContent());
    }

    @Test
    public void getTimelinePutsPagingMetaInHeadersNotBody() {
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getId()).thenReturn(1L);
        when(tweetService.getTimeline(1L, 0, 20)).thenReturn(new TimelineResult(List.of(projection), 42L, true));

        var response = controller.getTimeline(1L, 0, 20);

        assertEquals(1, response.getBody().size());
        assertEquals("42", response.getHeaders().getFirst(HeaderResponse.TOTAL_COUNT_HEADER));
        assertEquals("true", response.getHeaders().getFirst(HeaderResponse.HAS_NEXT_HEADER));
    }

    @Test
    public void getTimelineReturnsEmptyBodyForUserWithNoFollows() {
        when(tweetService.getTimeline(1L, 0, 20)).thenReturn(new TimelineResult(List.of(), 0L, false));

        var response = controller.getTimeline(1L, 0, 20);

        assertTrue(response.getBody().isEmpty());
        assertEquals("false", response.getHeaders().getFirst(HeaderResponse.HAS_NEXT_HEADER));
    }

    @Test
    public void updateTweetReturns200AndDelegatesToService() {
        Tweet updated = new Tweet(1L, "edited", null);
        when(tweetService.updateTweet(1L, 1L, "edited")).thenReturn(updated);

        var response = controller.updateTweet(1L, new TweetUpdateRequest(1L, "edited"));

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals("edited", response.getBody().getContent());
    }

    @Test
    public void deleteTweetReturns204AndDelegatesToService() {
        var response = controller.deleteTweet(1L, 1L);

        assertEquals(org.springframework.http.HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tweetService, times(1)).deleteTweet(1L, 1L);
    }
}
