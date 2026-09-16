package com.wangyu.mapper;

import com.wangyu.dto.AuthUserResponse;
import com.wangyu.dto.LoginResponse;
import com.wangyu.dto.LoginResult;
import com.wangyu.dto.SignUpResponse;
import com.wangyu.dto.UserProfileResponse;
import com.wangyu.entity.User;
import com.wangyu.repository.UserAuthProjection;
import com.wangyu.repository.UserProfileProjection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    public void mapsUserToSignUpResponse() {
        User user = new User("wangyu@example.com", "hashed", "wangyu", "code-123");

        SignUpResponse response = mapper.toSignUpResponse(user);

        assertEquals("wangyu@example.com", response.getEmail());
        assertEquals("wangyu", response.getNickname());
    }

    @Test
    public void mapsLoginResultToLoginResponse() {
        LoginResult result = new LoginResult(1L, "wangyu@example.com", "wangyu", "token-value");

        LoginResponse response = mapper.toLoginResponse(result);

        assertEquals("token-value", response.getToken());
        assertEquals(1L, response.getId().longValue());
    }

    @Test
    public void mapsProfileProjectionToProfileResponse() {
        UserProfileProjection projection = new UserProfileProjection() {
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
                return "https://example.com/p.png";
            }
        };

        UserProfileResponse response = mapper.toProfileResponse(projection);

        assertEquals("wangyu", response.getNickname());
        assertEquals("bio", response.getBio());
    }

    @Test
    public void mapsAuthProjectionToAuthUserResponse() {
        UserAuthProjection projection = new UserAuthProjection() {
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

        AuthUserResponse response = mapper.toAuthUserResponse(projection);

        assertEquals("wangyu@example.com", response.getEmail());
        assertEquals("code-123", response.getActivationCode());
    }
}
