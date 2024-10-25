// src/router/index.js

import {createRouter, createWebHistory} from 'vue-router';
import MainPage from "@/components/MainPage.vue"; // Import TopBar component
import TeacherPage from "@/components/TeacherPage.vue";
import StudentPage from "@/components/StudentPage.vue";
import AdminPage from "@/components/AdminPage.vue";

import ProfilePage from "@/components/ProfilePage.vue";
import CoursePage from "@/components/CoursePage.vue";


import SignUpStudent from "@/components/SignUpStudent.vue";
import SignUpTeacher from "@/components/SignUpTeacher.vue";
import LogIn from "@/components/LogIn.vue";
import VerifyEmail from "@/components/VerifyEmail.vue";

const routes = [
    {
        path: '/', // Home route
        name: 'MainPage',
        component: MainPage
    },
    {
        path: '/teacherpage',
        name: 'TeacherPage',
        component: TeacherPage
    },
    {
        path: '/studentpage',
        name: 'StudentPage',
        component: StudentPage
    },
    {
        path: '/adminpage',
        name: 'AdminPage',
        component: AdminPage
    },
    {
        path: '/profilepage',
        name: 'ProfilePage',
        component: ProfilePage
    },
    {
        path: '/coursepage',
        name: 'CoursePage',
        component: CoursePage
    },
    {
        path: '/signup-student',
        name: 'SignUpStudent',
        component: SignUpStudent
    },
    {
        path: '/signup-teacher',
        name: 'SignUpTeacher',
        component: SignUpTeacher
    },
    {
        path: '/verify-email',
        name: 'VerifyEmail',
        component: VerifyEmail
    },
    {
        path: '/login',
        name: 'LogIn',
        component: LogIn
    },


];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
