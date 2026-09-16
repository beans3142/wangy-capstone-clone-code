import type { UserProfileResponse } from '../../../types/domain';

export interface ProfileState {
  profile: UserProfileResponse | null;
  isFollowing: boolean;
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
  followStatus: 'idle' | 'loading' | 'succeeded' | 'failed';
}

export const PROFILE_FETCH_REQUEST = 'profile/fetchRequest';
export const PROFILE_FETCH_SUCCESS = 'profile/fetchSuccess';
export const PROFILE_FETCH_FAILURE = 'profile/fetchFailure';
export const PROFILE_FOLLOW_REQUEST = 'profile/followRequest';
export const PROFILE_FOLLOW_SUCCESS = 'profile/followSuccess';
export const PROFILE_UNFOLLOW_REQUEST = 'profile/unfollowRequest';
export const PROFILE_UNFOLLOW_SUCCESS = 'profile/unfollowSuccess';
export const PROFILE_FOLLOW_FAILURE = 'profile/followFailure';

export interface FetchProfileRequestAction {
  type: typeof PROFILE_FETCH_REQUEST;
  payload: { targetUserId: number; viewerUserId: number };
}

export interface FetchProfileSuccessAction {
  type: typeof PROFILE_FETCH_SUCCESS;
  payload: { profile: UserProfileResponse; isFollowing: boolean };
}

export interface FetchProfileFailureAction {
  type: typeof PROFILE_FETCH_FAILURE;
  payload: { message: string };
}

export interface FollowRequestAction {
  type: typeof PROFILE_FOLLOW_REQUEST;
  payload: { targetUserId: number; viewerUserId: number };
}

export interface FollowSuccessAction {
  type: typeof PROFILE_FOLLOW_SUCCESS;
}

export interface UnfollowRequestAction {
  type: typeof PROFILE_UNFOLLOW_REQUEST;
  payload: { targetUserId: number; viewerUserId: number };
}

export interface UnfollowSuccessAction {
  type: typeof PROFILE_UNFOLLOW_SUCCESS;
}

export interface FollowFailureAction {
  type: typeof PROFILE_FOLLOW_FAILURE;
  payload: { message: string };
}

export type ProfileAction =
  | FetchProfileRequestAction
  | FetchProfileSuccessAction
  | FetchProfileFailureAction
  | FollowRequestAction
  | FollowSuccessAction
  | UnfollowRequestAction
  | UnfollowSuccessAction
  | FollowFailureAction;
