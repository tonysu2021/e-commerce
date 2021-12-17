import { WsActionTypes } from '@src/store/types';
import WebsocketService from '@src/core/services/websocket.service'


export const WsActions = {
    wsSendMsg,
    wsRecieveMsg
};

function wsSendMsg(message) {
    return dispatch => {
        WebsocketService.sendMessage(message);
        dispatch({type: WsActionTypes.Ws_Send_Msg, message});
    };
}

function wsRecieveMsg(message) {
    return { type: WsActionTypes.Ws_Recieve_Msg, message };
}
