package com.wangyu.controller;

import java.util.List;

import com.wangyu.HeaderResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.dto.TweetCreateRequest;
import com.wangyu.dto.TweetResponse;
import com.wangyu.dto.TweetUpdateRequest;
import com.wangyu.mapper.TweetMapper;
import com.wangyu.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetController {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final TweetService tweetService;
    private final TweetMapper tweetMapper;

    public TweetController(TweetService tweetService, TweetMapper tweetMapper) {
        this.tweetService = tweetService;
        this.tweetMapper = tweetMapper;
    }

    /**
     * 텍스트(+선택적 이미지 URL) 본문으로 트윗을 작성하고, 본문 안의 해시태그/멘션을 파싱해 이벤트로 발행한다.
     * @param request 작성자 ID와 본문(280자 이하, 공백만으로는 불가)을 담은 요청
     * @return 생성된 트윗
     */
    @PostMapping("/api/v1/tweets")
    public ResponseEntity<TweetResponse> createTweet(@Valid @RequestBody TweetCreateRequest request) {
        TweetResponse response = tweetMapper.toResponse(
                tweetService.createTweet(request.getAuthorId(), request.getContent(), request.getImageUrl()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 트윗 ID로 단일 트윗을 조회한다.
     * @param id 조회할 트윗 식별자
     * @return 트윗 상세
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않는 경우
     */
    @GetMapping("/api/v1/tweets/{id}")
    public ResponseEntity<TweetResponse> getTweet(@PathVariable Long id) {
        return ResponseEntity.ok(tweetMapper.toResponse(tweetService.getTweet(id)));
    }

    /**
     * userId 본인 및 팔로우 중인 사용자들의 트윗을 최신순으로 페이징 조회한다. 총 개수/다음 페이지
     * 여부는 Body가 아닌 HeaderResponse 헤더(X-Total-Count, X-Has-Next)로 전달된다.
     * @param userId 타임라인을 조회할 사용자 식별자
     * @param page 0부터 시작하는 페이지 번호
     * @param size 페이지 크기
     * @return 헤더로 페이징 메타를 담은 트윗 목록 (빈 목록도 정상 응답)
     */
    @GetMapping("/api/v1/users/{userId}/timeline")
    public ResponseEntity<List<TweetResponse>> getTimeline(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        TimelineResult result = tweetService.getTimeline(userId, page, size);
        List<TweetResponse> content = tweetMapper.toResponseList(result.getContent());
        return HeaderResponse.of(content, result.getTotalCount(), result.isHasNext());
    }

    /**
     * 트윗 본문을 수정한다. 작성자 본인만 수정할 수 있다.
     * @param id 수정할 트윗 식별자
     * @param request 작성자 ID와 새 본문
     * @return 수정된 트윗
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않거나 작성자가 본인이 아닌 경우
     */
    @PatchMapping("/api/v1/tweets/{id}")
    public ResponseEntity<TweetResponse> updateTweet(@PathVariable Long id, @Valid @RequestBody TweetUpdateRequest request) {
        TweetResponse response =
                tweetMapper.toResponse(tweetService.updateTweet(id, request.getAuthorId(), request.getContent()));
        return ResponseEntity.ok(response);
    }

    /**
     * 트윗을 삭제한다. 작성자 본인만 삭제할 수 있다. 삭제/수정 이력은 별도로 남기지 않는다.
     * @param id 삭제할 트윗 식별자
     * @param authorId 삭제를 요청하는 사용자 식별자
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않거나 작성자가 본인이 아닌 경우
     */
    @DeleteMapping("/api/v1/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, @RequestParam Long authorId) {
        tweetService.deleteTweet(id, authorId);
        return ResponseEntity.noContent().build();
    }
}
