/**
 * 백엔드 DTO와 1:1로 대응하는 도메인 타입.
 * 필드명/구조는 harness-origin 백엔드 코드(user-service, tweet-service, image-service)의
 * 실제 Controller/DTO를 읽고 그대로 맞췄다 (worklog 참고, 추측 금지 원칙).
 */

export interface AuthUser {
  id: number;
  email: string;
  nickname: string;
  token: string;
}

export interface SignUpRequest {
  email: string;
  password: string;
  nickname: string;
}

export interface SignUpResponse {
  id: number;
  email: string;
  nickname: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  email: string;
  nickname: string;
  token: string;
}

export interface UserProfileResponse {
  id: number;
  nickname: string;
  bio: string | null;
  profileImageUrl: string | null;
}

export interface TweetResponse {
  id: number;
  authorId: number;
  content: string;
  imageUrl: string | null;
  createdAt: string;
  updatedAt: string;
  // 좋아요 백엔드가 이 작업 도중 완성되면서 TweetResponse에 뒤늦게 추가된 필드
  // (tweet-service의 likeCount/retweetCount 비정규화 컬럼, worklog 참고).
  likeCount: number;
  retweetCount: number;
}

export interface TweetCreateRequest {
  authorId: number;
  content: string;
  imageUrl?: string | null;
}

export interface ImageUploadResponse {
  url: string;
}

export interface TimelinePage {
  items: TweetResponse[];
  totalCount: number;
  hasNext: boolean;
}
