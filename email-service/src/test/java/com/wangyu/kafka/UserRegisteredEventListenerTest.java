package com.wangyu.kafka;

import com.wangyu.mail.EmailSender;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class UserRegisteredEventListenerTest {

    private EmailSender emailSender;
    private UserRegisteredEventListener listener;

    @Before
    public void setUp() {
        emailSender = mock(EmailSender.class);
        listener = new UserRegisteredEventListener(emailSender);
    }

    @Test
    public void sendsActivationEmailWhenActivationCodePresent() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "code-123");

        listener.onUserRegistered(event);

        verify(emailSender, times(1))
                .sendActivationEmail("wangyu@example.com", "wangyu", "code-123");
    }

    @Test
    public void skipsSendingWhenActivationCodeIsMissing() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", null);

        listener.onUserRegistered(event);

        verifyNoInteractions(emailSender);
    }

    @Test
    public void skipsSendingWhenActivationCodeIsBlank() {
        UserRegisteredEvent event = new UserRegisteredEvent(1L, "wangyu@example.com", "wangyu", "   ");

        listener.onUserRegistered(event);

        verifyNoInteractions(emailSender);
    }
}
