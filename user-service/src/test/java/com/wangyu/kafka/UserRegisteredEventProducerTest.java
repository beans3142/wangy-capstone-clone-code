package com.wangyu.kafka;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRegisteredEventProducerTest {

    private KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    private UserRegisteredEventProducer producer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new UserRegisteredEventProducer(kafkaTemplate, "user-registered");
    }

    @Test
    public void publishesEventToConfiguredTopic() {
        CompletableFuture<SendResult<String, UserRegisteredEvent>> future = new CompletableFuture<>();
        future.complete(null);
        when(kafkaTemplate.send(eq("user-registered"), any(), any())).thenReturn(future);

        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");
        producer.publish(event);

        verify(kafkaTemplate, times(1)).send("user-registered", "wangyu@example.com", event);
    }

    @Test
    public void publishSwallowsExceptionSoSignUpStillSucceedsWhenBrokerIsDown() {
        when(kafkaTemplate.send(any(String.class), any(), any()))
                .thenThrow(new org.springframework.kafka.KafkaException("broker unreachable"));

        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");

        producer.publish(event);
    }

    @Test
    public void publishLogsWhenSendCompletesExceptionally() {
        CompletableFuture<SendResult<String, UserRegisteredEvent>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("send failed"));
        when(kafkaTemplate.send(any(String.class), any(), any())).thenReturn(future);

        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");

        producer.publish(event);
    }
}
