package com.wangyu;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleApiRequestExceptionReturnsDeclaredStatusAndMessage() {
        ApiRequestException exception = new ApiRequestException("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorResponse> response = handler.handleApiRequestException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("유저를 찾을 수 없습니다.", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void handleUnexpectedExceptionHidesStackTraceAndReturns500() {
        ResponseEntity<ErrorResponse> response =
                handler.handleUnexpectedException(new NullPointerException("internal detail"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("예기치 못한 서버 오류가 발생했습니다.", response.getBody().getMessage());
    }
}
