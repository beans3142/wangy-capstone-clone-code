import type { TweetResponse } from '../../../types/domain';

export interface FeedState {
  items: TweetResponse[];
  likedTweetIds: number[];
  likeCounts: Record<number, number>;
  page: number;
  size: number;
  hasNext: boolean;
  totalCount: number;
  timelineStatus: 'idle' | 'loading' | 'succeeded' | 'failed';
  timelineError: string | null;
  createStatus: 'idle' | 'loading' | 'succeeded' | 'failed';
  createError: string | null;
}

export const FEED_FETCH_TIMELINE_REQUEST = 'feed/fetchTimelineRequest';
export const FEED_FETCH_TIMELINE_SUCCESS = 'feed/fetchTimelineSuccess';
export const FEED_FETCH_TIMELINE_FAILURE = 'feed/fetchTimelineFailure';
export const FEED_CREATE_TWEET_REQUEST = 'feed/createTweetRequest';
export const FEED_CREATE_TWEET_SUCCESS = 'feed/createTweetSuccess';
export const FEED_CREATE_TWEET_FAILURE = 'feed/createTweetFailure';
export const FEED_TOGGLE_LIKE_REQUEST = 'feed/toggleLikeRequest';
export const FEED_TOGGLE_LIKE_SUCCESS = 'feed/toggleLikeSuccess';
export const FEED_TOGGLE_LIKE_FAILURE = 'feed/toggleLikeFailure';

export interface FetchTimelineRequestAction {
  type: typeof FEED_FETCH_TIMELINE_REQUEST;
  payload: { userId: number; page: number; size: number; append: boolean };
}

export interface FetchTimelineSuccessAction {
  type: typeof FEED_FETCH_TIMELINE_SUCCESS;
  payload: { items: TweetResponse[]; totalCount: number; hasNext: boolean; page: number; append: boolean };
}

export interface FetchTimelineFailureAction {
  type: typeof FEED_FETCH_TIMELINE_FAILURE;
  payload: { message: string };
}

export interface CreateTweetRequestAction {
  type: typeof FEED_CREATE_TWEET_REQUEST;
  payload: { authorId: number; content: string; imageUrl: string | null };
}

export interface CreateTweetSuccessAction {
  type: typeof FEED_CREATE_TWEET_SUCCESS;
  payload: { tweet: TweetResponse };
}

export interface CreateTweetFailureAction {
  type: typeof FEED_CREATE_TWEET_FAILURE;
  payload: { message: string };
}

export interface ToggleLikeRequestAction {
  type: typeof FEED_TOGGLE_LIKE_REQUEST;
  payload: { tweetId: number };
}

export interface ToggleLikeSuccessAction {
  type: typeof FEED_TOGGLE_LIKE_SUCCESS;
  payload: { tweetId: number; liked: boolean; likeCount: number };
}

export interface ToggleLikeFailureAction {
  type: typeof FEED_TOGGLE_LIKE_FAILURE;
  payload: { tweetId: number; message: string };
}

export type FeedAction =
  | FetchTimelineRequestAction
  | FetchTimelineSuccessAction
  | FetchTimelineFailureAction
  | CreateTweetRequestAction
  | CreateTweetSuccessAction
  | CreateTweetFailureAction
  | ToggleLikeRequestAction
  | ToggleLikeSuccessAction
  | ToggleLikeFailureAction;
