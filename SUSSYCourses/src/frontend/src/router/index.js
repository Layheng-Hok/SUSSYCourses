import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/components/MainPage.vue"; // Import TopBar component
import TeacherPage from "@/components/TeacherPage.vue";
import StudentPage from "@/components/StudentPage.vue";
import AdminPage from "@/components/AdminPage.vue";
import ProfilePage from "@/components/ProfilePage.vue";
import CoursePage from "@/components/CoursePage.vue";
import EditProfile from "@/components/EditProfile.vue";
import AccountSecurity from "@/components/AccountSecurity.vue";
import Notifications from "@/components/Notifications.vue";
import HelpSupport from "@/components/HelpSupport.vue";
import CourseList from "@/components/CourseList.vue";

import SignUpStudent from "@/components/SignUpStudent.vue";
import SignUpTeacher from "@/components/SignUpTeacher.vue";
import LogIn from "@/components/LogIn.vue";
import VerifyEmail from "@/components/VerifyEmail.vue";
import TermsOfUse from "@/components/TermsOfUse.vue";
import PrivacyPolicy from "@/components/PrivacyPolicy.vue";
import UserList from "@/components/UserList.vue";
import TeacherDashboard from "@/components/TeacherDashboard.vue";
import TeacherProfile from "@/components/TeacherProfile.vue";
import Guideline from "@/components/Guideline.vue";
import TeacherCoursesDetails from "@/components/TeacherCoursesDetails.vue";
import TeacherCourse from "@/components/TeacherCourse.vue";

import NotFound from "@/components/404.vue";
import ForbiddenPage from "@/components/403.vue";
import TeacherStreaming from "@/components/TeacherStreaming.vue";

const routes = [
  {
    path: "/", // Home route
    name: "MainPage",
    component: MainPage,
  },
  {
    path: "/teacherpage",
    name: "TeacherPage",
    component: TeacherPage,
  },
  {
    path: "/student-dashboard/:userId",
    name: "StudentPage",
    component: StudentPage,
  },
  {
    path: "/admin-dashboard",
    name: "AdminPage",
    component: AdminPage,
  },
  {
    path: "/student-dashboard/:userId/profilepage",
    name: "ProfilePage",
    component: ProfilePage,
  },
  {
    path: "/student-dashboard/:userId/editprofile",
    name: "EditProfile",
    component: EditProfile,
  },
  {
    path: "/student-dashboard/:userId/accountsecurity",
    name: "AccountSecurity",
    component: AccountSecurity,
  },
  {
    path: "/student-dashboard/:userId/notifications",
    name: "Notifications",
    component: Notifications,
  },
  {
    path: "/helpsupport",
    name: "HelpSupport",
    component: HelpSupport,
  },
  {
    path: "/course/:courseId",
    name: "CoursePage",
    component: CoursePage,
    props: true,
  },
  {
    path: "/signup-student",
    name: "SignUpStudent",
    component: SignUpStudent,
  },
  {
    path: "/signup-teacher",
    name: "SignUpTeacher",
    component: SignUpTeacher,
  },
  {
    path: "/verify-email",
    name: "VerifyEmail",
    component: VerifyEmail,
  },
  {
    path: "/login",
    name: "LogIn",
    component: LogIn,
  },
  {
    path: "/termsofuse",
    name: "TermsOfUse",
    component: TermsOfUse,
  },
  {
    path: "/privacypolicy",
    name: "PrivacyPolicy",
    component: PrivacyPolicy,
  },
  {
    path: "/admin/users",
    name: "UserList",
    component: UserList,
  },
  {
    path: "/sussy/stream/:streamId",
    name: "TeacherStreaming",
    component: TeacherStreaming,
  },
  {
    path: "/instructor-dashboard/:userId",
    name: "TeacherDashboard",
    component: TeacherDashboard,
    props: (route) => ({
      userId: route.params.userId,
      initialTab: route.query.tab, // Pass the tab query as a prop
    }),
  },
  {
    path: "/instructor-dashboard/:userId/course/:courseId",
    name: "TeacherCourse",
    component: TeacherCourse,
  },
  {
    path: "/teacherprofile",
    name: "TeacherProfile",
    component: TeacherProfile,
  },
  {
    path: "/guideline",
    name: "Guideline",
    component: Guideline,
  },
  {
    path: "/teacher-courses-details",
    name: "TeacherCoursesDetails",
    component: TeacherCoursesDetails,
  },
  {
    path : "/courselist",
    name : "CourseList",
    component : CourseList
  },
  {
    path: '/403',
    name: 'ForbiddenPage',
    component: ForbiddenPage
},
  {
    path: "/:pathMatch(.*)*", // Catch-all route
    name: "NotFound",
    component: NotFound,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
