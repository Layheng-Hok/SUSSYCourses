import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081',
    withCredentials: true,
});

axiosInstance.interceptors.request.use(config => {
    const usn = localStorage.getItem('usn');
    const pwd = localStorage.getItem('pwd');
    if (usn && pwd) {
        const auth = btoa(`${usn}:${pwd}`);
        config.headers.Authorization = `Basic ${auth}`;
    }
    return config;
});

const axiosInstance2 = axios.create({
    baseURL: 'http://localhost:8081',
    withCredentials: true,
});

axiosInstance2.interceptors.request.use(config => {
    const usn = localStorage.getItem('public_usn');
    const pwd = localStorage.getItem('public_pwd');
    if (usn && pwd) {
        const auth = btoa(`${usn}:${pwd}`);
        config.headers.Authorization = `Basic ${auth}`;
    }
    return config;
});

export default {axiosInstance, axiosInstance2};
