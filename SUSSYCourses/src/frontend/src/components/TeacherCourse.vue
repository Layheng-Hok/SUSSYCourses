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
          <h2>Enrolled students</h2>
          <StudentsList />
        </div>

        <!-- Learning Progress Chart -->
        <div class="learning-progress-section">
          <h2>Course rating statistics</h2>
          <slot name="learning-progress">
            <DoughnutChart/>
          </slot>
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
      </div>
    </div>

    <div v-else>
      <p>Loading course information...</p>
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

const fetchCoursewareDetails = async () => {
  try {
    console.log(userId)
    console.log(courseId)
    const response = await axiosInstances.axiosInstance.get(`/users/${userId}/courses/${courseId}/coursewares`);

    console.log(response)
    course.value = response.data;
  } catch (error) {
    console.log("Error Details:", error);
    if (error.response && error.response.status === 403) {
      await router.push({name: 'ForbiddenPage'});
    }
    else if (error.response && error.response.status === 404) {
      await router.push({name: 'ForbiddenPage'});
    }

    else {
      console.error("Unexpected error occurred:", error);
    }
  }
};

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

onMounted(async () => {
  await fetchUserData();
  await fetchCoursewareDetails();
});
</script>


<style scoped>
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