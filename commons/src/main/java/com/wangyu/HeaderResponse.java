package com.wangyu;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * 페이징된 목록 응답에서 총 개수/다음 페이지 여부 같은 메타 정보를 Body가 아닌 HTTP 헤더로 실어 보낸다.
 * Body는 순수 컨텐츠 리스트만 유지해 클라이언트가 별도 래퍼 객체 없이 바로 배열을 소비할 수 있다.
 */
public final class HeaderResponse {

    public static final String TOTAL_COUNT_HEADER = "X-Total-Count";
    public static final String HAS_NEXT_HEADER = "X-Has-Next";

    private HeaderResponse() {
    }

    public static <T> ResponseEntity<List<T>> of(List<T> content, long totalCount, boolean hasNext) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(TOTAL_COUNT_HEADER, String.valueOf(totalCount));
        headers.add(HAS_NEXT_HEADER, String.valueOf(hasNext));
        return ResponseEntity.ok().headers(headers).body(content);
    }
}
