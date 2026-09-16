import { Button, Container, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { TweetComposer } from './TweetComposer';
import { TweetCard } from './TweetCard';
import { useFeedView } from './useFeedView';

const useStyles = makeStyles((theme) => ({
  container: {
    marginTop: theme.spacing(4),
    marginBottom: theme.spacing(4),
  },
  loadMore: {
    display: 'block',
    margin: `${theme.spacing(2)}px auto`,
  },
  empty: {
    textAlign: 'center',
    marginTop: theme.spacing(4),
  },
}));

export function FeedView() {
  const classes = useStyles();
  const { tweets, hasNext, loadMore, isLoading, error } = useFeedView();

  return (
    <Container maxWidth="sm" className={classes.container}>
      <Typography variant="h5" component="h1" gutterBottom>
        타임라인
      </Typography>
      <TweetComposer />
      {error && <Typography color="error">{error}</Typography>}
      {tweets.length === 0 && !isLoading ? (
        <Typography className={classes.empty} color="textSecondary">
          아직 트윗이 없습니다. 첫 트윗을 작성해보세요.
        </Typography>
      ) : (
        tweets.map((tweet) => <TweetCard key={tweet.id} tweet={tweet} />)
      )}
      {hasNext && (
        <Button className={classes.loadMore} onClick={loadMore} disabled={isLoading}>
          {isLoading ? '불러오는 중...' : '더 보기'}
        </Button>
      )}
    </Container>
  );
}
