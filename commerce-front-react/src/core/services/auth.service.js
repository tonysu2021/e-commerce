import config from 'config';
import { axiosApiInstance } from '../interceptor/jwt.interceptor'

const PREFIX = 'api/auth';

class AuthService {

    /** 
     * 使用者一般登入
     * @param {String} userId 使用者帳號
     * @param {String} password 使用者密碼
     * @returns {Promise<AxiosResponse<any>>}
    */
    login(userId, password) {
        return axiosApiInstance
            .post(`${config.apiUrl}/${PREFIX}/login`, {
                userId: userId,
                password: btoa(password)
            })
            .then((response) => {
                const responData = response.data.data;
                return responData;
            });
    }

    logout() {

    }

    register(registerUserReq) {
        return axiosApiInstance.post(`${config.apiUrl}/${PREFIX}/register`, {
            userAccount: registerUserReq.userAccount,
            password: btoa(registerUserReq.password),
            userChName: registerUserReq.userChName,
            userEngName: registerUserReq.userEngName,
            userNickName: registerUserReq.userNickName,
            inviteId: registerUserReq.inviteId,
            roleId: registerUserReq.roleId,
            perMissionId: registerUserReq.perMissionId,
            lang: "zh-Hant",
        });
    }
}

export default new AuthService();
