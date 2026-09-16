package com.wangyu.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 회원가입 자체의 성공 여부는 이메일 발송 성공 여부와 분리된 관심사다.
 * Kafka 전송이 실패해도 회원가입 트랜잭션에는 영향을 주지 않도록 예외를 여기서 흡수한다.
 */
@Component
public class UserRegisteredEventProducer {

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredEventProducer.class);

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    private final String topic;

    public UserRegisteredEventProducer(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate,
                                        @Value("${user-service.kafka.user-registered-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(UserRegisteredEvent event) {
        try {
            kafkaTemplate.send(topic, event.getEmail(), event)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.warn("Failed to publish user-registered event for {}", event.getEmail(), throwable);
                        }
                    });
        } catch (RuntimeException exception) {
            log.warn("Failed to publish user-registered event for {}", event.getEmail(), exception);
        }
    }
}
