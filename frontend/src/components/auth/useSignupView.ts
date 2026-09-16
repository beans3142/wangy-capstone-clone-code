import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import type { RootState } from '../../store/rootReducer';
import { signUpRequest } from '../../store/ducks/auth/actions';

export function useSignupView() {
  const dispatch = useDispatch();
  const history = useHistory();
  const { status, error } = useSelector((state: RootState) => state.auth);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');

  useEffect(() => {
    if (status === 'succeeded') {
      history.push('/login');
    }
  }, [status, history]);

  const submit = () => {
    dispatch(signUpRequest(email, password, nickname));
  };

  return {
    email,
    password,
    nickname,
    setEmail,
    setPassword,
    setNickname,
    submit,
    isLoading: status === 'loading',
    error,
  };
}
