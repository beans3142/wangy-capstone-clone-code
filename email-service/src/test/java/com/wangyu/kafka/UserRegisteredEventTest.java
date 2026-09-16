package com.wangyu.kafka;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserRegisteredEventTest {

    @Test
    public void exposesAllPayloadFields() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");

        assertEquals(Long.valueOf(1L), event.getUserId());
        assertEquals("wangyu@example.com", event.getEmail());
        assertEquals("wangyu", event.getNickname());
        assertEquals("code-123", event.getActivationCode());
    }
}
