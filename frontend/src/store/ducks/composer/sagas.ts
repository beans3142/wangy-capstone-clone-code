import { call, put, takeLatest } from 'redux-saga/effects';
import type { AxiosResponse } from 'axios';
import * as imageApi from '../../../api/imageApi';
import type { ImageUploadResponse } from '../../../types/domain';
import { COMPOSER_UPLOAD_IMAGE_REQUEST, type UploadImageRequestAction } from './types';
import { uploadImageFailure, uploadImageSuccess } from './actions';

function extractErrorMessage(error: unknown): string {
  const withResponse = error as { response?: { data?: { message?: string } } };
  return withResponse.response?.data?.message ?? '이미지 업로드에 실패했습니다.';
}

function* handleUploadImage(action: UploadImageRequestAction) {
  try {
    const response: AxiosResponse<ImageUploadResponse> = yield call(imageApi.uploadImage, action.payload.file);
    yield put(uploadImageSuccess(response.data.url));
  } catch (error) {
    yield put(uploadImageFailure(extractErrorMessage(error)));
  }
}

export function* composerSaga() {
  yield takeLatest(COMPOSER_UPLOAD_IMAGE_REQUEST, handleUploadImage);
}
