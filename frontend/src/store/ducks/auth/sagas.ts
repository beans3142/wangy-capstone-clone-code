import { call, put, takeLatest } from 'redux-saga/effects';
import type { AxiosResponse } from 'axios';
import * as authApi from '../../../api/authApi';
import type { LoginResponse, SignUpResponse } from '../../../types/domain';
import {
  AUTH_LOGIN_REQUEST,
  AUTH_SIGNUP_REQUEST,
  type LoginRequestAction,
  type SignUpRequestAction,
} from './types';
import { loginFailure, loginSuccess, signUpFailure, signUpSuccess } from './actions';
import { persistCurrentUser } from './reducer';

function extractErrorMessage(error: unknown): string {
  const withResponse = error as { response?: { data?: { message?: string } } };
  return withResponse.response?.data?.message ?? '요청 처리 중 오류가 발생했습니다.';
}

function* handleSignUp(action: SignUpRequestAction) {
  try {
    const response: AxiosResponse<SignUpResponse> = yield call(authApi.signUp, action.payload);
    void response;
    yield put(signUpSuccess());
  } catch (error) {
    yield put(signUpFailure(extractErrorMessage(error)));
  }
}

function* handleLogin(action: LoginRequestAction) {
  try {
    const response: AxiosResponse<LoginResponse> = yield call(authApi.login, action.payload);
    const user = response.data;
    persistCurrentUser(user);
    yield put(loginSuccess(user));
  } catch (error) {
    yield put(loginFailure(extractErrorMessage(error)));
  }
}

export function* authSaga() {
  yield takeLatest(AUTH_SIGNUP_REQUEST, handleSignUp);
  yield takeLatest(AUTH_LOGIN_REQUEST, handleLogin);
}
