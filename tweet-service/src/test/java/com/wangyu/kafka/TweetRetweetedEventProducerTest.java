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

public class TweetRetweetedEventProducerTest {

    private KafkaTemplate<String, TweetRetweetedEvent> kafkaTemplate;
    private TweetRetweetedEventProducer producer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new TweetRetweetedEventProducer(kafkaTemplate, "tweet-retweeted");
    }

    @Test
    public void publishesEventToConfiguredTopic() {
        CompletableFuture<SendResult<String, TweetRetweetedEvent>> future = new CompletableFuture<>();
        future.complete(null);
        when(kafkaTemplate.send(eq("tweet-retweeted"), any(), any())).thenReturn(future);

        TweetRetweetedEvent event = new TweetRetweetedEvent(1L, 2L, 3L, true);
        producer.publish(event);

        verify(kafkaTemplate, times(1)).send("tweet-retweeted", "1", event);
    }

    @Test
    public void publishSwallowsExceptionSoRetweetToggleStillSucceeds() {
        when(kafkaTemplate.send(any(String.class), any(), any()))
                .thenThrow(new org.springframework.kafka.KafkaException("broker unreachable"));

        producer.publish(new TweetRetweetedEvent(1L, 2L, 3L, true));
    }

    @Test
    public void publishLogsWhenSendCompletesExceptionally() {
        CompletableFuture<SendResult<String, TweetRetweetedEvent>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("send failed"));
        when(kafkaTemplate.send(any(String.class), any(), any())).thenReturn(future);

        producer.publish(new TweetRetweetedEvent(1L, 2L, 3L, false));
    }
}
