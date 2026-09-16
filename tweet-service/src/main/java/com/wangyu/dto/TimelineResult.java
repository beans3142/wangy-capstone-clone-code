package com.wangyu.dto;

import java.util.List;

import com.wangyu.repository.TweetProjection;

/**
 * 타임라인 조회 결과를 컨텐츠와 페이징 메타(총 개수/다음 페이지 여부)로 함께 전달하기 위한
 * 서비스-컨트롤러 간 내부 전송 객체다. 메타는 HeaderResponse를 통해 헤더로만 노출된다.
 */
public class TimelineResult {

    private final List<TweetProjection> content;
    private final long totalCount;
    private final boolean hasNext;

    public TimelineResult(List<TweetProjection> content, long totalCount, boolean hasNext) {
        this.content = content;
        this.totalCount = totalCount;
        this.hasNext = hasNext;
    }

    public List<TweetProjection> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
