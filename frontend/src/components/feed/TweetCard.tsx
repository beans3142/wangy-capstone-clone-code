import { Avatar, Card, CardContent, CardMedia, IconButton, Typography } from '@material-ui/core';
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import { makeStyles } from '@material-ui/core/styles';
import type { TweetResponse } from '../../types/domain';
import { useTweetCard } from './useTweetCard';

const useStyles = makeStyles((theme) => ({
  card: {
    marginBottom: theme.spacing(2),
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    gap: theme.spacing(1),
    marginBottom: theme.spacing(1),
  },
  media: {
    height: 240,
  },
  likeRow: {
    display: 'flex',
    alignItems: 'center',
  },
}));

interface TweetCardProps {
  tweet: TweetResponse;
}

export function TweetCard({ tweet }: TweetCardProps) {
  const classes = useStyles();
  const { liked, likeCount, toggleLike } = useTweetCard(tweet);

  return (
    <Card className={classes.card}>
      <CardContent>
        <div className={classes.header}>
          <Avatar>{String(tweet.authorId)}</Avatar>
          <Typography variant="subtitle2">작성자 #{tweet.authorId}</Typography>
        </div>
        <Typography variant="body1">{tweet.content}</Typography>
      </CardContent>
      {tweet.imageUrl && <CardMedia className={classes.media} image={tweet.imageUrl} title="첨부 이미지" />}
      <CardContent className={classes.likeRow}>
        <IconButton
          onClick={toggleLike}
          color={liked ? 'secondary' : 'default'}
          aria-label="좋아요"
        >
          {liked ? <FavoriteIcon /> : <FavoriteBorderIcon />}
        </IconButton>
        <Typography variant="body2">{likeCount}</Typography>
      </CardContent>
    </Card>
  );
}
