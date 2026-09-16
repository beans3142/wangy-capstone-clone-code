package com.wangyu;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class GatewayRouteProperties {

    private List<String> freePaths = List.of();

    public List<String> getFreePaths() {
        return freePaths;
    }

    public void setFreePaths(List<String> freePaths) {
        this.freePaths = freePaths;
    }
}
