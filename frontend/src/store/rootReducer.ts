import { combineReducers } from 'redux';
import { authReducer } from './ducks/auth/reducer';
import { composerReducer } from './ducks/composer/reducer';
import { feedReducer } from './ducks/feed/reducer';
import { profileReducer } from './ducks/profile/reducer';

export const rootReducer = combineReducers({
  auth: authReducer,
  feed: feedReducer,
  profile: profileReducer,
  composer: composerReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
