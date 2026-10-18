package com.wangyu.service;

import java.util.Optional;

import com.wangyu.AbstractServiceTest;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.LoginRequest;
import com.wangyu.dto.LoginResult;
import com.wangyu.dto.SignUpRequest;
import com.wangyu.entity.AccountStatus;
import com.wangyu.entity.User;
import com.wangyu.repository.UserAuthProjection;
import com.wangyu.repository.UserProfileProjection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void signUpSavesEncodedPasswordAndPublishesEvent() {
        SignUpRequest request = new SignUpRequest("new@example.com", "password123", "wangyu");
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.signUp(request);

        assertEquals("new@example.com", saved.getEmail());
        assertNotNull(saved.getActivationCode());
        assertTrue(passwordEncoder.matches("password123", saved.getPassword()));
    }

    @Test
    public void signUpRejectsDuplicateEmail() {
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);

        try {
            userService.signUp(new SignUpRequest("dup@example.com", "password123", "wangyu"));
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        }
    }

    @Test
    public void loginIssuesTokenForCorrectCredentials() {
        User user = activeUser("wangyu@example.com", "password123");
        when(userRepository.findByEmail("wangyu@example.com")).thenReturn(Optional.of(user));

        LoginResult result = userService.login(new LoginRequest("wangyu@example.com", "password123"));

        assertEquals("wangyu@example.com", result.getEmail());
        assertNotNull(result.getToken());
    }

    @Test
    public void loginRejectsUnknownEmailWithGenericMessage() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        try {
            userService.login(new LoginRequest("missing@example.com", "password123"));
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
            assertEquals("이메일 또는 비밀번호가 올바르지 않습니다", exception.getMessage());
        }
    }

    @Test
    public void loginRejectsWrongPasswordWithSameGenericMessageAsUnknownEmail() {
        User user = activeUser("wangyu@example.com", "password123");
        when(userRepository.findByEmail("wangyu@example.com")).thenReturn(Optional.of(user));

        try {
            userService.login(new LoginRequest("wangyu@example.com", "wrong-password"));
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
            assertEquals("이메일 또는 비밀번호가 올바르지 않습니다", exception.getMessage());
        }
    }

    @Test
    public void loginRejectsWithdrawnAccount() {
        User user = userWithStatus("withdrawn@example.com", "password123", AccountStatus.WITHDRAWN);
        when(userRepository.findByEmail("withdrawn@example.com")).thenReturn(Optional.of(user));

        try {
            userService.login(new LoginRequest("withdrawn@example.com", "password123"));
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        }
    }

    @Test
    public void loginRejectsBannedAccount() {
        User user = userWithStatus("banned@example.com", "password123", AccountStatus.BANNED);
        when(userRepository.findByEmail("banned@example.com")).thenReturn(Optional.of(user));

        try {
            userService.login(new LoginRequest("banned@example.com", "password123"));
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        }
    }

    @Test
    public void getPublicProfileReturnsProjectionWhenFound() {
        UserProfileProjection projection = mockProfileProjection();
        when(userRepository.getUserById(1L, UserProfileProjection.class)).thenReturn(Optional.of(projection));

        UserProfileProjection result = userService.getPublicProfile(1L);

        assertEquals("wangyu", result.getNickname());
    }

    @Test
    public void getPublicProfileThrowsWhenMissing() {
        when(userRepository.getUserById(99L, UserProfileProjection.class)).thenReturn(Optional.empty());

        try {
            userService.getPublicProfile(99L);
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Test
    public void getAuthUserByEmailReturnsProjectionWhenFound() {
        UserAuthProjection projection = mockAuthProjection();
        when(userRepository.getUserByEmail("wangyu@example.com", UserAuthProjection.class))
                .thenReturn(Optional.of(projection));

        UserAuthProjection result = userService.getAuthUserByEmail("wangyu@example.com");

        assertEquals("wangyu@example.com", result.getEmail());
    }

    @Test
    public void getAuthUserByEmailThrowsWhenMissing() {
        when(userRepository.getUserByEmail(anyString(), any())).thenReturn(Optional.empty());

        try {
            userService.getAuthUserByEmail("missing@example.com");
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Test
    public void activateAccountClearsActivationCode() {
        User user = activeUser("wangyu@example.com", "password123");
        when(userRepository.findByActivationCode("code-123")).thenReturn(Optional.of(user));

        User activated = userService.activateAccount("code-123");

        assertTrue(activated.isActivated());
        assertNull(activated.getActivationCode());
    }

    @Test
    public void activateAccountRejectsUnknownCode() {
        when(userRepository.findByActivationCode("unknown")).thenReturn(Optional.empty());

        try {
            userService.activateAccount("unknown");
            fail("expected ApiRequestException");
        } catch (ApiRequestException exception) {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }

    private User activeUser(String email, String rawPassword) {
        User user = new User(email, passwordEncoder.encode(rawPassword), "wangyu", "code-123");
        setId(user, 1L);
        return user;
    }

    private void setId(User user, Long id) {
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private User userWithStatus(String email, String rawPassword, AccountStatus status) {
        User user = activeUser(email, rawPassword);
        if (status != AccountStatus.ACTIVE) {
            setStatus(user, status);
        }
        return user;
    }

    private void setStatus(User user, AccountStatus status) {
        try {
            var field = User.class.getDeclaredField("status");
            field.setAccessible(true);
            field.set(user, status);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private UserProfileProjection mockProfileProjection() {
        return new UserProfileProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getNickname() {
                return "wangyu";
            }

            @Override
            public String getBio() {
                return "bio";
            }

            @Override
            public String getProfileImageUrl() {
                return "https://example.com/profile.png";
            }
        };
    }

    private UserAuthProjection mockAuthProjection() {
        return new UserAuthProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getEmail() {
                return "wangyu@example.com";
            }

            @Override
            public String getActivationCode() {
                return "code-123";
            }
        };
    }
}
