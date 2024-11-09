// axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081',
    withCredentials: true,
});

axiosInstance.interceptors.request.use(config => {
    // Retrieve username and password from localStorage
    const usn = localStorage.getItem('usn');
    const pwd = localStorage.getItem('pwd');
    if (usn && pwd) {
        const auth = btoa(`${usn}:${pwd}`);
        config.headers.Authorization = `Basic ${auth}`;
    }
    return config;
});

export default axiosInstance;
