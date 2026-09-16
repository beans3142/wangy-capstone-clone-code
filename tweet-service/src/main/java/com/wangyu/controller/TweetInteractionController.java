package com.wangyu.controller;

import java.util.List;

import com.wangyu.HeaderResponse;
import com.wangyu.dto.BookmarkToggleResponse;
import com.wangyu.dto.LikeToggleResponse;
import com.wangyu.dto.RetweetToggleResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.dto.TweetResponse;
import com.wangyu.mapper.TweetMapper;
import com.wangyu.service.BookmarkService;
import com.wangyu.service.LikeService;
import com.wangyu.service.RetweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 좋아요/리트윗/북마크는 architecture.md 4.1의 완성된 게이트웨이 인증 흐름을 사용하는 첫 엔드포인트라
 * 사용자 식별자를 body/path가 아닌 X-Auth-User-Id 헤더에서 추출한다.
 */
@RestController
public class TweetInteractionController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String AUTH_USER_ID_HEADER = "X-Auth-User-Id";

    private final LikeService likeService;
    private final RetweetService retweetService;
    private final BookmarkService bookmarkService;
    private final TweetMapper tweetMapper;

    public TweetInteractionController(LikeService likeService,
                                       RetweetService retweetService,
                                       BookmarkService bookmarkService,
                                       TweetMapper tweetMapper) {
        this.likeService = likeService;
        this.retweetService = retweetService;
        this.bookmarkService = bookmarkService;
        this.tweetMapper = tweetMapper;
    }

    /**
     * 트윗 좋아요를 토글한다. 이미 좋아요한 상태라면 취소된다.
     * @param id 대상 트윗 식별자
     * @param userId 게이트웨이가 주입한 호출자 식별자
     * @return 토글 후 좋아요 여부와 최신 좋아요 수
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않는 경우
     */
    @PostMapping("/api/v1/tweets/{id}/like")
    public ResponseEntity<LikeToggleResponse> toggleLike(@PathVariable Long id,
                                                          @RequestHeader(AUTH_USER_ID_HEADER) Long userId) {
        return ResponseEntity.ok(likeService.toggleLike(id, userId));
    }

    /**
     * 트윗 리트윗을 토글한다. 이미 리트윗한 상태라면 취소된다.
     * @param id 대상 트윗 식별자
     * @param userId 게이트웨이가 주입한 호출자 식별자
     * @return 토글 후 리트윗 여부와 최신 리트윗 수
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않는 경우
     */
    @PostMapping("/api/v1/tweets/{id}/retweet")
    public ResponseEntity<RetweetToggleResponse> toggleRetweet(@PathVariable Long id,
                                                                @RequestHeader(AUTH_USER_ID_HEADER) Long userId) {
        return ResponseEntity.ok(retweetService.toggleRetweet(id, userId));
    }

    /**
     * 트윗 북마크를 토글한다. 이미 북마크한 상태라면 취소된다.
     * @param id 대상 트윗 식별자
     * @param userId 게이트웨이가 주입한 호출자 식별자
     * @return 토글 후 북마크 여부
     * @throws com.wangyu.ApiRequestException 트윗이 존재하지 않는 경우
     */
    @PostMapping("/api/v1/tweets/{id}/bookmark")
    public ResponseEntity<BookmarkToggleResponse> toggleBookmark(@PathVariable Long id,
                                                                  @RequestHeader(AUTH_USER_ID_HEADER) Long userId) {
        return ResponseEntity.ok(bookmarkService.toggleBookmark(id, userId));
    }

    /**
     * 호출자 본인의 북마크 목록을 최신순으로 페이징 조회한다. 다른 사용자의 북마크는 볼 수 없다.
     * @param userId 게이트웨이가 주입한 호출자 식별자
     * @param page 0부터 시작하는 페이지 번호
     * @param size 페이지 크기
     * @return 헤더로 페이징 메타를 담은 북마크된 트윗 목록
     */
    @GetMapping("/api/v1/users/me/bookmarks")
    public ResponseEntity<List<TweetResponse>> getMyBookmarks(@RequestHeader(AUTH_USER_ID_HEADER) Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        TimelineResult result = bookmarkService.getMyBookmarks(userId, page, size);
        List<TweetResponse> content = tweetMapper.toResponseList(result.getContent());
        return HeaderResponse.of(content, result.getTotalCount(), result.isHasNext());
    }
}
