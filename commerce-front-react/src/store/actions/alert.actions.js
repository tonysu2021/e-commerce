import { AlertActionType } from '../types/alert-action.type';

const success = (message) => {
    return { type: AlertActionType.SUCCESS, payload: { message } };
}

const error = (message) => {
    return { type: AlertActionType.ERROR, payload: { message } };
}

const clear = () => {
    return { type: AlertActionType.CLEAR, payload: {} };
}

export const alertActions = {
    success,
    error,
    clear
};