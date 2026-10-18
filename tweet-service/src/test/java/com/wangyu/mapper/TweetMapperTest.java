package com.wangyu.mapper;

import java.util.List;

import com.wangyu.dto.TweetResponse;
import com.wangyu.entity.Tweet;
import com.wangyu.repository.TweetProjection;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class TweetMapperTest {

    private final TweetMapper mapper = new TweetMapper();

    @Test
    public void toResponseMapsEntityFields() {
        Tweet tweet = new Tweet(1L, "hello world", "http://example.com/a.png");

        TweetResponse response = mapper.toResponse(tweet);

        assertEquals(tweet.getAuthorId(), response.getAuthorId());
        assertEquals(tweet.getContent(), response.getContent());
        assertEquals(tweet.getImageUrl(), response.getImageUrl());
    }

    @Test
    public void toResponseMapsProjectionFields() {
        TweetProjection projection = Mockito.mock(TweetProjection.class);
        Mockito.when(projection.getId()).thenReturn(1L);
        Mockito.when(projection.getAuthorId()).thenReturn(2L);
        Mockito.when(projection.getContent()).thenReturn("hi");

        TweetResponse response = mapper.toResponse(projection);

        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals(Long.valueOf(2L), response.getAuthorId());
        assertEquals("hi", response.getContent());
    }

    @Test
    public void toResponseListMapsEachProjection() {
        TweetProjection first = Mockito.mock(TweetProjection.class);
        Mockito.when(first.getId()).thenReturn(1L);
        TweetProjection second = Mockito.mock(TweetProjection.class);
        Mockito.when(second.getId()).thenReturn(2L);

        List<TweetResponse> responses = mapper.toResponseList(List.of(first, second));

        assertEquals(2, responses.size());
        assertEquals(Long.valueOf(1L), responses.get(0).getId());
        assertEquals(Long.valueOf(2L), responses.get(1).getId());
    }
}
