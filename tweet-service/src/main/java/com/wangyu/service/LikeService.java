package com.wangyu.service;

import com.wangyu.ApiRequestException;
import com.wangyu.dto.LikeToggleResponse;
import com.wangyu.entity.Tweet;
import com.wangyu.entity.TweetLike;
import com.wangyu.kafka.TweetLikedEvent;
import com.wangyu.kafka.TweetLikedEventProducer;
import com.wangyu.repository.TweetLikeRepository;
import com.wangyu.repository.TweetProjection;
import com.wangyu.repository.TweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final TweetRepository tweetRepository;
    private final TweetLikeRepository tweetLikeRepository;
    private final TweetLikedEventProducer tweetLikedEventProducer;

    public LikeService(TweetRepository tweetRepository,
                        TweetLikeRepository tweetLikeRepository,
                        TweetLikedEventProducer tweetLikedEventProducer) {
        this.tweetRepository = tweetRepository;
        this.tweetLikeRepository = tweetLikeRepository;
        this.tweetLikedEventProducer = tweetLikedEventProducer;
    }

    /**
     * 좋아요 여부를 토글한다. 이미 좋아요한 상태면 취소, 아니면 새로 좋아요한다.
     * 카운트는 tweet_likes를 세는 대신 tweets.like_count에 원자적 UPDATE(t.likeCount = t.likeCount ± 1)로
     * 반영해 동시 좋아요 100건이 들어와도 각 UPDATE가 DB 행 잠금으로 직렬화되어 정확히 반영된다.
     */
    @Transactional
    public LikeToggleResponse toggleLike(Long tweetId, Long userId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 트윗입니다", HttpStatus.NOT_FOUND));

        boolean liked = tweetLikeRepository.findByTweetIdAndUserId(tweetId, userId)
                .map(existing -> {
                    tweetLikeRepository.delete(existing);
                    tweetRepository.decrementLikeCount(tweetId);
                    return false;
                })
                .orElseGet(() -> {
                    tweetLikeRepository.save(new TweetLike(tweetId, userId));
                    tweetRepository.incrementLikeCount(tweetId);
                    return true;
                });

        long likeCount = tweetRepository.getTweetById(tweetId, TweetProjection.class)
                .map(TweetProjection::getLikeCount)
                .orElse(0L);

        tweetLikedEventProducer.publish(new TweetLikedEvent(tweetId, tweet.getAuthorId(), userId, liked));
        return new LikeToggleResponse(liked, likeCount);
    }
}
