import { useSelector } from 'react-redux';
import type { RootState } from '../../store/rootReducer';

export function usePrivateRoute() {
  const currentUser = useSelector((state: RootState) => state.auth.currentUser);
  return { isAuthenticated: currentUser !== null };
}
