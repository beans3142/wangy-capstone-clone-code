import { Link as RouterLink } from 'react-router-dom';
import { Button, Container, Link, TextField, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useSignupView } from './useSignupView';

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

export function SignupView() {
  const classes = useStyles();
  const { email, password, nickname, setEmail, setPassword, setNickname, submit, isLoading, error } =
    useSignupView();

  return (
    <Container maxWidth="xs" className={classes.container}>
      <Typography variant="h4" component="h1">
        회원가입
      </Typography>
      <TextField
        label="이메일"
        type="email"
        value={email}
        onChange={(event) => setEmail(event.target.value)}
        fullWidth
      />
      <TextField
        label="비밀번호 (8자 이상)"
        type="password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        fullWidth
      />
      <TextField
        label="닉네임"
        value={nickname}
        onChange={(event) => setNickname(event.target.value)}
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
        disabled={isLoading || !email || !password || !nickname}
        className={classes.submitButton}
      >
        {isLoading ? '가입 중...' : '회원가입'}
      </Button>
      <Typography variant="body2">
        이미 계정이 있으신가요? <Link component={RouterLink} to="/login">로그인</Link>
      </Typography>
    </Container>
  );
}
