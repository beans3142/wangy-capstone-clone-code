import { call, put, takeLatest } from 'redux-saga/effects';
import type { AxiosResponse } from 'axios';
import * as userApi from '../../../api/userApi';
import type { UserProfileResponse } from '../../../types/domain';
import {
  PROFILE_FETCH_REQUEST,
  PROFILE_FOLLOW_REQUEST,
  PROFILE_UNFOLLOW_REQUEST,
  type FetchProfileRequestAction,
  type FollowRequestAction,
  type UnfollowRequestAction,
} from './types';
import {
  fetchProfileFailure,
  fetchProfileSuccess,
  followFailure,
  followSuccess,
  unfollowSuccess,
} from './actions';

function extractErrorMessage(error: unknown): string {
  const withResponse = error as { response?: { data?: { message?: string } } };
  return withResponse.response?.data?.message ?? '요청 처리 중 오류가 발생했습니다.';
}

function* handleFetchProfile(action: FetchProfileRequestAction) {
  const { targetUserId, viewerUserId } = action.payload;
  try {
    const profileResponse: AxiosResponse<UserProfileResponse> = yield call(userApi.getPublicProfile, targetUserId);
    // UserProfileResponse에는 isFollowing 필드가 없어(코드 확인 완료), 뷰어가 팔로우하는
    // 사람들의 ID 목록을 따로 조회해 프로필 대상이 포함되는지로 팔로우 여부를 판단한다.
    const followingIdsResponse: AxiosResponse<number[]> = yield call(userApi.getFollowingIds, viewerUserId);
    const isFollowing = followingIdsResponse.data.includes(targetUserId);
    yield put(fetchProfileSuccess(profileResponse.data, isFollowing));
  } catch (error) {
    yield put(fetchProfileFailure(extractErrorMessage(error)));
  }
}

function* handleFollow(action: FollowRequestAction) {
  const { targetUserId, viewerUserId } = action.payload;
  try {
    yield call(userApi.follow, targetUserId, viewerUserId);
    yield put(followSuccess());
  } catch (error) {
    yield put(followFailure(extractErrorMessage(error)));
  }
}

function* handleUnfollow(action: UnfollowRequestAction) {
  const { targetUserId, viewerUserId } = action.payload;
  try {
    yield call(userApi.unfollow, targetUserId, viewerUserId);
    yield put(unfollowSuccess());
  } catch (error) {
    yield put(followFailure(extractErrorMessage(error)));
  }
}

export function* profileSaga() {
  yield takeLatest(PROFILE_FETCH_REQUEST, handleFetchProfile);
  yield takeLatest(PROFILE_FOLLOW_REQUEST, handleFollow);
  yield takeLatest(PROFILE_UNFOLLOW_REQUEST, handleUnfollow);
}
