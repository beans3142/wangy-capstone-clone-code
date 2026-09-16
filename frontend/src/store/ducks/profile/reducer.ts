import {
  PROFILE_FETCH_FAILURE,
  PROFILE_FETCH_REQUEST,
  PROFILE_FETCH_SUCCESS,
  PROFILE_FOLLOW_FAILURE,
  PROFILE_FOLLOW_REQUEST,
  PROFILE_FOLLOW_SUCCESS,
  PROFILE_UNFOLLOW_REQUEST,
  PROFILE_UNFOLLOW_SUCCESS,
  type ProfileAction,
  type ProfileState,
} from './types';

const initialState: ProfileState = {
  profile: null,
  isFollowing: false,
  status: 'idle',
  error: null,
  followStatus: 'idle',
};

export function profileReducer(state: ProfileState = initialState, action: ProfileAction): ProfileState {
  switch (action.type) {
    case PROFILE_FETCH_REQUEST:
      return { ...state, status: 'loading', error: null };
    case PROFILE_FETCH_SUCCESS:
      return { ...state, status: 'succeeded', profile: action.payload.profile, isFollowing: action.payload.isFollowing };
    case PROFILE_FETCH_FAILURE:
      return { ...state, status: 'failed', error: action.payload.message };
    case PROFILE_FOLLOW_REQUEST:
    case PROFILE_UNFOLLOW_REQUEST:
      return { ...state, followStatus: 'loading' };
    case PROFILE_FOLLOW_SUCCESS:
      return { ...state, followStatus: 'succeeded', isFollowing: true };
    case PROFILE_UNFOLLOW_SUCCESS:
      return { ...state, followStatus: 'succeeded', isFollowing: false };
    case PROFILE_FOLLOW_FAILURE:
      return { ...state, followStatus: 'failed' };
    default:
      return state;
  }
}
