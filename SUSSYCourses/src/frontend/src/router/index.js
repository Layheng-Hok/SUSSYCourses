// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue"; // Import TopBar component
import SignUp from "@/components/SignUp.vue";
const routes = [
    {
        path: '/', // Home route
        name: 'MainPage',
        component: MainPage
    },
    {
        path: '/signup',
        name: 'SignUP',
        component: SignUp
    },

];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
