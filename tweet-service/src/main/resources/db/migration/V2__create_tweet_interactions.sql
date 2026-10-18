ALTER TABLE tweets
    ADD COLUMN like_count    BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN retweet_count BIGINT NOT NULL DEFAULT 0;

CREATE TABLE tweet_likes (
    id         BIGSERIAL PRIMARY KEY,
    tweet_id   BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tweet_likes_tweet_user UNIQUE (tweet_id, user_id)
);

CREATE INDEX ix_tweet_likes_tweet ON tweet_likes (tweet_id);

CREATE TABLE retweets (
    id         BIGSERIAL PRIMARY KEY,
    tweet_id   BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_retweets_tweet_user UNIQUE (tweet_id, user_id)
);

CREATE INDEX ix_retweets_tweet ON retweets (tweet_id);

CREATE TABLE bookmarks (
    id         BIGSERIAL PRIMARY KEY,
    tweet_id   BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_bookmarks_tweet_user UNIQUE (tweet_id, user_id)
);

CREATE INDEX ix_bookmarks_user_created ON bookmarks (user_id, created_at DESC);
