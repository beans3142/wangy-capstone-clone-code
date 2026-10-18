package com.wangyu.kafka;

public class TweetRetweetedEvent {

    private Long tweetId;
    private Long authorId;
    private Long actorUserId;
    private boolean retweeted;

    protected TweetRetweetedEvent() {
    }

    public TweetRetweetedEvent(Long tweetId, Long authorId, Long actorUserId, boolean retweeted) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.actorUserId = actorUserId;
        this.retweeted = retweeted;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getActorUserId() {
        return actorUserId;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
}
