export interface ComposerState {
  uploadedImageUrl: string | null;
  uploadStatus: 'idle' | 'loading' | 'succeeded' | 'failed';
  uploadError: string | null;
}

export const COMPOSER_UPLOAD_IMAGE_REQUEST = 'composer/uploadImageRequest';
export const COMPOSER_UPLOAD_IMAGE_SUCCESS = 'composer/uploadImageSuccess';
export const COMPOSER_UPLOAD_IMAGE_FAILURE = 'composer/uploadImageFailure';
export const COMPOSER_RESET = 'composer/reset';

export interface UploadImageRequestAction {
  type: typeof COMPOSER_UPLOAD_IMAGE_REQUEST;
  payload: { file: File };
}

export interface UploadImageSuccessAction {
  type: typeof COMPOSER_UPLOAD_IMAGE_SUCCESS;
  payload: { url: string };
}

export interface UploadImageFailureAction {
  type: typeof COMPOSER_UPLOAD_IMAGE_FAILURE;
  payload: { message: string };
}

export interface ComposerResetAction {
  type: typeof COMPOSER_RESET;
}

export type ComposerAction =
  | UploadImageRequestAction
  | UploadImageSuccessAction
  | UploadImageFailureAction
  | ComposerResetAction;
