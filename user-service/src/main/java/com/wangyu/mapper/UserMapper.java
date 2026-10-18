package com.wangyu.mapper;

import com.wangyu.dto.AuthUserResponse;
import com.wangyu.dto.LoginResponse;
import com.wangyu.dto.LoginResult;
import com.wangyu.dto.SignUpResponse;
import com.wangyu.dto.UserProfileResponse;
import com.wangyu.entity.User;
import com.wangyu.repository.UserAuthProjection;
import com.wangyu.repository.UserProfileProjection;
import com.wangyu.BasicMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements BasicMapper {

    public SignUpResponse toSignUpResponse(User user) {
        return convertToDto(user, SignUpResponse.class);
    }

    public LoginResponse toLoginResponse(LoginResult loginResult) {
        return convertToDto(loginResult, LoginResponse.class);
    }

    public UserProfileResponse toProfileResponse(UserProfileProjection projection) {
        return convertToDto(projection, UserProfileResponse.class);
    }

    public AuthUserResponse toAuthUserResponse(UserAuthProjection projection) {
        return convertToDto(projection, AuthUserResponse.class);
    }
}
