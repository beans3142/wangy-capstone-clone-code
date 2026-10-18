package com.wangyu;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign으로 호출하는 서블릿(MVC) 기반 서비스에서만 의미가 있다. {@code @ConditionalOnClass}로
 * 감싸둔 이유: api-gateway처럼 WebFlux 기반이라 서블릿 API 자체가 클래스패스에 없는 소비자가
 * commons를 의존해도(같은 flat 패키지라 컴포넌트 스캔에 걸림) 이 빈이 조용히 스킵되게 하기 위함.
 * (그렇지 않으면 NoClassDefFoundError로 컨텍스트 로딩 자체가 실패한다.)
 */
@Component
@ConditionalOnClass(HttpServletRequest.class)
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
