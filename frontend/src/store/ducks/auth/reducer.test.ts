import { afterEach, describe, expect, it } from 'vitest';
import { authReducer } from './reducer';
import { loginFailure, loginRequest, loginSuccess, logout } from './actions';
import type { AuthUser } from '../../../types/domain';

const sampleUser: AuthUser = { id: 1, email: 'a@a.com', nickname: '유완규', token: 'jwt-token' };

describe('authReducer', () => {
  afterEach(() => {
    localStorage.clear();
  });

  it('로그인 요청 중에는 loading 상태다', () => {
    const state = authReducer(undefined, loginRequest('a@a.com', 'password123'));
    expect(state.status).toBe('loading');
  });

  it('로그인 성공 시 currentUser를 저장한다', () => {
    const state = authReducer(undefined, loginSuccess(sampleUser));
    expect(state.status).toBe('succeeded');
    expect(state.currentUser).toEqual(sampleUser);
  });

  it('로그인 실패 시 에러 메시지를 담는다', () => {
    const state = authReducer(undefined, loginFailure('비밀번호가 올바르지 않습니다'));
    expect(state.status).toBe('failed');
    expect(state.error).toBe('비밀번호가 올바르지 않습니다');
  });

  it('로그아웃하면 currentUser를 비운다', () => {
    const loggedIn = authReducer(undefined, loginSuccess(sampleUser));
    const loggedOut = authReducer(loggedIn, logout());
    expect(loggedOut.currentUser).toBeNull();
  });
});
