import type { ReactElement } from 'react';
import { Redirect, Route, type RouteProps } from 'react-router-dom';
import { usePrivateRoute } from './usePrivateRoute';

interface PrivateRouteProps extends RouteProps {
  children: ReactElement;
}

/**
 * 인증 안 된 상태로 보호된 화면(피드/작성/프로필)에 접근하면 로그인 화면으로 보낸다.
 * (PRD 수용 기준: "인증 안 된 상태로 피드/작성 화면 접근 시 로그인 화면으로 보낸다.")
 */
export function PrivateRoute({ children, ...rest }: PrivateRouteProps) {
  const { isAuthenticated } = usePrivateRoute();

  return (
    <Route
      {...rest}
      render={() => (isAuthenticated ? children : <Redirect to="/login" />)}
    />
  );
}
