import { apiClient } from './client';
import type { UserProfileResponse } from '../types/domain';

/**
 * user-service UserController/FollowController와 1:1 대응.
 * 팔로우/언팔로우/팔로잉목록 경로는 실제 FollowController 코드를 읽고 확정했다:
 *  - POST   /api/v1/users/{followingId}/follow            body: { followerId }
 *  - DELETE /api/v1/users/{followingId}/follow/{followerId}
 *  - GET    /api/v1/users/{id}/following-ids               -> number[]
 * PRD가 "정확한 경로를 직접 확인할 것"이라 명시한 부분이라, 추측하지 않고 코드에서 그대로 가져왔다.
 */
export function getPublicProfile(userId: number) {
  return apiClient.get<UserProfileResponse>(`/api/v1/users/${userId}`);
}

export function follow(followingId: number, followerId: number) {
  return apiClient.post<void>(`/api/v1/users/${followingId}/follow`, { followerId });
}

export function unfollow(followingId: number, followerId: number) {
  return apiClient.delete<void>(`/api/v1/users/${followingId}/follow/${followerId}`);
}

export function getFollowingIds(userId: number) {
  return apiClient.get<number[]>(`/api/v1/users/${userId}/following-ids`);
}
