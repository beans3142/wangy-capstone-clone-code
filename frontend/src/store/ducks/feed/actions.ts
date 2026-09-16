import type { TweetResponse } from '../../../types/domain';
import {
  FEED_CREATE_TWEET_FAILURE,
  FEED_CREATE_TWEET_REQUEST,
  FEED_CREATE_TWEET_SUCCESS,
  FEED_FETCH_TIMELINE_FAILURE,
  FEED_FETCH_TIMELINE_REQUEST,
  FEED_FETCH_TIMELINE_SUCCESS,
  FEED_TOGGLE_LIKE_FAILURE,
  FEED_TOGGLE_LIKE_REQUEST,
  FEED_TOGGLE_LIKE_SUCCESS,
  type CreateTweetFailureAction,
  type CreateTweetRequestAction,
  type CreateTweetSuccessAction,
  type FetchTimelineFailureAction,
  type FetchTimelineRequestAction,
  type FetchTimelineSuccessAction,
  type ToggleLikeFailureAction,
  type ToggleLikeRequestAction,
  type ToggleLikeSuccessAction,
} from './types';

export const fetchTimelineRequest = (
  userId: number,
  page: number,
  size: number,
  append: boolean,
): FetchTimelineRequestAction => ({
  type: FEED_FETCH_TIMELINE_REQUEST,
  payload: { userId, page, size, append },
});

export const fetchTimelineSuccess = (
  items: TweetResponse[],
  totalCount: number,
  hasNext: boolean,
  page: number,
  append: boolean,
): FetchTimelineSuccessAction => ({
  type: FEED_FETCH_TIMELINE_SUCCESS,
  payload: { items, totalCount, hasNext, page, append },
});

export const fetchTimelineFailure = (message: string): FetchTimelineFailureAction => ({
  type: FEED_FETCH_TIMELINE_FAILURE,
  payload: { message },
});

export const createTweetRequest = (
  authorId: number,
  content: string,
  imageUrl: string | null,
): CreateTweetRequestAction => ({
  type: FEED_CREATE_TWEET_REQUEST,
  payload: { authorId, content, imageUrl },
});

export const createTweetSuccess = (tweet: TweetResponse): CreateTweetSuccessAction => ({
  type: FEED_CREATE_TWEET_SUCCESS,
  payload: { tweet },
});

export const createTweetFailure = (message: string): CreateTweetFailureAction => ({
  type: FEED_CREATE_TWEET_FAILURE,
  payload: { message },
});

export const toggleLikeRequest = (tweetId: number): ToggleLikeRequestAction => ({
  type: FEED_TOGGLE_LIKE_REQUEST,
  payload: { tweetId },
});

export const toggleLikeSuccess = (tweetId: number, liked: boolean, likeCount: number): ToggleLikeSuccessAction => ({
  type: FEED_TOGGLE_LIKE_SUCCESS,
  payload: { tweetId, liked, likeCount },
});

export const toggleLikeFailure = (tweetId: number, message: string): ToggleLikeFailureAction => ({
  type: FEED_TOGGLE_LIKE_FAILURE,
  payload: { tweetId, message },
});
