import { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import type { RootState } from '../../store/rootReducer';
import { createTweetRequest } from '../../store/ducks/feed/actions';
import { resetComposer, uploadImageRequest } from '../../store/ducks/composer/actions';

export function useTweetComposer() {
  const dispatch = useDispatch();
  const currentUser = useSelector((state: RootState) => state.auth.currentUser);
  const { uploadedImageUrl, uploadStatus } = useSelector((state: RootState) => state.composer);
  const createStatus = useSelector((state: RootState) => state.feed.createStatus);

  const [content, setContent] = useState('');
  const wasSubmitting = useRef(false);

  useEffect(() => {
    if (createStatus === 'loading') {
      wasSubmitting.current = true;
    }
    if (createStatus === 'succeeded' && wasSubmitting.current) {
      setContent('');
      dispatch(resetComposer());
      wasSubmitting.current = false;
    }
  }, [createStatus, dispatch]);

  const selectImage = (file: File) => {
    dispatch(uploadImageRequest(file));
  };

  const submit = () => {
    if (!currentUser || !content.trim()) {
      return;
    }
    dispatch(createTweetRequest(currentUser.id, content.trim(), uploadedImageUrl));
  };

  return {
    content,
    setContent,
    selectImage,
    submit,
    isUploadingImage: uploadStatus === 'loading',
    uploadedImageUrl,
    isSubmitting: createStatus === 'loading',
    canSubmit: content.trim().length > 0 && uploadStatus !== 'loading',
  };
}
