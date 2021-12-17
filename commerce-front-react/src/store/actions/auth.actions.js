import { AuthActionTypes } from '@src/store/types';

const loginSuccess = (user) => {
    return { type: AuthActionTypes.LOGIN_SUCCESS, payload: { user } }
}
const loginFailure = (error) => {
    return { type: AuthActionTypes.LOGIN_FAILURE, payload: { error } }
}

const logout = () => {
    return { type: AuthActionTypes.LOGOUT, payload: {} };
}

export const authActions = {
    loginSuccess,
    loginFailure,
    logout,
};
