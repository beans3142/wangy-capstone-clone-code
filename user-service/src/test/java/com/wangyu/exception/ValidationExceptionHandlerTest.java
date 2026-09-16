package com.wangyu.exception;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationExceptionHandlerTest {

    private final ValidationExceptionHandler handler = new ValidationExceptionHandler();

    @Test
    public void mapsFieldErrorsToFieldNameAndMessage() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("signUpRequest", "email", "이메일 형식이 올바르지 않습니다")));

        Map<String, String> result = handler.handleValidationException(exception);

        assertEquals("이메일 형식이 올바르지 않습니다", result.get("email"));
    }
}
