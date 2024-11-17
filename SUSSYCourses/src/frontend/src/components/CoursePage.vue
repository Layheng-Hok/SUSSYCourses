<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img class="nav-logo" src="@/assets/Banner/banner2.png" alt="Element logo"/>
      </router-link>
    </el-menu-item>
    <el-menu-item index="1" @click="toggleSidebar" class="sidebar-toggle">
      <img class="profile-pic-small" :src=" user?.profilePic || defaultProfilePic" alt="Profile Picture"/>
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
          <Courseware :course-id="course.id" />
        </div>

        <div class="learning-progress-section">
          <h2>Learning Progress</h2>
          <!-- Slot for the chart -->
          <slot name="learning-progress">
            <DoughnutChart/>
          </slot>
        </div>

        <!-- Rating and Review Section -->
        <div class="ratings-section">
          <h2>Rate and Review</h2>
          <el-rate v-model="userRating" allow-half></el-rate>
          <el-input
            type="textarea"
            class ="review-input"
            v-model="userReview"
            placeholder="Write your review here..."
            rows="4"
          ></el-input>
          <el-button type="primary" class ="submit-button" @click="submitReview">Submit</el-button>
        </div>

        <!-- Comment Section -->
        <div class="comments-section">
          <h2>Comments</h2>
          <CommentSection :student-id="studentId" />
        </div>
      </div>

      <!-- Right Side: Course Details and Instructor Information -->
      <div class="right-section">
        <!-- Course Details -->
        <el-card class="course-details" shadow="hover">
          <h2>Course Details</h2>
          <img :src="course.image" alt="Course Image" class="course-image" />
          <p><strong>{{ course.name }}</strong></p>
          <p class="description">{{ course.description }}</p>
          <p><strong>Category:</strong> {{ course.category }}</p>
          <p><strong>Rating:</strong> ⭐ {{ course.rating }}</p>
        </el-card>

        <!-- Instructor Information -->
        <el-card class="instructor-info" shadow="hover">
          <h2>Instructor Information</h2>
          <div>  <img
                :src="course.instructorImage"
                alt="Instructor Image"
                class="instructor-image">
              </div>
        <div>
           <p><strong>{{ course.instructorName }}</strong></p>
            <p class="bio">{{ course.instructorBio }}</p>
        </div>
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
import { useRouter } from 'vue-router';
import Courseware from './Courseware.vue';
import CommentSection from './CommentSection.vue';
import DoughnutChart from './DoughnutChart.vue';
import ProfileSidebar from './ProfileSidebar.vue';
import axiosInstances from '@/services/axiosInstance';

const router = useRouter();
const user = ref(null); 
const userId = localStorage.getItem('userId');
const userRating = ref(0);
const userReview = ref("");
// const likes = ref(0); // To be fetched
// const liked = ref(false);
const defaultProfilePic = "/assets/Avatars/student.jpg";
const activeIndex = ref('1');
const isSidebarVisible = ref(false);

const course = {
  id: '1',
  name: 'Vue Crash Course',
  description: 'Learn Vue.js from scratch in this comprehensive crash course.',
  image: '/assets/Courses/course.jpg',
  instructorName: 'Jane Doe',
  instructorImage: '/assets/Avatars/instructor.jpg',
  instructorBio: 'A passionate Vue.js developer and instructor with over 5 years of experience.',
  likes: 123,
  rating: 4.5,
  category: 'Web Development',
};

const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`student/profile/${userId}`);
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

const submitReview = () => {
  if (userRating.value && userReview.value.trim()) {
    alert(
      `Thank you for your review! Rating: ${userRating.value}, Review: "${userReview.value}"`
    );
    userRating.value = 0;
    userReview.value = "";
  } else {
    alert("Please provide a rating and review before submitting.");
  }
};

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

onMounted(fetchUserData);
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
  width: 65px;
  height: 65px;
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
.ratings-section,
.comments-section,
.learning-progress-section {
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding:20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.course-details,
.instructor-info {
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

.instructor-image {
  width: 150ox;
  height: 150px;
  border-radius: 8px;
}

.comments-section h2,
.ratings-section h2,
.courseware-section h2,
.course-details h2,
.instructor-info h2,
.learning-progress-section h2 {
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: bold;
  color: black !important;
}

.ratings-section .el-rate {
  margin-bottom: 10px;
}

.review-input {
  width: 800px;
  font-size: 16px;
}

.submit-button{
  background: #007bff;
  color: #fff;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  font-size: 16px;
  width: 100px;
  height: auto;
  margin-top: 10px;
}

.description, .bio {
  margin: 20px 100px;
  text-align: justify;
  font-size: 16px;
  line-height: 1.6;
}
</style>