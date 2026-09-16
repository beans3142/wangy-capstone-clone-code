import { call, put, takeEvery, takeLatest } from 'redux-saga/effects';
import type { AxiosResponse } from 'axios';
import * as tweetApi from '../../../api/tweetApi';
import * as likeApi from '../../../api/likeApi';
import type { TimelinePage, TweetResponse } from '../../../types/domain';
import {
  FEED_CREATE_TWEET_REQUEST,
  FEED_FETCH_TIMELINE_REQUEST,
  FEED_TOGGLE_LIKE_REQUEST,
  type CreateTweetRequestAction,
  type FetchTimelineRequestAction,
  type ToggleLikeRequestAction,
} from './types';
import {
  createTweetFailure,
  createTweetSuccess,
  fetchTimelineFailure,
  fetchTimelineSuccess,
  toggleLikeFailure,
  toggleLikeSuccess,
} from './actions';

function extractErrorMessage(error: unknown): string {
  const withResponse = error as { response?: { data?: { message?: string } } };
  return withResponse.response?.data?.message ?? '요청 처리 중 오류가 발생했습니다.';
}

function* handleFetchTimeline(action: FetchTimelineRequestAction) {
  const { userId, page, size, append } = action.payload;
  try {
    const result: TimelinePage = yield call(tweetApi.getTimeline, userId, page, size);
    yield put(fetchTimelineSuccess(result.items, result.totalCount, result.hasNext, page, append));
  } catch (error) {
    yield put(fetchTimelineFailure(extractErrorMessage(error)));
  }
}

function* handleCreateTweet(action: CreateTweetRequestAction) {
  const { authorId, content, imageUrl } = action.payload;
  try {
    const response: AxiosResponse<TweetResponse> = yield call(tweetApi.createTweet, {
      authorId,
      content,
      imageUrl,
    });
    yield put(createTweetSuccess(response.data));
  } catch (error) {
    yield put(createTweetFailure(extractErrorMessage(error)));
  }
}

function* handleToggleLike(action: ToggleLikeRequestAction) {
  const { tweetId } = action.payload;
  try {
    const response: AxiosResponse<likeApi.LikeToggleResponse> = yield call(likeApi.toggleLike, tweetId);
    yield put(toggleLikeSuccess(tweetId, response.data.liked, response.data.likeCount));
  } catch (error) {
    // 좋아요 백엔드가 아직 없는 상태이므로(likeApi.ts 참고) 실패는 조용히 흡수하고
    // 리듀서가 처리한 낙관적 토글 결과를 그대로 유지한다.
    yield put(toggleLikeFailure(tweetId, extractErrorMessage(error)));
  }
}

export function* feedSaga() {
  yield takeLatest(FEED_FETCH_TIMELINE_REQUEST, handleFetchTimeline);
  yield takeLatest(FEED_CREATE_TWEET_REQUEST, handleCreateTweet);
  // 트윗마다 독립적인 토글이므로 takeLatest로 다른 트윗의 요청을 취소시키면 안 된다.
  yield takeEvery(FEED_TOGGLE_LIKE_REQUEST, handleToggleLike);
}
