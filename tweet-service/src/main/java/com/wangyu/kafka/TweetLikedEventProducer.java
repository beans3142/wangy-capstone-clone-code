package com.wangyu.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 알림 소비(Phase 9)는 이 서비스의 책임이 아니므로 이벤트 발행까지만 담당한다.
 * Kafka 전송 실패가 좋아요 토글 트랜잭션을 막아서는 안 되므로 예외를 여기서 흡수한다.
 */
@Component
public class TweetLikedEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TweetLikedEventProducer.class);

    private final KafkaTemplate<String, TweetLikedEvent> kafkaTemplate;
    private final String topic;

    public TweetLikedEventProducer(KafkaTemplate<String, TweetLikedEvent> kafkaTemplate,
                                    @Value("${tweet-service.kafka.tweet-liked-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(TweetLikedEvent event) {
        try {
            kafkaTemplate.send(topic, String.valueOf(event.getTweetId()), event)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.warn("Failed to publish tweet-liked event for tweet {}", event.getTweetId(), throwable);
                        }
                    });
        } catch (RuntimeException exception) {
            log.warn("Failed to publish tweet-liked event for tweet {}", event.getTweetId(), exception);
        }
    }
}
