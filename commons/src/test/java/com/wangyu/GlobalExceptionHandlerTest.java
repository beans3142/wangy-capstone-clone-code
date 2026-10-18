package com.wangyu;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleApiRequestExceptionReturnsDeclaredStatusAndMessage() {
        ApiRequestException exception = new ApiRequestException("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

        ResponseEntity<String> response = handler.handleApiRequestException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("유저를 찾을 수 없습니다.", response.getBody());
    }
}
