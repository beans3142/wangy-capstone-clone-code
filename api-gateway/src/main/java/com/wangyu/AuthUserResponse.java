package com.wangyu;

/**
 * user-service의 GET /api/v1/auth/user/{email} 계약 응답을 그대로 받는 DTO다(architecture.md 4.1).
 * 게이트웨이는 id만 쓰지만 계약 형태를 그대로 유지해 필드 매핑이 깨지지 않게 한다.
 */
public class AuthUserResponse {

    private Long id;
    private String email;
    private String activationCode;

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
