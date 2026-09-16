import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../store/rootReducer';
import { fetchTimelineRequest } from '../../store/ducks/feed/actions';

const PAGE_SIZE = 20;

export function useFeedView() {
  const dispatch = useDispatch();
  const currentUser = useSelector((state: RootState) => state.auth.currentUser);
  const { items, hasNext, page, timelineStatus, timelineError } = useSelector((state: RootState) => state.feed);

  useEffect(() => {
    if (currentUser) {
      dispatch(fetchTimelineRequest(currentUser.id, 0, PAGE_SIZE, false));
    }
    // currentUser는 로그인 이후 화면 진입 시점에 고정되어 있어 첫 로드에만 의존한다.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser?.id]);

  const loadMore = () => {
    if (currentUser) {
      dispatch(fetchTimelineRequest(currentUser.id, page + 1, PAGE_SIZE, true));
    }
  };

  return {
    tweets: items,
    hasNext,
    loadMore,
    isLoading: timelineStatus === 'loading',
    error: timelineError,
  };
}
