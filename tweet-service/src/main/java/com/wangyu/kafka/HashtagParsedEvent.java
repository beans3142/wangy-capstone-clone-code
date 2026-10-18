package com.wangyu.kafka;

public class HashtagParsedEvent {

    private Long tweetId;
    private Long authorId;
    private String tag;

    protected HashtagParsedEvent() {
    }

    public HashtagParsedEvent(Long tweetId, Long authorId, String tag) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.tag = tag;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTag() {
        return tag;
    }
}
