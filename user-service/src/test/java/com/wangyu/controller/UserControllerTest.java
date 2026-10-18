package com.wangyu.controller;

import com.wangyu.dto.UserProfileResponse;
import com.wangyu.mapper.UserMapper;
import com.wangyu.repository.UserProfileProjection;
import com.wangyu.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserService userService;
    private UserMapper userMapper;
    private UserController controller;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        userMapper = mock(UserMapper.class);
        controller = new UserController(userService, userMapper);
    }

    @Test
    public void getProfileReturns200WithPublicFields() {
        UserProfileProjection projection = mock(UserProfileProjection.class);
        UserProfileResponse response = new UserProfileResponse(1L, "wangyu", "bio", "https://example.com/p.png");
        when(userService.getPublicProfile(1L)).thenReturn(projection);
        when(userMapper.toProfileResponse(projection)).thenReturn(response);

        ResponseEntity<UserProfileResponse> result = controller.getProfile(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("wangyu", result.getBody().getNickname());
    }
}
