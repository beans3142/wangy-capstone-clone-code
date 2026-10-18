package com.wangyu.service;

import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.RetweetToggleResponse;
import com.wangyu.entity.Retweet;
import com.wangyu.entity.Tweet;
import com.wangyu.kafka.TweetRetweetedEvent;
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

public class RetweetServiceTest extends AbstractServiceTest {

    @Autowired
    private RetweetService retweetService;

    @Test
    public void toggleRetweetCreatesRetweetAndIncrementsCountWhenNotYetRetweeted() {
        Tweet tweet = new Tweet(10L, "hello", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.empty());
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getRetweetCount()).thenReturn(1L);
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.of(projection));

        RetweetToggleResponse response = retweetService.toggleRetweet(1L, 2L);

        assertTrue(response.isRetweeted());
        assertEquals(1L, response.getRetweetCount());
        verify(retweetRepository, times(1)).save(any(Retweet.class));
        verify(tweetRepository, times(1)).incrementRetweetCount(1L);
    }

    @Test
    public void toggleRetweetRemovesRetweetAndDecrementsCountWhenAlreadyRetweeted() {
        Tweet tweet = new Tweet(10L, "hello", null);
        Retweet existing = new Retweet(1L, 2L);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.of(existing));
        TweetProjection projection = mock(TweetProjection.class);
        when(projection.getRetweetCount()).thenReturn(0L);
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.of(projection));

        RetweetToggleResponse response = retweetService.toggleRetweet(1L, 2L);

        assertFalse(response.isRetweeted());
        verify(retweetRepository, times(1)).delete(existing);
        verify(tweetRepository, times(1)).decrementRetweetCount(1L);
    }

    @Test
    public void toggleRetweetPublishesEventWithAuthorId() {
        Tweet tweet = new Tweet(10L, "hello", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.findByTweetIdAndUserId(1L, 2L)).thenReturn(Optional.empty());
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.empty());

        retweetService.toggleRetweet(1L, 2L);

        ArgumentCaptor<TweetRetweetedEvent> captor = ArgumentCaptor.forClass(TweetRetweetedEvent.class);
        verify(tweetRetweetedEventProducer, times(1)).publish(captor.capture());
        assertTrue(captor.getValue().isRetweeted());
        assertEquals(Long.valueOf(10L), captor.getValue().getAuthorId());
    }

    @Test
    public void toggleRetweetThrows404WhenTweetMissing() {
        when(tweetRepository.findById(99L)).thenReturn(Optional.empty());

        try {
            retweetService.toggleRetweet(99L, 2L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
        verify(retweetRepository, never()).save(any());
    }
}
