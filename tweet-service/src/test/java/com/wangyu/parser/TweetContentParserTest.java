package com.wangyu.parser;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TweetContentParserTest {

    @Test
    public void extractHashtagsFindsKoreanAndEnglishTags() {
        List<String> hashtags = TweetContentParser.extractHashtags("#캡스톤 화이팅 #capstone");

        assertEquals(List.of("캡스톤", "capstone"), hashtags);
    }

    @Test
    public void extractMentionsFindsNicknames() {
        List<String> mentions = TweetContentParser.extractMentions("#캡스톤 화이팅 @친구");

        assertEquals(List.of("친구"), mentions);
    }

    @Test
    public void extractHashtagsDeduplicatesRepeatedTags() {
        List<String> hashtags = TweetContentParser.extractHashtags("#capstone 화이팅 #capstone 화이팅");

        assertEquals(List.of("capstone"), hashtags);
    }

    @Test
    public void extractHashtagsReturnsEmptyWhenNoneFound() {
        assertTrue(TweetContentParser.extractHashtags("그냥 평범한 트윗").isEmpty());
    }

    @Test
    public void extractMentionsReturnsEmptyWhenNoneFound() {
        assertTrue(TweetContentParser.extractMentions("그냥 평범한 트윗").isEmpty());
    }

    @Test
    public void extractHashtagsHandlesNullAndBlankSafely() {
        assertTrue(TweetContentParser.extractHashtags(null).isEmpty());
        assertTrue(TweetContentParser.extractHashtags("   ").isEmpty());
    }

    @Test
    public void extractMentionsHandlesNullAndBlankSafely() {
        assertTrue(TweetContentParser.extractMentions(null).isEmpty());
        assertTrue(TweetContentParser.extractMentions("   ").isEmpty());
    }
}
