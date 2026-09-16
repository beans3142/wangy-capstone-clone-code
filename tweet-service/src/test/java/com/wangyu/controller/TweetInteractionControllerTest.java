package com.wangyu.controller;

import java.util.List;

import com.wangyu.HeaderResponse;
import com.wangyu.dto.BookmarkToggleResponse;
import com.wangyu.dto.LikeToggleResponse;
import com.wangyu.dto.RetweetToggleResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.mapper.TweetMapper;
import com.wangyu.repository.TweetProjection;
import com.wangyu.service.BookmarkService;
import com.wangyu.service.LikeService;
import com.wangyu.service.RetweetService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TweetInteractionControllerTest {

    private LikeService likeService;
    private RetweetService retweetService;
    private BookmarkService bookmarkService;
    private TweetInteractionController controller;

    @Before
    public void setUp() {
        likeService = mock(LikeService.class);
        retweetService = mock(RetweetService.class);
        bookmarkService = mock(BookmarkService.class);
        controller = new TweetInteractionController(likeService, retweetService, bookmarkService, new TweetMapper());
    }

    @Test
    public void toggleLikeReturns200WithToggleResult() {
        when(likeService.toggleLike(1L, 2L)).thenReturn(new LikeToggleResponse(true, 1L));

        var response = controller.toggleLike(1L, 2L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isLiked());
        assertEquals(1L, response.getBody().getLikeCount());
    }

    @Test
    public void toggleRetweetReturns200WithToggleResult() {
        when(retweetService.toggleRetweet(1L, 2L)).thenReturn(new RetweetToggleResponse(false, 0L));

        var response = controller.toggleRetweet(1L, 2L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals(0L, response.getBody().getRetweetCount());
    }

    @Test
    public void toggleBookmarkReturns200WithToggleResult() {
        when(bookmarkService.toggleBookmark(1L, 2L)).thenReturn(new BookmarkToggleResponse(true));

        var response = controller.toggleBookmark(1L, 2L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isBookmarked());
    }

    @Test
    public void getMyBookmarksPutsPagingMetaInHeadersNotBody() {
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getId()).thenReturn(1L);
        when(bookmarkService.getMyBookmarks(2L, 0, 20)).thenReturn(new TimelineResult(List.of(projection), 1L, false));

        var response = controller.getMyBookmarks(2L, 0, 20);

        assertEquals(1, response.getBody().size());
        assertEquals("1", response.getHeaders().getFirst(HeaderResponse.TOTAL_COUNT_HEADER));
        assertEquals("false", response.getHeaders().getFirst(HeaderResponse.HAS_NEXT_HEADER));
    }
}
