package com.wangyu.controller;

import com.wangyu.dto.AuthUserResponse;
import com.wangyu.dto.LoginRequest;
import com.wangyu.dto.LoginResponse;
import com.wangyu.dto.LoginResult;
import com.wangyu.dto.SignUpRequest;
import com.wangyu.dto.SignUpResponse;
import com.wangyu.entity.User;
import com.wangyu.mapper.UserMapper;
import com.wangyu.repository.UserAuthProjection;
import com.wangyu.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private UserService userService;
    private UserMapper userMapper;
    private AuthController controller;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        userMapper = mock(UserMapper.class);
        controller = new AuthController(userService, userMapper);
    }

    @Test
    public void signUpReturns201WithCreatedAccount() {
        SignUpRequest request = new SignUpRequest("wangyu@example.com", "password123", "wangyu");
        User savedUser = new User("wangyu@example.com", "hashed", "wangyu", "code-123");
        SignUpResponse response = new SignUpResponse(1L, "wangyu@example.com", "wangyu");
        when(userService.signUp(request)).thenReturn(savedUser);
        when(userMapper.toSignUpResponse(savedUser)).thenReturn(response);

        ResponseEntity<SignUpResponse> result = controller.signUp(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("wangyu", result.getBody().getNickname());
    }

    @Test
    public void loginReturns200WithToken() {
        LoginRequest request = new LoginRequest("wangyu@example.com", "password123");
        LoginResult loginResult = new LoginResult(1L, "wangyu@example.com", "wangyu", "token-value");
        LoginResponse response = new LoginResponse(1L, "wangyu@example.com", "wangyu", "token-value");
        when(userService.login(request)).thenReturn(loginResult);
        when(userMapper.toLoginResponse(loginResult)).thenReturn(response);

        ResponseEntity<LoginResponse> result = controller.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("token-value", result.getBody().getToken());
    }

    @Test
    public void getAuthUserReturnsGatewayContractFields() {
        UserAuthProjection projection = mock(UserAuthProjection.class);
        AuthUserResponse response = new AuthUserResponse(1L, "wangyu@example.com", "code-123");
        when(userService.getAuthUserByEmail("wangyu@example.com")).thenReturn(projection);
        when(userMapper.toAuthUserResponse(projection)).thenReturn(response);

        ResponseEntity<AuthUserResponse> result = controller.getAuthUser("wangyu@example.com");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("code-123", result.getBody().getActivationCode());
    }

    @Test
    public void activateReturns200WithActivatedAccount() {
        User activatedUser = new User("wangyu@example.com", "hashed", "wangyu", "code-123");
        activatedUser.activate();
        SignUpResponse response = new SignUpResponse(1L, "wangyu@example.com", "wangyu");
        when(userService.activateAccount("code-123")).thenReturn(activatedUser);
        when(userMapper.toSignUpResponse(activatedUser)).thenReturn(response);

        ResponseEntity<SignUpResponse> result = controller.activate("code-123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
