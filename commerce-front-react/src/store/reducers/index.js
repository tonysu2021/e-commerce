import { combineReducers } from 'redux';

import { authReducer } from './auth.reducer';
import { alertReducer } from './alert.reducer';

const rootReducer = combineReducers({
  authentication: authReducer,
  alert: alertReducer
});

export default rootReducer;