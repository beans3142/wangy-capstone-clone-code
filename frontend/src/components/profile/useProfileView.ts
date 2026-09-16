import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import type { RootState } from '../../store/rootReducer';
import { fetchProfileRequest, followRequest, unfollowRequest } from '../../store/ducks/profile/actions';

export function useProfileView() {
  const dispatch = useDispatch();
  const { userId } = useParams<{ userId: string }>();
  const targetUserId = Number(userId);
  const currentUser = useSelector((state: RootState) => state.auth.currentUser);
  const { profile, isFollowing, status, error, followStatus } = useSelector((state: RootState) => state.profile);

  useEffect(() => {
    if (currentUser && Number.isFinite(targetUserId)) {
      dispatch(fetchProfileRequest(targetUserId, currentUser.id));
    }
    // targetUserId(URL 파라미터)와 로그인 사용자가 바뀔 때만 다시 조회하면 된다.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [targetUserId, currentUser?.id]);

  const toggleFollow = () => {
    if (!currentUser) {
      return;
    }
    if (isFollowing) {
      dispatch(unfollowRequest(targetUserId, currentUser.id));
    } else {
      dispatch(followRequest(targetUserId, currentUser.id));
    }
  };

  return {
    profile,
    isFollowing,
    isOwnProfile: currentUser?.id === targetUserId,
    toggleFollow,
    isLoading: status === 'loading',
    isFollowActionLoading: followStatus === 'loading',
    error,
  };
}
