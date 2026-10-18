package com.wangyu.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.wangyu.ApiRequestException;
import com.wangyu.dto.BookmarkToggleResponse;
import com.wangyu.dto.TimelineResult;
import com.wangyu.entity.Bookmark;
import com.wangyu.repository.BookmarkRepository;
import com.wangyu.repository.TweetProjection;
import com.wangyu.repository.TweetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    private final TweetRepository tweetRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(TweetRepository tweetRepository, BookmarkRepository bookmarkRepository) {
        this.tweetRepository = tweetRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional
    public BookmarkToggleResponse toggleBookmark(Long tweetId, Long userId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new ApiRequestException("존재하지 않는 트윗입니다", HttpStatus.NOT_FOUND);
        }

        boolean bookmarked = bookmarkRepository.findByTweetIdAndUserId(tweetId, userId)
                .map(existing -> {
                    bookmarkRepository.delete(existing);
                    return false;
                })
                .orElseGet(() -> {
                    bookmarkRepository.save(new Bookmark(tweetId, userId));
                    return true;
                });

        return new BookmarkToggleResponse(bookmarked);
    }

    /**
     * 호출자 자신의 북마크만 조회한다(userId는 컨트롤러가 X-Auth-User-Id 헤더에서 추출해 넘겨준다).
     * 북마크한 트윗이 그 사이 삭제됐을 수 있으므로(원본 작성자가 삭제) 조회 시점에 존재하는 트윗만 반환한다.
     */
    @Transactional(readOnly = true)
    public TimelineResult getMyBookmarks(Long userId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ApiRequestException("page는 0 이상, size는 1 이상이어야 합니다", HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page, size);
        List<Bookmark> bookmarks = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        long totalCount = bookmarkRepository.countByUserId(userId);
        boolean hasNext = (long) (page + 1) * size < totalCount;

        List<Long> tweetIds = bookmarks.stream().map(Bookmark::getTweetId).collect(Collectors.toList());
        Map<Long, TweetProjection> tweetsById = tweetRepository.findByIdIn(tweetIds, TweetProjection.class).stream()
                .collect(Collectors.toMap(TweetProjection::getId, projection -> projection, (a, b) -> a, LinkedHashMap::new));

        List<TweetProjection> content = bookmarks.stream()
                .map(bookmark -> tweetsById.get(bookmark.getTweetId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new TimelineResult(content, totalCount, hasNext);
    }
}
