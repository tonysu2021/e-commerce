import { AlertActionType } from '../types/alert-action.type';

const initialState = {
  type: '',
  message: ''
};

export function alertReducer(state = initialState, action) {
  switch (action.type) {
    case AlertActionType.SUCCESS:
      return {
        type: 'alert-success',
        message: action.payload.message
      };
    case AlertActionType.ERROR:
      return {
        type: 'alert-danger',
        message: action.payload.message
      };
    case AlertActionType.CLEAR:
      return {
        type: '',
        message: ''
      };
    default:
      return state
  }
}