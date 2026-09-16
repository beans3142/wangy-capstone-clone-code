import type { UserProfileResponse } from '../../../types/domain';
import {
  PROFILE_FETCH_FAILURE,
  PROFILE_FETCH_REQUEST,
  PROFILE_FETCH_SUCCESS,
  PROFILE_FOLLOW_FAILURE,
  PROFILE_FOLLOW_REQUEST,
  PROFILE_FOLLOW_SUCCESS,
  PROFILE_UNFOLLOW_REQUEST,
  PROFILE_UNFOLLOW_SUCCESS,
  type FetchProfileFailureAction,
  type FetchProfileRequestAction,
  type FetchProfileSuccessAction,
  type FollowFailureAction,
  type FollowRequestAction,
  type FollowSuccessAction,
  type UnfollowRequestAction,
  type UnfollowSuccessAction,
} from './types';

export const fetchProfileRequest = (targetUserId: number, viewerUserId: number): FetchProfileRequestAction => ({
  type: PROFILE_FETCH_REQUEST,
  payload: { targetUserId, viewerUserId },
});

export const fetchProfileSuccess = (
  profile: UserProfileResponse,
  isFollowing: boolean,
): FetchProfileSuccessAction => ({
  type: PROFILE_FETCH_SUCCESS,
  payload: { profile, isFollowing },
});

export const fetchProfileFailure = (message: string): FetchProfileFailureAction => ({
  type: PROFILE_FETCH_FAILURE,
  payload: { message },
});

export const followRequest = (targetUserId: number, viewerUserId: number): FollowRequestAction => ({
  type: PROFILE_FOLLOW_REQUEST,
  payload: { targetUserId, viewerUserId },
});

export const followSuccess = (): FollowSuccessAction => ({ type: PROFILE_FOLLOW_SUCCESS });

export const unfollowRequest = (targetUserId: number, viewerUserId: number): UnfollowRequestAction => ({
  type: PROFILE_UNFOLLOW_REQUEST,
  payload: { targetUserId, viewerUserId },
});

export const unfollowSuccess = (): UnfollowSuccessAction => ({ type: PROFILE_UNFOLLOW_SUCCESS });

export const followFailure = (message: string): FollowFailureAction => ({
  type: PROFILE_FOLLOW_FAILURE,
  payload: { message },
});
