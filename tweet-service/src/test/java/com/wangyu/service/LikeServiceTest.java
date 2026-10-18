package com.wangyu.service;

import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.LikeToggleResponse;
import com.wangyu.entity.Tweet;
import com.wangyu.entity.TweetLike;
import com.wangyu.kafka.TweetLikedEvent;
import com.wangyu.repository.TweetProjection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
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

public class LikeServiceTest extends AbstractServiceTest {

    @Autowired
    private LikeService likeService;

    @Test
    public void toggleLikeCreatesLikeAndIncrementsCountWhenNotYetLiked() {
        Tweet tweet = new Tweet(10L, "hello", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(tweetLikeRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.empty());
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getLikeCount()).thenReturn(1L);
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.of(projection));

        LikeToggleResponse response = likeService.toggleLike(1L, 2L);

        assertTrue(response.isLiked());
        assertEquals(1L, response.getLikeCount());
        verify(tweetLikeRepository, times(1)).save(any(TweetLike.class));
        verify(tweetRepository, times(1)).incrementLikeCount(1L);
        verify(tweetRepository, never()).decrementLikeCount(any());
    }

    @Test
    public void toggleLikeRemovesLikeAndDecrementsCountWhenAlreadyLiked() {
        Tweet tweet = new Tweet(10L, "hello", null);
        TweetLike existing = new TweetLike(1L, 2L);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(tweetLikeRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.of(existing));
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getLikeCount()).thenReturn(0L);
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.of(projection));

        LikeToggleResponse response = likeService.toggleLike(1L, 2L);

        assertFalse(response.isLiked());
        assertEquals(0L, response.getLikeCount());
        verify(tweetLikeRepository, times(1)).delete(existing);
        verify(tweetRepository, times(1)).decrementLikeCount(1L);
        verify(tweetRepository, never()).incrementLikeCount(any());
    }

    @Test
    public void toggleLikePublishesEventEvenWhenUnliking() {
        Tweet tweet = new Tweet(10L, "hello", null);
        TweetLike existing = new TweetLike(1L, 2L);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(tweetLikeRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.of(existing));
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.empty());

        likeService.toggleLike(1L, 2L);

        ArgumentCaptor<TweetLikedEvent> captor = ArgumentCaptor.forClass(TweetLikedEvent.class);
        verify(tweetLikedEventProducer, times(1)).publish(captor.capture());
        assertFalse(captor.getValue().isLiked());
        assertEquals(Long.valueOf(10L), captor.getValue().getAuthorId());
    }

    @Test
    public void toggleLikeThrows404WhenTweetMissing() {
        when(tweetRepository.findById(99L)).thenReturn(Optional.empty());

        try {
            likeService.toggleLike(99L, 2L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
        verify(tweetLikeRepository, never()).save(any());
    }
}
