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

public class TweetLikedEventProducerTest {

    private KafkaTemplate<String, TweetLikedEvent> kafkaTemplate;
    private TweetLikedEventProducer producer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new TweetLikedEventProducer(kafkaTemplate, "tweet-liked");
    }

    @Test
    public void publishesEventToConfiguredTopic() {
        CompletableFuture<SendResult<String, TweetLikedEvent>> future = new CompletableFuture<>();
        future.complete(null);
        when(kafkaTemplate.send(eq("tweet-liked"), any(), any())).thenReturn(future);

        TweetLikedEvent event = new TweetLikedEvent(1L, 2L, 3L, true);
        producer.publish(event);

        verify(kafkaTemplate, times(1)).send("tweet-liked", "1", event);
    }

    @Test
    public void publishSwallowsExceptionSoLikeToggleStillSucceeds() {
        when(kafkaTemplate.send(any(String.class), any(), any()))
                .thenThrow(new org.springframework.kafka.KafkaException("broker unreachable"));

        producer.publish(new TweetLikedEvent(1L, 2L, 3L, true));
    }

    @Test
    public void publishLogsWhenSendCompletesExceptionally() {
        CompletableFuture<SendResult<String, TweetLikedEvent>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("send failed"));
        when(kafkaTemplate.send(any(String.class), any(), any())).thenReturn(future);

        producer.publish(new TweetLikedEvent(1L, 2L, 3L, false));
    }
}
