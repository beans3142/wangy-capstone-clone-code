import { apiClient } from './client';
import type { LoginRequest, LoginResponse, SignUpRequest, SignUpResponse } from '../types/domain';

/**
 * user-service AuthController와 1:1 대응 (POST /api/v1/auth/signup, /login).
 * 두 경로 모두 api-gateway free-paths에 등록되어 있어 토큰 없이 호출 가능하다.
 */
export function signUp(request: SignUpRequest) {
  return apiClient.post<SignUpResponse>('/api/v1/auth/signup', request);
}

export function login(request: LoginRequest) {
  return apiClient.post<LoginResponse>('/api/v1/auth/login', request);
}
