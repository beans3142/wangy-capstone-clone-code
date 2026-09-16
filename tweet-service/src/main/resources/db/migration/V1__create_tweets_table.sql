CREATE TABLE tweets (
    id          BIGSERIAL    PRIMARY KEY,
    author_id   BIGINT       NOT NULL,
    content     VARCHAR(280) NOT NULL,
    image_url   VARCHAR(500),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_tweets_author_created ON tweets (author_id, created_at DESC);
