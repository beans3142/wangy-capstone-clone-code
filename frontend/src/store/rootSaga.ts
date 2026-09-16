import { all } from 'redux-saga/effects';
import { authSaga } from './ducks/auth/sagas';
import { composerSaga } from './ducks/composer/sagas';
import { feedSaga } from './ducks/feed/sagas';
import { profileSaga } from './ducks/profile/sagas';

export function* rootSaga() {
  yield all([authSaga(), feedSaga(), profileSaga(), composerSaga()]);
}
