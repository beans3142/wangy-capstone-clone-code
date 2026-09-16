import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../store/rootReducer';
import { toggleLikeRequest } from '../../store/ducks/feed/actions';
import type { TweetResponse } from '../../types/domain';

export function useTweetCard(tweet: TweetResponse) {
  const dispatch = useDispatch();
  const liked = useSelector((state: RootState) => state.feed.likedTweetIds.includes(tweet.id));
  const likeCount = useSelector((state: RootState) => state.feed.likeCounts[tweet.id] ?? 0);

  const toggleLike = () => {
    dispatch(toggleLikeRequest(tweet.id));
  };

  return { liked, likeCount, toggleLike };
}
