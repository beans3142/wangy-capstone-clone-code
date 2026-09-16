import { Avatar, Button, Container, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useProfileView } from './useProfileView';

const useStyles = makeStyles((theme) => ({
  container: {
    marginTop: theme.spacing(4),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: theme.spacing(1),
  },
  avatar: {
    width: theme.spacing(10),
    height: theme.spacing(10),
  },
}));

export function ProfileView() {
  const classes = useStyles();
  const { profile, isFollowing, isOwnProfile, toggleFollow, isLoading, isFollowActionLoading, error } =
    useProfileView();

  if (isLoading) {
    return (
      <Container className={classes.container}>
        <Typography>불러오는 중...</Typography>
      </Container>
    );
  }

  if (error || !profile) {
    return (
      <Container className={classes.container}>
        <Typography color="error">{error ?? '사용자를 찾을 수 없습니다.'}</Typography>
      </Container>
    );
  }

  return (
    <Container className={classes.container}>
      <Avatar src={profile.profileImageUrl ?? undefined} className={classes.avatar}>
        {profile.nickname.charAt(0)}
      </Avatar>
      <Typography variant="h5">{profile.nickname}</Typography>
      {profile.bio && (
        <Typography variant="body2" color="textSecondary">
          {profile.bio}
        </Typography>
      )}
      {!isOwnProfile && (
        <Button
          variant={isFollowing ? 'outlined' : 'contained'}
          color="primary"
          onClick={toggleFollow}
          disabled={isFollowActionLoading}
        >
          {isFollowing ? '언팔로우' : '팔로우'}
        </Button>
      )}
    </Container>
  );
}
