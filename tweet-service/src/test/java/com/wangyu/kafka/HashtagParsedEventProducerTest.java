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

public class HashtagParsedEventProducerTest {

    private KafkaTemplate<String, HashtagParsedEvent> kafkaTemplate;
    private HashtagParsedEventProducer producer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new HashtagParsedEventProducer(kafkaTemplate, "hashtag-parsed");
    }

    @Test
    public void publishesEventToConfiguredTopic() {
        CompletableFuture<SendResult<String, HashtagParsedEvent>> future = new CompletableFuture<>();
        future.complete(null);
        when(kafkaTemplate.send(eq("hashtag-parsed"), any(), any())).thenReturn(future);

        HashtagParsedEvent event = new HashtagParsedEvent(1L, 2L, "capstone");
        producer.publish(event);

        verify(kafkaTemplate, times(1)).send("hashtag-parsed", "1", event);
    }

    @Test
    public void publishSwallowsExceptionSoTweetCreationStillSucceeds() {
        when(kafkaTemplate.send(any(String.class), any(), any()))
                .thenThrow(new org.springframework.kafka.KafkaException("broker unreachable"));

        producer.publish(new HashtagParsedEvent(1L, 2L, "capstone"));
    }

    @Test
    public void publishLogsWhenSendCompletesExceptionally() {
        CompletableFuture<SendResult<String, HashtagParsedEvent>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("send failed"));
        when(kafkaTemplate.send(any(String.class), any(), any())).thenReturn(future);

        producer.publish(new HashtagParsedEvent(1L, 2L, "capstone"));
    }
}
