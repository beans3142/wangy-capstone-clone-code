package com.wangyu.service;

import java.util.ArrayList;
import java.util.List;

import com.wangyu.ApiRequestException;
import com.wangyu.client.UserServiceClient;
import com.wangyu.dto.TimelineResult;
import com.wangyu.entity.Tweet;
import com.wangyu.kafka.HashtagParsedEvent;
import com.wangyu.kafka.HashtagParsedEventProducer;
import com.wangyu.kafka.MentionParsedEvent;
import com.wangyu.kafka.MentionParsedEventProducer;
import com.wangyu.parser.TweetContentParser;
import com.wangyu.repository.TweetProjection;
import com.wangyu.repository.TweetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserServiceClient userServiceClient;
    private final HashtagParsedEventProducer hashtagParsedEventProducer;
    private final MentionParsedEventProducer mentionParsedEventProducer;

    public TweetService(TweetRepository tweetRepository,
                         UserServiceClient userServiceClient,
                         HashtagParsedEventProducer hashtagParsedEventProducer,
                         MentionParsedEventProducer mentionParsedEventProducer) {
        this.tweetRepository = tweetRepository;
        this.userServiceClient = userServiceClient;
        this.hashtagParsedEventProducer = hashtagParsedEventProducer;
        this.mentionParsedEventProducer = mentionParsedEventProducer;
    }

    @Transactional
    public Tweet createTweet(Long authorId, String content, String imageUrl) {
        Tweet tweet = tweetRepository.save(new Tweet(authorId, content, imageUrl));
        publishParsedEvents(tweet);
        return tweet;
    }

    @Transactional(readOnly = true)
    public TweetProjection getTweet(Long id) {
        return tweetRepository.getTweetById(id, TweetProjection.class)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 트윗입니다", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public TimelineResult getTimeline(Long userId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ApiRequestException("page는 0 이상, size는 1 이상이어야 합니다", HttpStatus.BAD_REQUEST);
        }

        List<Long> authorIds = new ArrayList<>(userServiceClient.getFollowingIds(userId));
        authorIds.add(userId);

        Pageable pageable = PageRequest.of(page, size);
        List<TweetProjection> content =
                tweetRepository.findByAuthorIdInOrderByCreatedAtDesc(authorIds, pageable, TweetProjection.class);
        long totalCount = tweetRepository.countByAuthorIdIn(authorIds);
        boolean hasNext = (long) (page + 1) * size < totalCount;

        return new TimelineResult(content, totalCount, hasNext);
    }

    @Transactional
    public Tweet updateTweet(Long id, Long authorId, String content) {
        Tweet tweet = getOwnedTweet(id, authorId);
        tweet.changeContent(content);
        return tweet;
    }

    @Transactional
    public void deleteTweet(Long id, Long authorId) {
        Tweet tweet = getOwnedTweet(id, authorId);
        tweetRepository.delete(tweet);
    }

    private Tweet getOwnedTweet(Long id, Long authorId) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 트윗입니다", HttpStatus.NOT_FOUND));
        if (!tweet.isAuthoredBy(authorId)) {
            throw new ApiRequestException("본인이 작성한 트윗만 수정/삭제할 수 있습니다", HttpStatus.FORBIDDEN);
        }
        return tweet;
    }

    private void publishParsedEvents(Tweet tweet) {
        for (String tag : TweetContentParser.extractHashtags(tweet.getContent())) {
            hashtagParsedEventProducer.publish(new HashtagParsedEvent(tweet.getId(), tweet.getAuthorId(), tag));
        }
        for (String mention : TweetContentParser.extractMentions(tweet.getContent())) {
            mentionParsedEventProducer.publish(new MentionParsedEvent(tweet.getId(), tweet.getAuthorId(), mention));
        }
    }
}
