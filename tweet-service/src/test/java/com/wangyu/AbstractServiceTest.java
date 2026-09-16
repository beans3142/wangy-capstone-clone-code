package com.wangyu;

import com.wangyu.client.UserServiceClient;
import com.wangyu.kafka.HashtagParsedEventProducer;
import com.wangyu.kafka.MentionParsedEventProducer;
import com.wangyu.kafka.TweetLikedEventProducer;
import com.wangyu.kafka.TweetRetweetedEventProducer;
import com.wangyu.repository.BookmarkRepository;
import com.wangyu.repository.RetweetRepository;
import com.wangyu.repository.TweetLikeRepository;
import com.wangyu.repository.TweetRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * architecture.md 4.4: 서비스 단위 테스트는 Repository/Kafka Producer/Feign Client를
 * @MockBean으로 대체해 실제 DB/Kafka/user-service 호출 없이 돌아간다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractServiceTest {

    @MockBean
    protected TweetRepository tweetRepository;

    @MockBean
    protected TweetLikeRepository tweetLikeRepository;

    @MockBean
    protected RetweetRepository retweetRepository;

    @MockBean
    protected BookmarkRepository bookmarkRepository;

    @MockBean
    protected UserServiceClient userServiceClient;

    @MockBean
    protected HashtagParsedEventProducer hashtagParsedEventProducer;

    @MockBean
    protected MentionParsedEventProducer mentionParsedEventProducer;

    @MockBean
    protected TweetLikedEventProducer tweetLikedEventProducer;

    @MockBean
    protected TweetRetweetedEventProducer tweetRetweetedEventProducer;
}
