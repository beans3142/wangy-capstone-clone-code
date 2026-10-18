package com.wangyu.kafka;

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
