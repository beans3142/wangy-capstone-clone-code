package com.wangyu.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 해시태그 집계(Phase 7 tag-service)는 이 서비스의 책임이 아니므로 이벤트 발행까지만 담당한다.
 * Kafka 전송 실패가 트윗 작성 트랜잭션을 막아서는 안 되므로 예외를 여기서 흡수한다.
 */
@Component
public class HashtagParsedEventProducer {

    private static final Logger log = LoggerFactory.getLogger(HashtagParsedEventProducer.class);

    private final KafkaTemplate<String, HashtagParsedEvent> kafkaTemplate;
    private final String topic;

    public HashtagParsedEventProducer(KafkaTemplate<String, HashtagParsedEvent> kafkaTemplate,
                                       @Value("${tweet-service.kafka.hashtag-parsed-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(HashtagParsedEvent event) {
        try {
            kafkaTemplate.send(topic, String.valueOf(event.getTweetId()), event)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.warn("Failed to publish hashtag-parsed event for tweet {}", event.getTweetId(), throwable);
                        }
                    });
        } catch (RuntimeException exception) {
            log.warn("Failed to publish hashtag-parsed event for tweet {}", event.getTweetId(), exception);
        }
    }
}
