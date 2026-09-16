import type { ChangeEvent } from 'react';
import { Button, Card, CardContent, TextField, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useTweetComposer } from './useTweetComposer';

const useStyles = makeStyles((theme) => ({
  card: {
    marginBottom: theme.spacing(3),
  },
  actions: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: theme.spacing(1),
  },
  preview: {
    maxWidth: '100%',
    maxHeight: 160,
    marginTop: theme.spacing(1),
    borderRadius: theme.shape.borderRadius,
  },
}));

export function TweetComposer() {
  const classes = useStyles();
  const { content, setContent, selectImage, submit, isUploadingImage, uploadedImageUrl, isSubmitting, canSubmit } =
    useTweetComposer();

  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      selectImage(file);
    }
  };

  return (
    <Card className={classes.card}>
      <CardContent>
        <TextField
          placeholder="무슨 일이 일어나고 있나요?"
          value={content}
          onChange={(event) => setContent(event.target.value)}
          multiline
          minRows={2}
          fullWidth
        />
        {uploadedImageUrl && (
          <img src={uploadedImageUrl} alt="업로드 미리보기" className={classes.preview} />
        )}
        <div className={classes.actions}>
          <label htmlFor="tweet-image-input">
            <input
              id="tweet-image-input"
              type="file"
              accept="image/*"
              hidden
              onChange={handleFileChange}
            />
            <Button component="span" disabled={isUploadingImage}>
              {isUploadingImage ? '업로드 중...' : '이미지 첨부'}
            </Button>
          </label>
          <Button
            variant="contained"
            color="primary"
            onClick={submit}
            disabled={!canSubmit || isSubmitting}
          >
            {isSubmitting ? '작성 중...' : '트윗하기'}
          </Button>
        </div>
        <Typography variant="caption" color="textSecondary">
          {content.length}/280
        </Typography>
      </CardContent>
    </Card>
  );
}
