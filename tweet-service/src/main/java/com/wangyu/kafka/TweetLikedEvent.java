package com.wangyu.kafka;

public class TweetLikedEvent {

    private Long tweetId;
    private Long authorId;
    private Long actorUserId;
    private boolean liked;

    protected TweetLikedEvent() {
    }

    public TweetLikedEvent(Long tweetId, Long authorId, Long actorUserId, boolean liked) {
        this.tweetId = tweetId;
        this.authorId = authorId;
        this.actorUserId = actorUserId;
        this.liked = liked;
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

    public boolean isLiked() {
        return liked;
    }
}
