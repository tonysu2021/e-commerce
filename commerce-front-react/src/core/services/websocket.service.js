import config from 'config';
import { store } from '@store/store';
import { WsMsgActionTypes } from '@core/constants';

class WebsocketService {
    constructor() {
        this.webSocket = null;
    }

    wsConnect() {
        const state = store.getState();
        const auth = state.authentication.user;
        const webSocket = new WebSocket(`${config.wsUrl}/${auth.accessToken}`);


        webSocket.onmessage = (event) => {
            this.recieveMessage(event.data);
        }

    }

    recieveMessage(message) {
        let recieveMsg = JSON.parse(message);
        recieveMsg.isSendServer = true;
        switch (recieveMsg.type) {
            case WsMsgActionTypes.CONNECTION_SUCCESS: {
                console.log('CONNECTION_SUCCESS : ', JSON.parse(message));
                break;
            }
            case WsMsgActionTypes.FORCE_CLOSE_CONNECTION:
            case WsMsgActionTypes.TOKEN_INVALID: {

                break;
            }
            case WsMsgActionTypes.TOKEN_REFRESH: {

                break;
            }
            case WsMsgActionTypes.UNDO_MESSAGE:
            case WsMsgActionTypes.SINGLE_IMAGE:
            case WsMsgActionTypes.SINGLE_PIC:
            case WsMsgActionTypes.SINGLE:
            case WsMsgActionTypes.SINGLE_FILE:
            case WsMsgActionTypes.SINGLE_WAVE: {
                
                break;
            }
            default:
            // Do nothing
        }

    }

    sendMessage(type, payload) {
        this.webSocket.send(JSON.stringify({ type, payload }));
    }
}

export default new WebsocketService();