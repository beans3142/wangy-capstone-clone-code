import type { AuthUser } from '../../../types/domain';
import {
  AUTH_LOGIN_FAILURE,
  AUTH_LOGIN_REQUEST,
  AUTH_LOGIN_SUCCESS,
  AUTH_LOGOUT,
  AUTH_SIGNUP_FAILURE,
  AUTH_SIGNUP_REQUEST,
  AUTH_SIGNUP_SUCCESS,
  type LoginFailureAction,
  type LoginRequestAction,
  type LoginSuccessAction,
  type LogoutAction,
  type SignUpFailureAction,
  type SignUpRequestAction,
  type SignUpSuccessAction,
} from './types';

export const signUpRequest = (email: string, password: string, nickname: string): SignUpRequestAction => ({
  type: AUTH_SIGNUP_REQUEST,
  payload: { email, password, nickname },
});

export const signUpSuccess = (): SignUpSuccessAction => ({ type: AUTH_SIGNUP_SUCCESS });

export const signUpFailure = (message: string): SignUpFailureAction => ({
  type: AUTH_SIGNUP_FAILURE,
  payload: { message },
});

export const loginRequest = (email: string, password: string): LoginRequestAction => ({
  type: AUTH_LOGIN_REQUEST,
  payload: { email, password },
});

export const loginSuccess = (user: AuthUser): LoginSuccessAction => ({
  type: AUTH_LOGIN_SUCCESS,
  payload: { user },
});

export const loginFailure = (message: string): LoginFailureAction => ({
  type: AUTH_LOGIN_FAILURE,
  payload: { message },
});

export const logout = (): LogoutAction => ({ type: AUTH_LOGOUT });
