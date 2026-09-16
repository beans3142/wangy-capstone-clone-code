import { Redirect, Route, Switch, BrowserRouter as Router } from 'react-router-dom';
import { NavBar } from './components/common/NavBar';
import { PrivateRoute } from './components/common/PrivateRoute';
import { LoginView } from './components/auth/LoginView';
import { SignupView } from './components/auth/SignupView';
import { FeedView } from './components/feed/FeedView';
import { ProfileView } from './components/profile/ProfileView';

export function App() {
  return (
    <Router>
      <NavBar />
      <Switch>
        <Route exact path="/login" component={LoginView} />
        <Route exact path="/signup" component={SignupView} />
        <PrivateRoute exact path="/feed">
          <FeedView />
        </PrivateRoute>
        <PrivateRoute exact path="/profile/:userId">
          <ProfileView />
        </PrivateRoute>
        <Redirect to="/feed" />
      </Switch>
    </Router>
  );
}
