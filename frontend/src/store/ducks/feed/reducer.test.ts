import { describe, expect, it } from 'vitest';
import { feedReducer } from './reducer';
import { toggleLikeRequest, toggleLikeSuccess, fetchTimelineSuccess, createTweetSuccess } from './actions';
import type { TweetResponse } from '../../../types/domain';

const sampleTweet: TweetResponse = {
  id: 1,
  authorId: 10,
  content: 'hello',
  imageUrl: null,
  createdAt: '2026-01-01T00:00:00',
  updatedAt: '2026-01-01T00:00:00',
  likeCount: 0,
  retweetCount: 0,
};

describe('feedReducer', () => {
  it('낙관적으로 좋아요를 토글한다 (좋아요 백엔드 부재 시에도 즉시 반영)', () => {
    const state = feedReducer(undefined, toggleLikeRequest(1));

    expect(state.likedTweetIds).toContain(1);
    expect(state.likeCounts[1]).toBe(1);

    const toggledBack = feedReducer(state, toggleLikeRequest(1));
    expect(toggledBack.likedTweetIds).not.toContain(1);
    expect(toggledBack.likeCounts[1]).toBe(0);
  });

  it('서버 응답이 오면 서버 값으로 좋아요 상태를 덮어쓴다', () => {
    const optimistic = feedReducer(undefined, toggleLikeRequest(1));
    const confirmed = feedReducer(optimistic, toggleLikeSuccess(1, true, 42));

    expect(confirmed.likedTweetIds).toEqual([1]);
    expect(confirmed.likeCounts[1]).toBe(42);
  });

  it('타임라인 첫 페이지 조회는 목록을 교체하고, append 조회는 뒤에 이어붙인다', () => {
    const firstPage = feedReducer(undefined, fetchTimelineSuccess([sampleTweet], 1, false, 0, false));
    expect(firstPage.items).toHaveLength(1);

    const secondTweet = { ...sampleTweet, id: 2 };
    const secondPage = feedReducer(firstPage, fetchTimelineSuccess([secondTweet], 2, false, 1, true));
    expect(secondPage.items.map((tweet) => tweet.id)).toEqual([1, 2]);
  });

  it('트윗 작성 성공 시 새 트윗을 목록 맨 앞에 추가한다', () => {
    const state = feedReducer(undefined, createTweetSuccess(sampleTweet));
    expect(state.items[0]).toEqual(sampleTweet);
  });
});
