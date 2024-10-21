// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue"; // Import TopBar component
import TeacherPage from "@/components/TeacherPage.vue";
import StudentPage from "@/components/StudentPage.vue";
import AdminPage from "@/components/AdminPage.vue";

import ProfilePage from "@/components/ProfilePage.vue";
import CoursePage from "@/components/CoursePage.vue";


import SignUpStudent from "@/components/SignUpStudent.vue";
import SignUpTeacher from "@/components/SignUpTeacher.vue";
import LogInStudent from "@/components/LogInStudent.vue";
import LogInTeacher from "@/components/LogInTeacher.vue";

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
        path: '/signupstudent',
        name: 'SignUPStudent',
        component: SignUpStudent
    },
    {
        path: '/signupteacher',
        name: 'SignUPTeacher',
        component: SignUpTeacher
    },
    {
        path: '/loginstudent',
        name: 'LogInStudent',
        component: LogInStudent
    },
    {
        path: '/loginteacher',
        name: 'LogInTeacher',
        component: LogInTeacher
    },


];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
