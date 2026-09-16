import { apiClient } from './client';

/**
 * 좋아요 토글 API 계약.
 *
 * PRD 지시대로 tweet-service의 Phase 6("tweet-interaction") 코드를 직접 확인했지만,
 * feature/phase06-tweet-interaction 브랜치에는 아직 Like 엔티티/컨트롤러가 전혀 없다
 * (git grep으로 "like" 관련 Java 파일 0건 확인, worklog 참고). 즉 좋아요 백엔드는
 * 브랜치 이름과 달리 아직 구현되지 않은 상태다.
 *
 * PRD 지침("확정 전이면 커스텀 훅으로 분리해 나중에 경로만 바꾸면 되게")에 따라
 * 엔드포인트 경로를 이 파일 하나에만 고립시켰다. 백엔드가 완성되면 아래 두 함수의
 * URL과 응답 타입만 바꾸면 되고, 이를 사용하는 useTweetCard/saga는 손댈 필요가 없다.
 *
 * 백엔드 부재 상태에서 화면이 죽지 않도록, 실제 호출은 시도하되(엔드포인트가 생기면
 * 바로 동작) 실패 시 상위(saga)에서 안내와 함께 흡수한다.
 */
export interface LikeToggleResponse {
  liked: boolean;
  likeCount: number;
}

export function toggleLike(tweetId: number) {
  return apiClient.post<LikeToggleResponse>(`/api/v1/tweets/${tweetId}/likes/toggle`);
}
