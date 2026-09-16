import { apiClient } from './client';
import type { TimelinePage, TweetCreateRequest, TweetResponse } from '../types/domain';

/**
 * tweet-service TweetController와 1:1 대응.
 * 주의: PRD 초안은 타임라인 경로를 `GET /api/v1/tweets/timeline`이라 적었지만,
 * 실제 TweetController 코드를 읽어보면 `GET /api/v1/users/{userId}/timeline`이다.
 * 추측 대신 코드를 확인해 PRD의 오기를 바로잡았다 (worklog 참고).
 *
 * 페이징 메타(X-Total-Count, X-Has-Next)는 응답 헤더로만 내려오고 body는 순수 배열이다.
 */
export function getTimeline(userId: number, page: number, size: number) {
  return apiClient
    .get<TweetResponse[]>(`/api/v1/users/${userId}/timeline`, { params: { page, size } })
    .then((response): TimelinePage => {
      const totalCountHeader = response.headers['x-total-count'];
      const hasNextHeader = response.headers['x-has-next'];
      return {
        items: response.data,
        // api-gateway globalcors 설정에 exposedHeaders가 없어 브라우저가 커스텀 헤더를
        // 못 읽는 경우가 있다. 그 경우를 대비해 반환된 개수로 hasNext를 보수적으로 추정한다.
        totalCount: totalCountHeader ? Number(totalCountHeader) : response.data.length,
        hasNext: hasNextHeader ? hasNextHeader === 'true' : response.data.length === size,
      };
    });
}

export function getTweet(tweetId: number) {
  return apiClient.get<TweetResponse>(`/api/v1/tweets/${tweetId}`);
}

export function createTweet(request: TweetCreateRequest) {
  return apiClient.post<TweetResponse>('/api/v1/tweets', request);
}
