import { setStoredToken } from '../../../api/client';
import type { AuthUser } from '../../../types/domain';
import {
  AUTH_LOGIN_FAILURE,
  AUTH_LOGIN_REQUEST,
  AUTH_LOGIN_SUCCESS,
  AUTH_LOGOUT,
  AUTH_SIGNUP_FAILURE,
  AUTH_SIGNUP_REQUEST,
  AUTH_SIGNUP_SUCCESS,
  type AuthAction,
  type AuthState,
} from './types';

const USER_STORAGE_KEY = 'twitterClone.user';

function readStoredUser(): AuthUser | null {
  const raw = localStorage.getItem(USER_STORAGE_KEY);
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw) as AuthUser;
  } catch {
    return null;
  }
}

export function persistCurrentUser(user: AuthUser | null): void {
  if (user) {
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
    setStoredToken(user.token);
  } else {
    localStorage.removeItem(USER_STORAGE_KEY);
    setStoredToken(null);
  }
}

const initialState: AuthState = {
  currentUser: readStoredUser(),
  status: 'idle',
  error: null,
};

export function authReducer(state: AuthState = initialState, action: AuthAction): AuthState {
  switch (action.type) {
    case AUTH_SIGNUP_REQUEST:
    case AUTH_LOGIN_REQUEST:
      return { ...state, status: 'loading', error: null };
    case AUTH_SIGNUP_SUCCESS:
      return { ...state, status: 'succeeded' };
    case AUTH_LOGIN_SUCCESS:
      return { ...state, status: 'succeeded', currentUser: action.payload.user, error: null };
    case AUTH_SIGNUP_FAILURE:
    case AUTH_LOGIN_FAILURE:
      return { ...state, status: 'failed', error: action.payload.message };
    case AUTH_LOGOUT:
      return { ...state, currentUser: null, status: 'idle', error: null };
    default:
      return state;
  }
}
