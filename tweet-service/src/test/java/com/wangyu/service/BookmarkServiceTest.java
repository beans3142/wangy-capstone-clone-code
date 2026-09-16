package com.wangyu.service;

import java.util.List;
import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.BookmarkToggleResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.entity.Bookmark;
import com.wangyu.repository.TweetProjection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookmarkServiceTest extends AbstractServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Test
    public void toggleBookmarkCreatesBookmarkWhenNotYetBookmarked() {
        when(tweetRepository.existsById(1L)).thenReturn(true);
        when(bookmarkRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        BookmarkToggleResponse response = bookmarkService.toggleBookmark(1L, 2L);

        assertTrue(response.isBookmarked());
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    public void toggleBookmarkRemovesBookmarkWhenAlreadyBookmarked() {
        Bookmark existing = new Bookmark(1L, 2L);
        when(tweetRepository.existsById(1L)).thenReturn(true);
        when(bookmarkRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.of(existing));

        BookmarkToggleResponse response = bookmarkService.toggleBookmark(1L, 2L);

        assertFalse(response.isBookmarked());
        verify(bookmarkRepository, times(1)).delete(existing);
    }

    @Test
    public void toggleBookmarkThrows404WhenTweetMissing() {
        when(tweetRepository.existsById(99L)).thenReturn(false);

        try {
            bookmarkService.toggleBookmark(99L, 2L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
        verify(bookmarkRepository, never()).save(any());
    }

    @Test
    public void getMyBookmarksReturnsOnlyExistingTweetsInBookmarkOrder() {
        Bookmark first = new Bookmark(1L, 5L);
        Bookmark second = new Bookmark(2L, 5L);
        when(bookmarkRepository.findByUserIdOrderByCreatedAtDesc(org.mockito.ArgumentMatchers.eq(5L), any(Pageable.class)))
                .thenReturn(List.of(first, second));
        when(bookmarkRepository.countByUserId(5L)).thenReturn(2L);

        TweetProjection tweetOne = mock(TweetProjection.class);
        when(tweetOne.getId()).thenReturn(1L);
        when(tweetRepository.findByIdIn(any(), org.mockito.ArgumentMatchers.eq(TweetProjection.class)))
                .thenReturn(List.of(tweetOne));

        TimelineResult result = bookmarkService.getMyBookmarks(5L, 0, 20);

        assertEquals(1, result.getContent().size());
        assertEquals(2L, result.getTotalCount());
        assertFalse(result.isHasNext());
    }

    @Test
    public void getMyBookmarksRejectsNegativePage() {
        try {
            bookmarkService.getMyBookmarks(5L, -1, 20);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }

    @Test
    public void getMyBookmarksRejectsNonPositiveSize() {
        try {
            bookmarkService.getMyBookmarks(5L, 0, 0);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }
}
