package com.wangyu.dto;

/**
 * api-gateway가 게이트웨이 인증 흐름(architecture.md 4.1) 2단계에서 호출하는 계약 응답이다.
 * 필드명(id, email, activationCode)을 임의로 바꾸면 게이트웨이 연동이 깨진다.
 */
public class AuthUserResponse {

    private Long id;
    private String email;
    private String activationCode;

    public AuthUserResponse() {
    }

    public AuthUserResponse(Long id, String email, String activationCode) {
        this.id = id;
        this.email = email;
        this.activationCode = activationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
