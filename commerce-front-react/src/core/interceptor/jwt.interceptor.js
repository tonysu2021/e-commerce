/*eslint no-undef: "error"*/
/*eslint-env node*/

import { store } from '@store/store';

const axios = require('axios');
export const axiosApiInstance = axios.create();

// Request interceptor for API calls
axiosApiInstance.interceptors.request.use(
    config => {
        const state = store.getState();
        const auth = state.authentication.user;
        config.headers = {
            'Authorization': `${auth ? auth.accessToken : ''}`,
            'Accept': 'application/json'
        }
        config.withCredentials = true;
        return config;
    },
    error => {
        Promise.reject(error)
    });

// Response interceptor for API calls
axiosApiInstance.interceptors.response.use((response) => {
    return response
}, function (error) {
    const originalRequest = error.config;
    if (error.response && error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = true;
        const access_token = refreshAccessToken();
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + access_token;
        return axiosApiInstance(originalRequest);
    }
    return Promise.reject(error);
});
