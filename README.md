<div align=center>
    
# SUSSYCourses

Role-based OpenCourseWare Web App in Vue.js + Spring Boot

SUSTech 2024 Fall Project of `CS309 - Object-oriented Analysis and Design` Led by Professor [Yuqun ZHANG](https://zhangyuqun.github.io/)

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


### Features  

#### For Administrators  
- **Course Management**  
  - Approve course creation requests submitted by instructors  

#### For Instructors  
- **Course Management**  
  - Apply for course creation; courses can be:  
    - **Open** (anyone can join)  
    - **Semi-open** (students must request to join)  
    - **Non-open** (instructors manually add students)  
  - Make announcements to students in a specific course  
  - Start a live stream for remote learning and automatically notify students

- **Courseware Management**  
  - Upload courseware, including but not limited to DOCX, PDF, MD, PPTX, and MP4  
  - Categorize courseware into lectures, assignments, and projects  
  - Update courseware with version tracking  
  - Set download permissions for courseware  
  - Enforce video anti-cheat (only one video can be played at a time)  
  - Upload supplementary materials (e.g., data, code) for students  

- **Course Evaluation**  
  - Receive student feedback on content quality, teaching effectiveness, and workload balance  

#### For Students  
- **Course Management**  
  - Join an open course or request to join a semi-open course  
  - Receive notifications for updates and course announcements  

- **Learning Resources**  
  - View top-rated courses and instructors  
  - Access and download courseware (if permitted)  
  - Watch course videos online or join a live lecture stream  
  - Access a free LLM/AI chatbot  

- **Engagement & Feedback**  
  - Like favorite courses  
  - Comment on courseware and participate in instructor Q&A (supports text and media formats)  
  - Submit course evaluations with ratings and reviews  

- **Homework & Progress**  
  - Submit homework assignments  
  - Track courseware viewing progress  
  - Earn points for engagement, including providing feedback and completing course materials

### Project Setup

#### Prerequisites
Ensure you have the following installed:
- [Node.js](https://nodejs.org/) (Latest LTS recommended)
- [Vue CLI](https://cli.vuejs.org/) (`npm install -g @vue/cli`)
- [Java 17+](https://adoptium.net/)
- [Gradle](https://gradle.org/install/)
- [PostgreSQL](https://www.postgresql.org/download/)

#### Database Setup
1. Start PostgreSQL and create a database:
    ```sql
    CREATE DATABASE cs309_project;
    ```
2. Update `application.yml` in `src/main/resources/` with your database credentials:
    ```properties
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/cs309_project
        username: your_username
        password: your_password
    ```

#### Backend Setup
1. Navigate to the backend directory:
    ```sh
    cd main
    ```
2. Build and run the Spring Boot application:
    ```sh
    ./gradlew bootRun
    ```
3. However, IntelliJ IDEA is a recommended approach to run the backend server.

   The backend will start on **http://localhost:8081**.


#### Frontend Setup
1. Navigate to the frontend directory:
    ```sh
    cd frontend
    ```
2. Install dependencies:
    ```sh
    npm install
    ```
3. Run the server:
    ```sh
    npm run serve
    ```
    The frontend will be available at **http://localhost:8080**.

#### Running the Full Stack Application
1. Ensure PostgreSQL is running.
2. Start the backend using `./gradlew bootRun`.
3. Start the frontend using `npm run dev`.
4. Open `http://localhost:8080` in your browser.

#### Troubleshooting
- **Port Conflicts:** Ensure no other applications are running on ports `8080` or `8081`.
- **Database Connection Issues:** Double-check PostgreSQL credentials in `application.yml`.
- **Vue Not Loading:** Clear cache and restart the development server.
  ```sh
  rm -rf node_modules package-lock.json
  npm install
  npm run serve
  ```
  
### Dev Team

| Name                                | Role     |
|-------------------------------------|----------|
| [Jaouhara Zerhouni Khal](https://github.com/Jouwy)         | Frontend |
| [Tan Hao Yang](https://github.com/haoyanghy)          | Frontend |
| [Harrold Tok Kwan Hang](https://github.com/Barracudapi)                 | Backend  |
| [Hok Lay Heng](https://github.com/Layheng-Hok)                   | Backend  |
