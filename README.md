
# SUSSYCourses

OpenCourseWare Web App

SUSTech 2024 Fall Project of Course `CS309 - Object-oriented Analysis and Design` Led by Professor [Yuqun ZHANG](https://zhangyuqun.github.io/)

</div>

### Simplified Directory Path Diagram
```
.
├── SUSSYCourses
│   ├── build
│   ├── gradle
│   ├── node_modules
│   └── src
│       ├── frontend                                                                  # main folder of frontend code
│       │   ├── src
│       │   │   ├── App.vue
│       │   │   ├── assets
│       │   │   ├── components
│       │   │   │   ├── 403.vue
│       │   │   │   ├── 404.vue
│       │   │   │   ├── AccountSecurity.vue
│       │   │   │   ├── AdminPage.vue
│       │   │   │   ├── AnnouncementForm.vue
│       │   │   │   ├── BarChart.vue
│       │   │   │   ├── Chat.vue
│       │   │   │   ├── CommentSection.vue
│       │   │   │   ├── CourseBreakdown.vue
│       │   │   │   ├── CourseList.vue
│       │   │   │   ├── CoursePage.vue
│       │   │   │   ├── Courseware.vue
│       │   │   │   ├── DoughnutChart.vue
│       │   │   │   ├── EditProfile.vue
│       │   │   │   ├── FilePreview.vue
│       │   │   │   ├── Guideline.vue
│       │   │   │   ├── HelpSupport.vue
│       │   │   │   ├── HeroBanner.vue                                                     
│       │   │   │   ├── LogIn.vue
│       │   │   │   ├── MainPage.vue
│       │   │   │   ├── Notifications.vue
│       │   │   │   ├── PrivacyPolicy.vue
│       │   │   │   ├── ProfilePage.vue
│       │   │   │   ├── ProfileSidebar.vue
│       │   │   │   ├── PublicCoursePage.vue  
│       │   │   │   ├── PublicCourseware.vue
│       │   │   │   ├── PublicFilePreview.vue
│       │   │   │   ├── RatingAndReview.vue
│       │   │   │   ├── Security.vue
│       │   │   │   ├── SignUpStudent.vue
│       │   │   │   ├── SignUpTeacher.vue
│       │   │   │   ├── StreamInfo.vue
│       │   │   │   ├── StudentPage.vue                                                        
│       │   │   │   ├── StudentsList.vue
│       │   │   │   ├── TeacherCourse.vue
│       │   │   │   ├── TeacherCourses.vue
│       │   │   │   ├── TeacherCoursesDetails.vue
│       │   │   │   ├── TeacherCourseware.vue
│       │   │   │   ├── TeacherDashboard.vue
│       │   │   │   ├── TeacherNotifications.vue
│       │   │   │   ├── TeacherPage.vue
│       │   │   │   ├── TeacherProfile.vue
│       │   │   │   ├── TeacherStreaming.vue
│       │   │   │   ├── TermsOfUse.vue
│       │   │   │   ├── UserInfo.vue
│       │   │   │   ├── UserList.vue
│       │   │   │   └── VerifyEmail.vue
│       │   │   ├── main.js
│       │   │   ├── router
│       │   │   │   └── index.js
│       │   │   └── services
│       │   │       └── axiosInstance.js
│       │   └── vue.config.js
│       ├── main                                                                      # main folder of backend code
│       │   ├── config
│       │   │   ├── admin_key.json
│       │   │   └── storage_key.json
│       │   ├── java
│       │   │   └── com
│       │   │       └── sustech
│       │   │           └── cs309
│       │   │               └── project
│       │   │                   └── sussycourses
│       │   │                       ├── SussyCoursesApplication.java                  # driver code of Spring Boot
│       │   │                       ├── controller
│       │   │                       │   ├── AuthController.java
│       │   │                       │   ├── CommentsController.java
│       │   │                       │   ├── CourseController.java
│       │   │                       │   ├── CourseStudentController.java
│       │   │                       │   ├── CoursewareController.java
│       │   │                       │   ├── CoursewareStudentController.java
│       │   │                       │   ├── NotificationController.java
│       │   │                       │   ├── RatingController.java
│       │   │                       │   ├── StreamController.java
│       │   │                       │   └── WebAppUserController.java
│       │   │                       ├── domain
│       │   │                       │   ├── Comment.java
│       │   │                       │   ├── Course.java
│       │   │                       │   ├── CourseStudent.java
│       │   │                       │   ├── Courseware.java
│       │   │                       │   ├── CoursewareStudent.java
│       │   │                       │   ├── Notification.java
│       │   │                       │   ├── Rating.java
│       │   │                       │   ├── Role.java
│       │   │                       │   ├── Stream.java
│       │   │                       │   └── WebAppUser.java
│       │   │                       ├── dto
│       │   │                       │   ├── AdminCourseDetailResponse.java
│       │   │                       │   ├── ApprovedCoursesResponse.java
│       │   │                       │   ├── BasicCourseResponse.java
│       │   │                       │   ├── ChangePasswordRequest.java
│       │   │                       │   ├── CommentCreationRequest.java
│       │   │                       │   ├── CommentResponse.java
│       │   │                       │   ├── CourseProgressResponse.java
│       │   │                       │   ├── CourseRequest.java
│       │   │                       │   ├── CourseStudentListResponse.java
│       │   │                       │   ├── CoursewareListResponse.java
│       │   │                       │   ├── CoursewareRequest.java
│       │   │                       │   ├── CoursewareResponse.java
│       │   │                       │   ├── CoursewareVersionResponse.java
│       │   │                       │   ├── CourseworkDataResponse.java
│       │   │                       │   ├── InstructorCourseDetailResponse.java
│       │   │                       │   ├── InstructorDetailResponse.java
│       │   │                       │   ├── LoginRequest.java
│       │   │                       │   ├── NotificationCreationRequest.java
│       │   │                       │   ├── NotificationListResponse.java
│       │   │                       │   ├── NotificationResponse.java
│       │   │                       │   ├── RatingRequest.java
│       │   │                       │   ├── RatingResponse.java
│       │   │                       │   ├── RegistrationRequest.java
│       │   │                       │   ├── ReplyRequest.java
│       │   │                       │   ├── StreamResponse.java
│       │   │                       │   ├── StudentCourseDetailResponse.java
│       │   │                       │   ├── StudentCourseListResponse.java
│       │   │                       │   ├── StudentDetailResponse.java
│       │   │                       │   ├── TopRatedCourseResponse.java
│       │   │                       │   ├── TopRatedInstructorResponse.java
│       │   │                       │   ├── UpdateCoursewareRequest.java
│       │   │                       │   ├── UpdateUserRequest.java
│       │   │                       │   └── UserResponse.java
│       │   │                       ├── repository
│       │   │                       │   ├── CommentRepository.java
│       │   │                       │   ├── CourseRepository.java
│       │   │                       │   ├── CourseStudentRepository.java
│       │   │                       │   ├── CoursewareRepository.java
│       │   │                       │   ├── CoursewareStudentRepository.java
│       │   │                       │   ├── NotificationRepository.java
│       │   │                       │   ├── RatingRepository.java
│       │   │                       │   ├── RoleRepository.java
│       │   │                       │   ├── StreamRepository.java
│       │   │                       │   └── WebAppUserRepository.java
│       │   │                       ├── security
│       │   │                       │   ├── CustomUserDetails.java
│       │   │                       │   └── SecurityConfig.java
│       │   │                       ├── service
│       │   │                       │   ├── CommentService.java
│       │   │                       │   ├── CourseService.java
│       │   │                       │   ├── CourseStudentService.java
│       │   │                       │   ├── CoursewareService.java
│       │   │                       │   ├── CoursewareStudentService.java
│       │   │                       │   ├── CustomUserDetailsService.java
│       │   │                       │   ├── EmailService.java
│       │   │                       │   ├── NotificationService.java
│       │   │                       │   ├── RatingService.java
│       │   │                       │   ├── StreamService.java
│       │   │                       │   ├── VerificationTokenService.java
│       │   │                       │   └── WebAppUserService.java
│       │   │                       └── utils
│       │   │                           └── CloudUtils.java
│       │   └── resources
│       │       ├── application.yml
│       │       ├── public
│       │       ├── schema.sql
│       │       ├── static
│       │       └── templates
│       └── test
└── README.md

```

### Tech Stack
#### Frontend
<p align="left">
    <a href="https://developer.mozilla.org/en-US/docs/Web/HTML" target="_blank" rel="noreferrer">
        <img src="https://upload.wikimedia.org/wikipedia/commons/3/38/HTML5_Badge.svg" alt="html5" width="40" height="40"/>
    </a>
    <a href="https://developer.mozilla.org/en-US/docs/Web/CSS" target="_blank" rel="noreferrer">
        <img src="https://upload.wikimedia.org/wikipedia/commons/6/62/CSS3_logo.svg" alt="css3" width="40" height="40"/>
    </a>
    <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank" rel="noreferrer"> 
        <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/javascript/javascript-original.svg" alt="javascript" width="40" height="40"/>
    </a>
    <a href="https://vuejs.org/" target="_blank" rel="noreferrer"> 
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/1024px-Vue.js_Logo_2.svg.png" alt="vuejs" width="45" height="40"/>
    </a>
</p>

#### Backend
<p align="left">
    <a href="https://spring.io/" target="_blank" rel="noreferrer">
        <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/>
    </a>
    <a href="https://www.postgresql.org" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original-wordmark.svg" alt="postgresql" width="40" height="40"/>
    </a>
    <a href="https://cloud.google.com" target="_blank" rel="noreferrer"> 
        <img src="https://www.vectorlogo.zone/logos/google_cloud/google_cloud-icon.svg" alt="gcp" width="40" height="40"/> 
    </a>
    <a href="https://gradle.org/" target="_blank" rel="noreferrer">
        <img src="https://cdn.iconscout.com/icon/free/png-512/free-gradle-logo-icon-download-in-svg-png-gif-file-formats--programming-language-logos-pack-icons-1174969.png?f=webp&w=512" alt="gradle" width="40" height="40"/>
    </a>
</p>

