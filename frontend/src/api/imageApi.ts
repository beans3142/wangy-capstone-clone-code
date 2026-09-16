import { apiClient } from './client';
import type { ImageUploadResponse } from '../types/domain';

/** image-service ImageController와 1:1 대응 (POST /api/v1/images, multipart file 필드명 "file"). */
export function uploadImage(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return apiClient.post<ImageUploadResponse>('/api/v1/images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}
