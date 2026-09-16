import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { App } from './App';
import { store } from './store';
import { theme } from './theme';
import './index.css';

// MUI v4(architecture.md 강제 사항)는 findDOMNode 기반이라 StrictMode의 이중 렌더/경고와
// 충돌한다. React 19 스캐폴드 기본값이던 StrictMode는 이 프로젝트 제약(MUI v4)과
// 양립하지 않아 의도적으로 제외했다 (worklog 참고).
createRoot(document.getElementById('root')!).render(
  <Provider store={store}>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <App />
    </ThemeProvider>
  </Provider>,
);
