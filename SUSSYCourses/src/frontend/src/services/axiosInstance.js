import axios from 'axios';

const email = 'fbringer99@gmail.com';
const password = '1';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081',
    withCredentials: true, // Allows cookies to be sent with the request
});

axiosInstance.interceptors.request.use(config => {
    const auth = btoa(`${email}:${password}`);
    config.headers.Authorization = `Basic ${auth}`;
    return config;
});

export default axiosInstance;
