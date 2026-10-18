package com.wangyu.controller;

import com.wangyu.dto.UserProfileResponse;
import com.wangyu.mapper.UserMapper;
import com.wangyu.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * 다른 사용자가 조회할 수 있는 공개 프로필을 반환한다.
     * @param id 조회할 사용자 식별자
     * @return 닉네임, 소개, 프로필 이미지 URL을 담은 공개 프로필
     * @throws com.wangyu.ApiRequestException 사용자가 존재하지 않는 경우
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        UserProfileResponse response = userMapper.toProfileResponse(userService.getPublicProfile(id));
        return ResponseEntity.ok(response);
    }
}
