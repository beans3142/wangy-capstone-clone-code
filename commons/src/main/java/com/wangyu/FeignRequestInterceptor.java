package com.wangyu;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest currentRequest = getCurrentRequest();
        if (currentRequest == null) {
            return;
        }

        String authorizationHeader = currentRequest.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && !authorizationHeader.isBlank()) {
            template.header(AUTHORIZATION_HEADER, authorizationHeader);
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }
}
