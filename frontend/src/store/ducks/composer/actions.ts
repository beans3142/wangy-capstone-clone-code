import {
  COMPOSER_RESET,
  COMPOSER_UPLOAD_IMAGE_FAILURE,
  COMPOSER_UPLOAD_IMAGE_REQUEST,
  COMPOSER_UPLOAD_IMAGE_SUCCESS,
  type ComposerResetAction,
  type UploadImageFailureAction,
  type UploadImageRequestAction,
  type UploadImageSuccessAction,
} from './types';

export const uploadImageRequest = (file: File): UploadImageRequestAction => ({
  type: COMPOSER_UPLOAD_IMAGE_REQUEST,
  payload: { file },
});

export const uploadImageSuccess = (url: string): UploadImageSuccessAction => ({
  type: COMPOSER_UPLOAD_IMAGE_SUCCESS,
  payload: { url },
});

export const uploadImageFailure = (message: string): UploadImageFailureAction => ({
  type: COMPOSER_UPLOAD_IMAGE_FAILURE,
  payload: { message },
});

export const resetComposer = (): ComposerResetAction => ({ type: COMPOSER_RESET });
