package com.wangyu.kafka;

/**
 * user-service가 발행하는 이벤트와 동일한 형태의 소비측 표현이다.
 * 서로 다른 마이크로서비스의 클래스를 직접 import할 수 없으므로(도메인 경계),
 * Kafka 페이로드 계약(userId, email, nickname, activationCode)만 이 클래스로 복제한다.
 */
public class UserRegisteredEvent {

    private Long userId;
    private String email;
    private String nickname;
    private String activationCode;

    protected UserRegisteredEvent() {
    }

    public UserRegisteredEvent(Long userId, String email, String nickname, String activationCode) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.activationCode = activationCode;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getActivationCode() {
        return activationCode;
    }
}
