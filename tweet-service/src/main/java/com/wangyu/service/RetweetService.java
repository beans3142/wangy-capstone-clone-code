package com.wangyu.service;

import com.wangyu.ApiRequestException;
import com.wangyu.dto.RetweetToggleResponse;
import com.wangyu.entity.Retweet;
import com.wangyu.entity.Tweet;
import com.wangyu.kafka.TweetRetweetedEvent;
import com.wangyu.kafka.TweetRetweetedEventProducer;
import com.wangyu.repository.RetweetRepository;
import com.wangyu.repository.TweetProjection;
import com.wangyu.repository.TweetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetweetService {

    private final TweetRepository tweetRepository;
    private final RetweetRepository retweetRepository;
    private final TweetRetweetedEventProducer tweetRetweetedEventProducer;

    public RetweetService(TweetRepository tweetRepository,
                           RetweetRepository retweetRepository,
                           TweetRetweetedEventProducer tweetRetweetedEventProducer) {
        this.tweetRepository = tweetRepository;
        this.retweetRepository = retweetRepository;
        this.tweetRetweetedEventProducer = tweetRetweetedEventProducer;
    }

    @Transactional
    public RetweetToggleResponse toggleRetweet(Long tweetId, Long userId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 트윗입니다", HttpStatus.NOT_FOUND));

        boolean retweeted = retweetRepository.findByTweetIdAndUserId(tweetId, userId)
                .map(existing -> {
                    retweetRepository.delete(existing);
                    tweetRepository.decrementRetweetCount(tweetId);
                    return false;
                })
                .orElseGet(() -> {
                    retweetRepository.save(new Retweet(tweetId, userId));
                    tweetRepository.incrementRetweetCount(tweetId);
                    return true;
                });

        long retweetCount = tweetRepository.getTweetById(tweetId, TweetProjection.class)
                .map(TweetProjection::getRetweetCount)
                .orElse(0L);

        tweetRetweetedEventProducer.publish(new TweetRetweetedEvent(tweetId, tweet.getAuthorId(), userId, retweeted));
        return new RetweetToggleResponse(retweeted, retweetCount);
    }
}
