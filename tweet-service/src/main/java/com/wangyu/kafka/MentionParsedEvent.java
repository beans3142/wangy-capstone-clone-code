package com.wangyu.kafka;

public class MentionParsedEvent {

    private Long tweetId;
    private Long authorId;
    private String mentionedNickname;

    protected MentionParsedEvent() {
    }

    public MentionParsedEvent(Long tweetId, Long authorId, String mentionedNickname) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.mentionedNickname = mentionedNickname;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getMentionedNickname() {
        return mentionedNickname;
    }
}
