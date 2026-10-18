package com.wangyu.dto;

public class LoginResult {

    private Long id;
    private String email;
    private String nickname;
    private String token;

    protected LoginResult() {
    }

    public LoginResult(Long id, String email, String nickname, String token) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getToken() {
        return token;
    }
}
