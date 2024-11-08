// src/router/index.js

import {createRouter, createWebHistory} from 'vue-router';
import MainPage from "@/components/MainPage.vue"; // Import TopBar component
import TeacherPage from "@/components/TeacherPage.vue";
import StudentPage from "@/components/StudentPage.vue";
import AdminPage from "@/components/AdminPage.vue";

import ProfilePage from "@/components/ProfilePage.vue";
import CoursePage from "@/components/CoursePage.vue";
import EditProfile from '@/components/EditProfile.vue';
import AccountSecurity from '@/components/AccountSecurity.vue';
import Notifications from '@/components/Notifications.vue';
import HelpSupport from '@/components/HelpSupport.vue';

import SignUpStudent from "@/components/SignUpStudent.vue";
import SignUpTeacher from "@/components/SignUpTeacher.vue";
import LogIn from "@/components/LogIn.vue";
import VerifyEmail from "@/components/VerifyEmail.vue";
import TermsOfUse from "@/components/TermsOfUse.vue";
import PrivacyPolicy from "@/components/PrivacyPolicy.vue";

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
        path: '/editprofile',
        name: 'EditProfile',
        component: EditProfile
    },
    {
        path: '/accountsecurity',
        name: 'AccountSecurity',
        component: AccountSecurity
    },
    {
        path: '/notifications',
        name: 'Notifications',
        component: Notifications
    },
    {
        path: '/helpsupport',
        name: 'HelpSupport',
        component: HelpSupport
    },
    {
        path: '/course/:courseId',
        name: 'CoursePage',
        component: CoursePage,
        props: true,
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
    {
        path: '/termsofuse',
        name: 'TermsOfUse',
        component: TermsOfUse
    },
    {
        path: '/privacypolicy',
        name: 'PrivacyPolicy',
        component: PrivacyPolicy
    },



];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
