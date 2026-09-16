package com.wangyu.controller;

import com.wangyu.dto.AuthUserResponse;
import com.wangyu.dto.LoginRequest;
import com.wangyu.dto.LoginResponse;
import com.wangyu.dto.SignUpRequest;
import com.wangyu.dto.SignUpResponse;
import com.wangyu.mapper.UserMapper;
import com.wangyu.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * 이메일/비밀번호/닉네임으로 신규 계정을 생성한다. 인증 메일 발송은 Kafka를 통해
     * email-service에 비동기로 위임되며, 이 응답을 기다리지 않는다.
     * @param request 가입 요청 (이메일, 비밀번호, 닉네임)
     * @return 생성된 계정의 공개 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = userMapper.toSignUpResponse(userService.signUp(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 이메일/비밀번호로 로그인하고 JWT를 발급받는다.
     * @param request 로그인 요청 (이메일, 비밀번호)
     * @return 발급된 토큰과 계정 정보
     * @throws com.wangyu.ApiRequestException 자격 증명이 올바르지 않거나 계정이 차단/탈퇴 상태인 경우
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userMapper.toLoginResponse(userService.login(request));
        return ResponseEntity.ok(response);
    }

    /**
     * api-gateway 인증 흐름(architecture.md 4.1)이 호출하는 내부 계약 엔드포인트다.
     * @param email 조회할 계정의 이메일
     * @return 계정 식별자, 이메일, 활성화 코드
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<AuthUserResponse> getAuthUser(@PathVariable String email) {
        AuthUserResponse response = userMapper.toAuthUserResponse(userService.getAuthUserByEmail(email));
        return ResponseEntity.ok(response);
    }

    /**
     * 가입 시 발급된 인증 코드로 계정을 활성화한다.
     * @param code 인증 메일에 담긴 활성화 코드
     * @return 활성화된 계정의 공개 정보
     */
    @PatchMapping("/activate/{code}")
    public ResponseEntity<SignUpResponse> activate(@PathVariable String code) {
        SignUpResponse response = userMapper.toSignUpResponse(userService.activateAccount(code));
        return ResponseEntity.ok(response);
    }
}
