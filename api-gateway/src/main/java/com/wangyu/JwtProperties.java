package com.wangyu;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;
    private List<String> freePaths = List.of();

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getFreePaths() {
        return freePaths;
    }

    public void setFreePaths(List<String> freePaths) {
        this.freePaths = freePaths;
    }
}
