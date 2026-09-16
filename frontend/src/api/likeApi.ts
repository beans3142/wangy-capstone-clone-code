import { apiClient } from './client';

/**
 * 좋아요 토글 API 계약.
 *
 * 작업 시작 시점에는 feature/phase06-tweet-interaction 브랜치에 Like 관련 코드가
 * 전혀 없었다(git grep 0건). 이후 작업 도중 다른 세션이 해당 브랜치에 실제로 좋아요
 * 기능을 완성해 push했고(커밋 0c0de4a), 재확인해보니 TweetLike 엔티티/LikeService/
 * LikeToggleResponse{liked, likeCount}까지는 존재한다. 다만 이를 물리는
 * @RestController 엔드포인트는 TweetController에 아직 추가되지 않아 REST 경로는
 * 여전히 미확정이다 (worklog 참고).
 *
 * PRD 지침("확정 전이면 커스텀 훅으로 분리해 나중에 경로만 바꾸면 되게")에 따라
 * 엔드포인트 경로를 이 파일 하나에만 고립시켰다. 백엔드에 컨트롤러가 추가되면 아래
 * 함수의 URL만 바꾸면 되고(응답 타입 LikeToggleResponse는 이미 실제 DTO와 일치),
 * 이를 사용하는 useTweetCard/saga는 손댈 필요가 없다.
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
