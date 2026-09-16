import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../store/rootReducer';
import { toggleLikeRequest } from '../../store/ducks/feed/actions';
import type { TweetResponse } from '../../types/domain';

export function useTweetCard(tweet: TweetResponse) {
  const dispatch = useDispatch();
  const liked = useSelector((state: RootState) => state.feed.likedTweetIds.includes(tweet.id));
  // 서버가 내려준 초기 likeCount를 기본값으로 쓰고, 사용자가 토글하면 로컬 상태로 덮어쓴다.
  const likeCount = useSelector((state: RootState) => state.feed.likeCounts[tweet.id] ?? tweet.likeCount);

  const toggleLike = () => {
    dispatch(toggleLikeRequest(tweet.id));
  };

  return { liked, likeCount, toggleLike };
}
