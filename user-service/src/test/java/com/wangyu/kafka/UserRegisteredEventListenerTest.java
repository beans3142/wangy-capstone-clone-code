package com.wangyu.kafka;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserRegisteredEventListenerTest {

    private UserRegisteredEventProducer producer;
    private UserRegisteredEventListener listener;

    @Before
    public void setUp() {
        producer = mock(UserRegisteredEventProducer.class);
        listener = new UserRegisteredEventListener(producer);
    }

    @Test
    public void delegatesEventToProducerAfterCommit() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");

        listener.onUserRegistered(event);

        verify(producer, times(1)).publish(event);
    }
}
