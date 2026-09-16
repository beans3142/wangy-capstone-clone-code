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
  type FeedAction,
  type FeedState,
} from './types';

const initialState: FeedState = {
  items: [],
  likedTweetIds: [],
  likeCounts: {},
  page: 0,
  size: 20,
  hasNext: false,
  totalCount: 0,
  timelineStatus: 'idle',
  timelineError: null,
  createStatus: 'idle',
  createError: null,
};

export function feedReducer(state: FeedState = initialState, action: FeedAction): FeedState {
  switch (action.type) {
    case FEED_FETCH_TIMELINE_REQUEST:
      return { ...state, timelineStatus: 'loading', timelineError: null };
    case FEED_FETCH_TIMELINE_SUCCESS:
      return {
        ...state,
        timelineStatus: 'succeeded',
        items: action.payload.append ? [...state.items, ...action.payload.items] : action.payload.items,
        totalCount: action.payload.totalCount,
        hasNext: action.payload.hasNext,
        page: action.payload.page,
      };
    case FEED_FETCH_TIMELINE_FAILURE:
      return { ...state, timelineStatus: 'failed', timelineError: action.payload.message };
    case FEED_CREATE_TWEET_REQUEST:
      return { ...state, createStatus: 'loading', createError: null };
    case FEED_CREATE_TWEET_SUCCESS:
      return {
        ...state,
        createStatus: 'succeeded',
        items: [action.payload.tweet, ...state.items],
        totalCount: state.totalCount + 1,
      };
    case FEED_CREATE_TWEET_FAILURE:
      return { ...state, createStatus: 'failed', createError: action.payload.message };
    case FEED_TOGGLE_LIKE_REQUEST: {
      // 낙관적 업데이트: 좋아요 백엔드가 아직 없어(worklog 참고) 응답을 기다리지 않고
      // 즉시 토글해 MVP 수용 기준("누르면 좋아요 수가 바뀐다")을 만족시킨다.
      const tweetId = action.payload.tweetId;
      const alreadyLiked = state.likedTweetIds.includes(tweetId);
      const currentCount = state.likeCounts[tweetId] ?? 0;
      return {
        ...state,
        likedTweetIds: alreadyLiked
          ? state.likedTweetIds.filter((id) => id !== tweetId)
          : [...state.likedTweetIds, tweetId],
        likeCounts: { ...state.likeCounts, [tweetId]: alreadyLiked ? Math.max(0, currentCount - 1) : currentCount + 1 },
      };
    }
    case FEED_TOGGLE_LIKE_SUCCESS: {
      const { tweetId, liked, likeCount } = action.payload;
      const likedTweetIds = liked
        ? Array.from(new Set([...state.likedTweetIds, tweetId]))
        : state.likedTweetIds.filter((id) => id !== tweetId);
      return {
        ...state,
        likedTweetIds,
        likeCounts: { ...state.likeCounts, [tweetId]: likeCount },
      };
    }
    case FEED_TOGGLE_LIKE_FAILURE:
      // 서버 확정 응답이 없을 뿐, 사용자 경험상 낙관적 토글 결과는 유지한다.
      return state;
    default:
      return state;
  }
}
