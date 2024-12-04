<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img class="nav-logo" src="@/assets/Banner/banner2.png" alt="Element logo"/>
      </router-link>
    </el-menu-item>
    <el-menu-item index="1" @click="toggleSidebar" class="sidebar-toggle">
      <img class="profile-pic-small" :src=" user?.profileImageUrl || defaultProfilePic" alt="Profile Picture"/>
    </el-menu-item>
  </el-menu>

  <div class="page-container">
    <div
        v-if="isSidebarVisible"
        class="overlay"
        @click="toggleSidebar">
    </div>
    <div v-if="course" class="content-container">
      <!-- Left Side: Courseware, Ratings, Reviews, and Comments -->
      <div class="left-section">
        <!-- Courseware Section -->
        <div class="courseware-section">
          <h2>Courseware</h2>
          <TeacherCourseware :course-id="courseId" />
        </div>

        <!-- Rating and Review Section -->
        <div class="students-section">
          <StudentsList :courseId="courseId"/>
        </div>

        <!-- Learning Progress Chart -->
        <div class="learning-progress-section">
          <div class="progress-left">
            <h2>Course Ratings</h2>
            <slot name="learning-progress">
              <BarChart
            :contentQualityRating="contentQualityRating"
            :teachingCompetenceRating="teachingCompetenceRating"
            :workloadBalanceRating="workloadBalanceRating"
          />
            </slot>
          </div>

          <div class="progress-right">
            <h2>Coursework Completion</h2>
            <slot name="coursework-progress">
              <DoughnutChart
            :data="courseworkData"
            :colors="courseworkColors"
          />
        </slot>
          </div>
        </div>

        <!-- Comment Section -->
        <div class="comments-section">
          <CommentSection />
        </div>
      </div>

      <!-- Right Side: Course Details and Instructor Information -->
      <div class="right-section">
        <!-- Course Details -->
        <el-card class="course-details" shadow="hover">
          <h2>Course Details</h2>
          <img :src="course?.coverImageUrl || defaultCoverPic" alt="Course Image" class="course-image" />
          <h3><strong>{{ course.courseName }}</strong></h3>
          <p class="description">{{ course?.description || "No description for this course yet."}}</p>
          <p><strong>Category:</strong> {{ course.topic }}</p>
          <p><strong>Rating:</strong> ⭐ {{ course?.averageRating || 0}}</p>
          <p><strong>Likes Count:</strong> ❤️ {{ course?.likesCount || 0 }}</p>
  
        </el-card>

        <el-card class="announcement" shadow="hover">
          <AnnouncementForm/>
        </el-card>

        <el-card class="announcement" shadow="hover">
          <StreamInfo :courseId="courseId"/>
        </el-card>
        <el-card class="announcement" shadow="hover">
    <div class="form-container">
      <h2>Add Student to Course</h2>
      
      <form @submit.prevent="submitForm">
        <div class="form-item">
          <label for="studentEmail">Student's Email</label>
          <input
            id="studentEmail"
            v-model="form.studentEmail"
            type="email"
            placeholder="Enter student's email"
            required
          />
        </div>

        <button type="submit" :disabled="loading" class="submit-button">Add Student</button>
      </form>
      
      <div v-if="message" :class="messageType" class="message-container">
        {{ message }}
      </div>
    </div>
  </el-card>
      </div>
    </div>

    <div v-else>
      <p>You are not authorized to view this course.</p>
    </div>

    <!-- Sidebar Component -->
    <ProfileSidebar
        :user="user"
        :activeIndex="activeIndex"
        :isVisible="isSidebarVisible"
        @menuSelect="handleMenuSelect"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProfileSidebar from './ProfileSidebar.vue';
import TeacherCourseware from "@/components/TeacherCourseware.vue";
import DoughnutChart from './DoughnutChart.vue';
import CommentSection from './CommentSection.vue';
import AnnouncementForm from './AnnouncementForm.vue';
import axiosInstances from '@/services/axiosInstance';
import StudentsList from './StudentsList.vue';
import StreamInfo from "@/components/StreamInfo.vue";
import BarChart from "@/components/BarChart.vue";

const route = useRoute();
const router = useRouter();
const user = ref(null);
const course = ref(null);
const courseId = route.params.courseId;
const userId = localStorage.getItem('userId');

const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const defaultProfilePic = "/assets/Avatars/student.jpg";
const defaultCoverPic = "/assets/Courses/whale.png";

const courseworkData = ref([50, 50]); 
const courseworkColors = ['#f39c12', '#e0e0e0'];
const contentQualityRating = ref(1); 
const teachingCompetenceRating = ref(1); 
const workloadBalanceRating = ref(1);  

const form = ref({
  studentEmail: '',
});
const loading = ref(false);
const message = ref('');
const messageType = ref(''); 


const fetchCourseworkData = async () => {
  try {
    
    const response = await axiosInstances.axiosInstance.get(`/courses/${courseId}/coursework-data`);
    const data = response.data;
    courseworkData.value = [data.courseworkCompletion * 100, 100 - data.courseworkCompletion * 100];
    contentQualityRating.value = data.contentQualityRating;
    teachingCompetenceRating.value = data.teachingCompetenceRating;
    workloadBalanceRating.value = data.workloadBalanceRating;
    
  } catch (error) {
    console.error("Error fetching progress data:", error);
  }
};

const submitForm = async () => {
  loading.value = true;
  console.log('Form:', form.value);
  try {

    const response = await axiosInstances.axiosInstance.post(
      `/instructors/${userId}/courses/${courseId}/add/${form.value.studentEmail}`
    );

    if (response.status === 200) {
      message.value = 'Student added successfully!';
      messageType.value = 'success';
    } else {
      message.value = 'Failed to add student!';
      messageType.value = 'error';
    }
  } catch (error) {
    message.value = 'An error occurred. Please try again.';
    messageType.value = 'error';
  } finally {
    loading.value = false;
  }
};

const fetchCourseDetails = async () => {
    try {
      const response = await axiosInstances.axiosInstance.get(`courses/${courseId}`);
      course.value = response.data;
    } catch (error) {
  
      console.log("Error Details:", error);
      if (error.response && error.response.status === 403) {
        router.push({ name: 'ForbiddenPage' });
      } else {
        console.error("Unexpected error occurred:", error);
      }    
    }
  };
  
const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`instructors/${userId}`);
    user.value = response.data;
  } catch (error) {

    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      router.push({ name: 'ForbiddenPage' });
    } else {
      console.error("Unexpected error occurred:", error);
    }    
  }
};

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

onMounted(async () => {
  await fetchUserData();
  await fetchCourseDetails();
  await fetchCourseworkData();
});
</script>


<style scoped>
.learning-progress-section {
  display: flex;
  justify-content: space-between;
}

.progress-left,
.progress-right {
  width: 48%; 
}

.form-item {
  margin: 30px 0;
}

.form-item label {
  font-weight: bold;
  margin-right: 10px;
}

.submit-button {
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-button:disabled {
  background-color: #ccc;
}

.message-container {
  margin-top: 20px;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
}

.success {
  background-color: #e0f7e0;
  color: green;
}

.error {
  background-color: #f8d7da;
  color: red;
}

.el-menu-demo {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.nav-logo {
  width: 240px;
  height: 90px;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item:hover {
  box-shadow: none !important;
  background-color: transparent !important;
}

.sidebar-toggle {
  margin-left: auto;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.profile-pic-small {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  object-fit: cover;
  overflow: hidden;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
  cursor: pointer;
}

.page-container {
  display: flex;
  flex-direction: column;
  padding: 20px;
  margin: 80px 0 20px 20px;
}

.content-container {
  display: flex;
  gap: 30px;
  justify-content: space-between;
}

.left-section {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-left: 20px;
}

.courseware-section,
.comments-section,
.learning-progress-section,
.students-section {
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding:20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.course-details,
.announcement {
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 5px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.course-image{
  width: 300px;
  height: 160px;
  border-radius: 8px;
}

.comments-section h2,
.students-secLware-section h2,
.course-details h2,
.learning-progress-section h2,
.announcement h2 {
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: bold;
  color: black !important;
}

</style>