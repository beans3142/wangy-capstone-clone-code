package com.wangyu.service;

import java.util.List;

import com.wangyu.ApiRequestException;
import com.wangyu.entity.Follow;
import com.wangyu.repository.FollowRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new ApiRequestException("자기 자신을 팔로우할 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            return;
        }
        followRepository.save(new Follow(followerId, followingId));
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .ifPresent(followRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<Long> getFollowingIds(Long followerId) {
        return followRepository.findFollowingIdsByFollowerId(followerId);
    }
}
