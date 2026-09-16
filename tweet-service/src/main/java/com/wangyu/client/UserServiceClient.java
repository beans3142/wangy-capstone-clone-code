package com.wangyu.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 타임라인 조립을 위해 user-service(Phase 3b)의 팔로우 조회 엔드포인트를 호출한다.
 * Eureka 서비스 디스커버리로 user-service를 찾으므로 URL을 하드코딩하지 않는다.
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}/following-ids")
    List<Long> getFollowingIds(@PathVariable("id") Long id);
}
