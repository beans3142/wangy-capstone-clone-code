import type { AuthUser } from '../../../types/domain';

export interface AuthState {
  currentUser: AuthUser | null;
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
}

export const AUTH_SIGNUP_REQUEST = 'auth/signupRequest';
export const AUTH_SIGNUP_SUCCESS = 'auth/signupSuccess';
export const AUTH_SIGNUP_FAILURE = 'auth/signupFailure';
export const AUTH_LOGIN_REQUEST = 'auth/loginRequest';
export const AUTH_LOGIN_SUCCESS = 'auth/loginSuccess';
export const AUTH_LOGIN_FAILURE = 'auth/loginFailure';
export const AUTH_LOGOUT = 'auth/logout';

export interface SignUpRequestAction {
  type: typeof AUTH_SIGNUP_REQUEST;
  payload: { email: string; password: string; nickname: string };
}

export interface SignUpSuccessAction {
  type: typeof AUTH_SIGNUP_SUCCESS;
}

export interface SignUpFailureAction {
  type: typeof AUTH_SIGNUP_FAILURE;
  payload: { message: string };
}

export interface LoginRequestAction {
  type: typeof AUTH_LOGIN_REQUEST;
  payload: { email: string; password: string };
}

export interface LoginSuccessAction {
  type: typeof AUTH_LOGIN_SUCCESS;
  payload: { user: AuthUser };
}

export interface LoginFailureAction {
  type: typeof AUTH_LOGIN_FAILURE;
  payload: { message: string };
}

export interface LogoutAction {
  type: typeof AUTH_LOGOUT;
}

export type AuthAction =
  | SignUpRequestAction
  | SignUpSuccessAction
  | SignUpFailureAction
  | LoginRequestAction
  | LoginSuccessAction
  | LoginFailureAction
  | LogoutAction;
