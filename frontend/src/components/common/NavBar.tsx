import { AppBar, Button, Toolbar, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useNavBar } from './useNavBar';

const useStyles = makeStyles((theme) => ({
  title: {
    flexGrow: 1,
    cursor: 'pointer',
  },
  spacer: {
    marginRight: theme.spacing(1),
  },
}));

export function NavBar() {
  const classes = useStyles();
  const { currentUser, goToFeed, goToMyProfile, handleLogout } = useNavBar();

  if (!currentUser) {
    return null;
  }

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" className={classes.title} onClick={goToFeed}>
          트위터 클론
        </Typography>
        <Button color="inherit" className={classes.spacer} onClick={goToMyProfile}>
          {currentUser.nickname}
        </Button>
        <Button color="inherit" onClick={handleLogout}>
          로그아웃
        </Button>
      </Toolbar>
    </AppBar>
  );
}
