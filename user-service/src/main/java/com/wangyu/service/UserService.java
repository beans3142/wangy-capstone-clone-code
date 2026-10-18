package com.wangyu.service;

import java.util.Map;
import java.util.UUID;

import com.wangyu.JwtProvider;
import com.wangyu.ApiRequestException;
import com.wangyu.dto.LoginRequest;
import com.wangyu.dto.LoginResult;
import com.wangyu.dto.SignUpRequest;
import com.wangyu.entity.User;
import com.wangyu.kafka.UserRegisteredEvent;
import com.wangyu.repository.UserAuthProjection;
import com.wangyu.repository.UserProfileProjection;
import com.wangyu.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "이메일 또는 비밀번호가 올바르지 않습니다";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtProvider jwtProvider,
                        ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public User signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiRequestException("이미 가입된 이메일입니다", HttpStatus.CONFLICT);
        }

        String activationCode = UUID.randomUUID().toString();
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                activationCode);
        User savedUser = userRepository.save(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(
                savedUser.getId(), savedUser.getEmail(), savedUser.getNickname(), activationCode));

        return savedUser;
    }

    @Transactional(readOnly = true)
    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiRequestException(INVALID_CREDENTIALS_MESSAGE, HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiRequestException(INVALID_CREDENTIALS_MESSAGE, HttpStatus.UNAUTHORIZED);
        }

        if (user.isLoginBlocked()) {
            throw new ApiRequestException("차단되었거나 탈퇴한 계정입니다", HttpStatus.FORBIDDEN);
        }

        String token = jwtProvider.generateToken(user.getEmail(), Map.of("userId", user.getId()));
        return new LoginResult(user.getId(), user.getEmail(), user.getNickname(), token);
    }

    @Transactional(readOnly = true)
    public UserProfileProjection getPublicProfile(Long id) {
        return userRepository.getUserById(id, UserProfileProjection.class)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 사용자입니다", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserAuthProjection getAuthUserByEmail(String email) {
        return userRepository.getUserByEmail(email, UserAuthProjection.class)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 사용자입니다", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User activateAccount(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ApiRequestException("유효하지 않은 인증 코드입니다", HttpStatus.BAD_REQUEST));
        user.activate();
        return user;
    }
}
