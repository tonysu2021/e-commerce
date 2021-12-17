import config from 'config';
import { axiosApiInstance } from '../interceptor/jwt.interceptor'

const PREFIX = 'user';

class UserService {

    getAllUsers() {
        return axiosApiInstance
            .get(`${config.apiUrl}/${PREFIX}/all_user`)
            .then((response) => {
                const responData = response.data.data;
                return responData;
            });
    }


}

export default new UserService();
