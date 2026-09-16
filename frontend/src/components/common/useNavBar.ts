import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import type { RootState } from '../../store/rootReducer';
import { logout } from '../../store/ducks/auth/actions';
import { persistCurrentUser } from '../../store/ducks/auth/reducer';

export function useNavBar() {
  const dispatch = useDispatch();
  const history = useHistory();
  const currentUser = useSelector((state: RootState) => state.auth.currentUser);

  const goToFeed = () => history.push('/feed');
  const goToMyProfile = () => {
    if (currentUser) {
      history.push(`/profile/${currentUser.id}`);
    }
  };
  const handleLogout = () => {
    persistCurrentUser(null);
    dispatch(logout());
    history.push('/login');
  };

  return { currentUser, goToFeed, goToMyProfile, handleLogout };
}
