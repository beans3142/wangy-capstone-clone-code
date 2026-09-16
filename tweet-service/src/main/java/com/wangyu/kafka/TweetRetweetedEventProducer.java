package com.wangyu.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TweetRetweetedEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TweetRetweetedEventProducer.class);

    private final KafkaTemplate<String, TweetRetweetedEvent> kafkaTemplate;
    private final String topic;

    public TweetRetweetedEventProducer(KafkaTemplate<String, TweetRetweetedEvent> kafkaTemplate,
                                        @Value("${tweet-service.kafka.tweet-retweeted-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(TweetRetweetedEvent event) {
        try {
            kafkaTemplate.send(topic, String.valueOf(event.getTweetId()), event)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.warn("Failed to publish tweet-retweeted event for tweet {}", event.getTweetId(), throwable);
                        }
                    });
        } catch (RuntimeException exception) {
            log.warn("Failed to publish tweet-retweeted event for tweet {}", event.getTweetId(), exception);
        }
    }
}
