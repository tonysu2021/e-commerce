export const WsMsgActionTypes = {
    /** 未知 */
    UNKNOWN: 0,
    /** 刷新token */
    TOKEN_REFRESH: 1,
    /** TOKEN 失效 */
    TOKEN_INVALID: 2,
    /** 連線建立 */
    CONNECTION_SUCCESS: 3,
    /** 強制斷線 */
    FORCE_CLOSE_CONNECTION: 4,
    /** 聊天室同步歷史開始 */
    HISTORY_START: 10,
    /** 聊天室同步歷史結束*/
    HISTORY_END: 11,
    /** 使用者最後一筆已讀 */
    LAST_READ_MESSAGE_REQ: 12,
    /** 通知其他人該使用者最後一筆已讀*/
    LAST_READ_MESSAGE_RES: 13,
    /** 1 : 1聊天 */
    SINGLE: 21,
    /** 1 : 1 貼圖 */
    SINGLE_PIC: 22,
    /** 1 : 1 圖片上傳 */
    SINGLE_IMAGE: 23,
    /** 1 : 1 檔案上傳,非圖片其他檔案 */
    SINGLE_FILE: 24,
    /** 1 : 1 留言上傳 */
    SINGLE_WAVE: 25,
    /** 群組聊天 */
    GROUP: 31,
    /** 群組 貼除 */
    GROUP_PIC: 32,
    /** 群組圖片上傳 */
    GROUP_IMAGE: 33,
    /** 1 : 1 群組檔案上傳,非圖片其他檔案 */
    GROUP_FILE: 34,
    /** 撤銷對話 */
    UNDO_MESSAGE: 41,
    /** 顯示content自訂訊息 */
    SHOW_CONTENT: 42,
    /** 建立聊天室 */
    CREATE_CHAT: 50,
    /** 解散聊天室 */
    DISMISS_CHAT: 51,
    /** 加入聊天室*/
    JOIN_CHAT: 52,
    /** 離開聊天室 */
    LEAVE_CHAT: 53,
    /** 拒絕加入聊天室 */
    REJECT_CHAT: 54,
    /** 群組刪除人員*/
    KICK_OFF: 55,
    /** 建立1V1聊天室 */
    CREATE_CHAT_1V1: 56,
    /** 被邀請加入群組 */
    INVITE_JOIN_CHAT_GROUP: 57,
    /** 邀請群組清單 */
    INVITE_GROUP_LIST: 58,
    /** 被邀請加入多人房間 */
    INVITE_JOIN_CHAT_MANY: 59,
    /** 邀請多人房間清單 */
    INVITE_MANY_LIST: 60,
    /**踢出房間清單*/
    KICK_OFF_LIST: 61,
    /** 邀請好友 */
    INVITE_FRIEND: 80,
    /** 加入好友*/
    ADD_FRIEND: 81,
    /** 拒絕加入好友 */
    REJECT_FRIEND: 82,
    /** 删除好友 */
    DELETE_FRIEND: 83,
    /** 前端心跳ping */
    FRONT_PING: 91,
    /** 前端心跳pong */
    FRONT_PONG: 92,
    /** 後端心跳ping */
    SERVER_PING: 93,
    /** 後端心跳pong */
    SERVER_PONG: 94,
};
