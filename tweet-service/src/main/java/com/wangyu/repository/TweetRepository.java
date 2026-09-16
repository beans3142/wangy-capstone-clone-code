package com.wangyu.repository;

import java.util.List;
import java.util.Optional;

import com.wangyu.entity.Tweet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    <T> Optional<T> getTweetById(Long id, Class<T> type);

    <T> List<T> findByAuthorIdInOrderByCreatedAtDesc(List<Long> authorIds, Pageable pageable, Class<T> type);

    <T> List<T> findByIdIn(List<Long> ids, Class<T> type);

    long countByAuthorIdIn(List<Long> authorIds);

    /**
     * 좋아요 수는 매 조회마다 tweet_likes를 COUNT하지 않고 tweets.like_count 컬럼에 비정규화해 즉시 응답한다.
     * 동시에 여러 사용자가 좋아요를 눌러도 정확히 반영되도록 SELECT-후-UPDATE가 아닌 단일 원자적 UPDATE로 증감시킨다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tweet t SET t.likeCount = t.likeCount + 1 WHERE t.id = :id")
    int incrementLikeCount(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tweet t SET t.likeCount = t.likeCount - 1 WHERE t.id = :id AND t.likeCount > 0")
    int decrementLikeCount(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tweet t SET t.retweetCount = t.retweetCount + 1 WHERE t.id = :id")
    int incrementRetweetCount(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tweet t SET t.retweetCount = t.retweetCount - 1 WHERE t.id = :id AND t.retweetCount > 0")
    int decrementRetweetCount(@Param("id") Long id);
}
