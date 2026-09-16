package com.wangyu;

import feign.RequestTemplate;
import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeignRequestInterceptorTest {

    private final FeignRequestInterceptor interceptor = new FeignRequestInterceptor();

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void relaysAuthorizationHeaderFromIncomingRequest() {
        MockHttpServletRequest incomingRequest = new MockHttpServletRequest();
        incomingRequest.addHeader("Authorization", "Bearer relayed-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(incomingRequest));

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertEquals("Bearer relayed-token", template.headers().get("Authorization").iterator().next());
    }

    @Test
    public void doesNothingWhenNoIncomingRequestContextExists() {
        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().isEmpty());
    }
}
