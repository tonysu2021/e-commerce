import { AuthActionTypes } from '@src/store/types';

const initialState = {
  loggedIn: false,
  user: null
};

export function authReducer(state = initialState, action) {
  switch (action.type) {
    case AuthActionTypes.LOGIN_REQUEST:
      return {
        loggingIn: true,
        user: action.payload.user
      };
    case AuthActionTypes.LOGIN_SUCCESS:
      return {
        loggedIn: true,
        user: action.payload.user
      };
    case AuthActionTypes.LOGOUT:
    case AuthActionTypes.LOGIN_FAILURE:
      return {
        loggedIn: false,
        user: null
      };
    default:
      return state
  }
}