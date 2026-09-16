package com.wangyu;

import java.util.List;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeaderResponseTest {

    @Test
    public void ofPutsMetaInHeadersAndContentInBody() {
        List<String> content = List.of("a", "b");

        ResponseEntity<List<String>> response = HeaderResponse.of(content, 42L, true);

        assertEquals(content, response.getBody());
        assertEquals("42", response.getHeaders().getFirst(HeaderResponse.TOTAL_COUNT_HEADER));
        assertEquals("true", response.getHeaders().getFirst(HeaderResponse.HAS_NEXT_HEADER));
    }

    @Test
    public void ofReflectsNoNextPageWhenLastPage() {
        ResponseEntity<List<String>> response = HeaderResponse.of(List.of(), 0L, false);

        assertTrue(response.getBody().isEmpty());
        assertEquals("false", response.getHeaders().getFirst(HeaderResponse.HAS_NEXT_HEADER));
    }
}
