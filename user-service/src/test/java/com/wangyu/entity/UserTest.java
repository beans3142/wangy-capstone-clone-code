package com.wangyu.entity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void newUserStartsUnactivatedAndActive() {
        User user = new User("wangyu@example.com", "hashed", "wangyu", "code-123");

        assertFalse(user.isActivated());
        assertFalse(user.isLoginBlocked());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    public void activateClearsCodeAndMarksActivated() {
        User user = new User("wangyu@example.com", "hashed", "wangyu", "code-123");

        user.activate();

        assertTrue(user.isActivated());
        assertNull(user.getActivationCode());
    }

    @Test
    public void newUserDefaultsToActiveStatus() {
        User user = new User("wangyu@example.com", "hashed", "wangyu", "code-123");

        assertTrue(AccountStatus.ACTIVE == user.getStatus());
    }
}
