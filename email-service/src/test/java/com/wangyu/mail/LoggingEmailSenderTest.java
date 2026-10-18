package com.wangyu.mail;

import org.junit.Test;

public class LoggingEmailSenderTest {

    private final LoggingEmailSender sender = new LoggingEmailSender();

    @Test
    public void sendActivationEmailDoesNotThrow() {
        sender.sendActivationEmail("wangyu@example.com", "wangyu", "code-123");
    }
}
