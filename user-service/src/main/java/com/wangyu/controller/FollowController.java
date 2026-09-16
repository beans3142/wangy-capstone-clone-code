package com.wangyu.controller;

import java.util.List;

import com.wangyu.dto.FollowRequest;
import com.wangyu.service.FollowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * followerId가 followingId를 팔로우한다. 이미 팔로우 중이면 중복 생성 없이 그대로 성공 처리한다.
     * @param followingId 팔로우 대상 사용자 식별자
     * @param request 요청자(팔로워)의 식별자를 담은 바디
     * @throws com.wangyu.ApiRequestException followerId와 followingId가 같은 경우
     */
    @PostMapping("/{followingId}/follow")
    public ResponseEntity<Void> follow(@PathVariable Long followingId, @Valid @RequestBody FollowRequest request) {
        followService.follow(request.getFollowerId(), followingId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * followerId가 followingId에 대한 팔로우를 취소한다. 팔로우 관계가 없어도 에러 없이 종료한다.
     * @param followingId 팔로우 취소 대상 사용자 식별자
     * @param followerId 요청자(팔로워)의 식별자
     */
    @DeleteMapping("/{followingId}/follow/{followerId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long followingId, @PathVariable Long followerId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    /**
     * tweet-service가 타임라인 조립 시 호출하는 내부 계약 엔드포인트다(architecture.md 4.1 헤더 주입 전까지는 인증 없이 공개).
     * @param id 팔로우하는 사람들의 ID 목록을 조회할 사용자 식별자
     * @return id가 팔로우하는 사용자들의 ID 목록 (없으면 빈 배열)
     */
    @GetMapping("/{id}/following-ids")
    public ResponseEntity<List<Long>> getFollowingIds(@PathVariable Long id) {
        return ResponseEntity.ok(followService.getFollowingIds(id));
    }
}
