import { Link as RouterLink } from 'react-router-dom';
import { Button, Container, Link, TextField, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useLoginView } from './useLoginView';

const useStyles = makeStyles((theme) => ({
  container: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    gap: theme.spacing(2),
  },
  submitButton: {
    marginTop: theme.spacing(2),
  },
  error: {
    color: theme.palette.error.main,
  },
}));

export function LoginView() {
  const classes = useStyles();
  const { email, password, setEmail, setPassword, submit, isLoading, error } = useLoginView();

  return (
    <Container maxWidth="xs" className={classes.container}>
      <Typography variant="h4" component="h1">
        로그인
      </Typography>
      <TextField
        label="이메일"
        type="email"
        value={email}
        onChange={(event) => setEmail(event.target.value)}
        fullWidth
      />
      <TextField
        label="비밀번호"
        type="password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        fullWidth
      />
      {error && (
        <Typography variant="body2" className={classes.error}>
          {error}
        </Typography>
      )}
      <Button
        variant="contained"
        color="primary"
        onClick={submit}
        disabled={isLoading || !email || !password}
        className={classes.submitButton}
      >
        {isLoading ? '로그인 중...' : '로그인'}
      </Button>
      <Typography variant="body2">
        계정이 없으신가요? <Link component={RouterLink} to="/signup">회원가입</Link>
      </Typography>
    </Container>
  );
}
