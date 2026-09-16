import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import type { RootState } from '../../store/rootReducer';
import { loginRequest } from '../../store/ducks/auth/actions';

/**
 * View-로직 분리 규칙(architecture.md 3.2)에 따라 LoginView.tsx는 이 훅만 호출한다.
 * useSelector/useDispatch는 여기 안에서만 쓴다.
 */
export function useLoginView() {
  const dispatch = useDispatch();
  const history = useHistory();
  const { currentUser, status, error } = useSelector((state: RootState) => state.auth);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  useEffect(() => {
    if (currentUser) {
      history.push('/feed');
    }
  }, [currentUser, history]);

  const submit = () => {
    dispatch(loginRequest(email, password));
  };

  return {
    email,
    password,
    setEmail,
    setPassword,
    submit,
    isLoading: status === 'loading',
    error,
  };
}
