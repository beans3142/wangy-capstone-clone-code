package com.wangyu.dto;

import jakarta.validation.constraints.NotNull;

public class FollowRequest {

    @NotNull(message = "팔로워 ID는 필수입니다")
    private Long followerId;

    public FollowRequest() {
    }

    public FollowRequest(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
}
