import {
  COMPOSER_RESET,
  COMPOSER_UPLOAD_IMAGE_FAILURE,
  COMPOSER_UPLOAD_IMAGE_REQUEST,
  COMPOSER_UPLOAD_IMAGE_SUCCESS,
  type ComposerAction,
  type ComposerState,
} from './types';

const initialState: ComposerState = {
  uploadedImageUrl: null,
  uploadStatus: 'idle',
  uploadError: null,
};

export function composerReducer(state: ComposerState = initialState, action: ComposerAction): ComposerState {
  switch (action.type) {
    case COMPOSER_UPLOAD_IMAGE_REQUEST:
      return { ...state, uploadStatus: 'loading', uploadError: null };
    case COMPOSER_UPLOAD_IMAGE_SUCCESS:
      return { ...state, uploadStatus: 'succeeded', uploadedImageUrl: action.payload.url };
    case COMPOSER_UPLOAD_IMAGE_FAILURE:
      return { ...state, uploadStatus: 'failed', uploadError: action.payload.message };
    case COMPOSER_RESET:
      return initialState;
    default:
      return state;
  }
}
