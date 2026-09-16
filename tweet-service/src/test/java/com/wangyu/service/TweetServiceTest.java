package com.wangyu.service;

import java.util.List;
import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.TimelineResult;
import com.wangyu.entity.Tweet;
import com.wangyu.kafka.HashtagParsedEvent;
import com.wangyu.kafka.MentionParsedEvent;
import com.wangyu.repository.TweetProjection;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest extends AbstractServiceTest {

    @Autowired
    private TweetService tweetService;

    @Test
    public void createTweetSavesTweetAndPublishesHashtagAndMentionEvents() {
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tweetService.createTweet(1L, "#캡스톤 화이팅 @친구", null);

        ArgumentCaptor<HashtagParsedEvent> hashtagCaptor = ArgumentCaptor.forClass(HashtagParsedEvent.class);
        verify(hashtagParsedEventProducer, times(1)).publish(hashtagCaptor.capture());
        assertEquals("캡스톤", hashtagCaptor.getValue().getTag());

        ArgumentCaptor<MentionParsedEvent> mentionCaptor = ArgumentCaptor.forClass(MentionParsedEvent.class);
        verify(mentionParsedEventProducer, times(1)).publish(mentionCaptor.capture());
        assertEquals("친구", mentionCaptor.getValue().getMentionedNickname());
    }

    @Test
    public void createTweetPublishesNothingWhenNoHashtagOrMention() {
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tweetService.createTweet(1L, "그냥 평범한 트윗", null);

        verify(hashtagParsedEventProducer, never()).publish(any());
        verify(mentionParsedEventProducer, never()).publish(any());
    }

    @Test
    public void getTweetReturnsProjectionWhenFound() {
        TweetProjection projection = org.mockito.Mockito.mock(TweetProjection.class);
        when(tweetRepository.getTweetById(1L, TweetProjection.class)).thenReturn(Optional.of(projection));

        assertEquals(projection, tweetService.getTweet(1L));
    }

    @Test
    public void getTweetThrows404WhenNotFound() {
        when(tweetRepository.getTweetById(99L, TweetProjection.class)).thenReturn(Optional.empty());

        try {
            tweetService.getTweet(99L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Test
    public void getTimelineIncludesSelfAndFollowingAuthorsWithHasNextTrue() {
        when(userServiceClient.getFollowingIds(1L)).thenReturn(List.of(2L, 3L));
        when(tweetRepository.findByAuthorIdInOrderByCreatedAtDesc(any(), any(Pageable.class), eq(TweetProjection.class)))
                .thenReturn(List.of(org.mockito.Mockito.mock(TweetProjection.class)));
        when(tweetRepository.countByAuthorIdIn(any())).thenReturn(5L);

        TimelineResult result = tweetService.getTimeline(1L, 0, 1);

        assertEquals(1, result.getContent().size());
        assertEquals(5L, result.getTotalCount());
        assertTrue(result.isHasNext());
    }

    @Test
    public void getTimelineReturnsEmptyAndNoNextForNewUserWithNoFollows() {
        when(userServiceClient.getFollowingIds(1L)).thenReturn(List.of());
        when(tweetRepository.findByAuthorIdInOrderByCreatedAtDesc(any(), any(Pageable.class), eq(TweetProjection.class)))
                .thenReturn(List.of());
        when(tweetRepository.countByAuthorIdIn(any())).thenReturn(0L);

        TimelineResult result = tweetService.getTimeline(1L, 0, 20);

        assertTrue(result.getContent().isEmpty());
        assertFalse(result.isHasNext());
    }

    @Test
    public void getTimelineRejectsNegativePage() {
        try {
            tweetService.getTimeline(1L, -1, 20);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }

    @Test
    public void getTimelineRejectsNonPositiveSize() {
        try {
            tweetService.getTimeline(1L, 0, 0);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }

    @Test
    public void updateTweetChangesContentWhenAuthorMatches() {
        Tweet tweet = new Tweet(1L, "original", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        Tweet updated = tweetService.updateTweet(1L, 1L, "edited");

        assertEquals("edited", updated.getContent());
    }

    @Test
    public void updateTweetRejectsWhenAuthorDoesNotMatch() {
        Tweet tweet = new Tweet(1L, "original", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        try {
            tweetService.updateTweet(1L, 99L, "edited");
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        }
    }

    @Test
    public void updateTweetThrows404WhenTweetMissing() {
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            tweetService.updateTweet(1L, 1L, "edited");
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Test
    public void deleteTweetRemovesTweetWhenAuthorMatches() {
        Tweet tweet = new Tweet(1L, "original", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        tweetService.deleteTweet(1L, 1L);

        verify(tweetRepository, times(1)).delete(tweet);
    }

    @Test
    public void deleteTweetRejectsWhenAuthorDoesNotMatch() {
        Tweet tweet = new Tweet(1L, "original", null);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        try {
            tweetService.deleteTweet(1L, 99L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        }
        verify(tweetRepository, never()).delete(any(Tweet.class));
    }
}
