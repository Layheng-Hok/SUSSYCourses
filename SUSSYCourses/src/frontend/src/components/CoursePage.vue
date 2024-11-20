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
          <Courseware :course-id="courseId" />
        </div>

        <!-- Learning Progress Chart -->
        <div class="learning-progress-section">
          <h2>Learning Progress</h2>
          <slot name="learning-progress">
            <DoughnutChart/>
          </slot>
        </div>

        <!-- Rating and Review Section -->
        <div class="ratings-section">
          <RatingAndReview />
        </div>

        <!-- Comment Section -->
        <div class="comments-section">
          <h2>Comments</h2>
          <CommentSection :student-id="userId" />
        </div>
      </div>

      <!-- Right Side: Course Details and Instructor Information -->
      <div class="right-section">
        <!-- Course Details -->
        <el-card class="course-details" shadow="hover">
          <h2>Course Details</h2>
          <img :src="course?.coverImageUrl || defaultCoverPic" alt="Course Image" class="course-image" />
          <p><strong>{{ course.courseName }}</strong></p>
          <p class="description">{{ course.description }}</p>
          <p><strong>Category:</strong> {{ course.topic }}</p>
          <p><strong>Rating:</strong> ⭐ {{ course.averageRating }}</p>
          <p><strong>Likes Count:</strong> ❤️ {{ course.likesCount }}</p>
           <!-- Like Button -->
          <el-button
              type="text"
              class="like-button"
              @click="incrementLikes"
            >
            <i :class="['fa-heart', isLiked ? 'fas liked' : 'far']"></i>
            {{ isLiked ? ' Unlike' : ' Like' }}
            </el-button>
        </el-card>
        <!-- <p><strong>Enrolled Students:</strong> {{ course.numStudentsEnrolled }}</p> -->
        
        <!-- Instructor Information -->
        <el-card class="instructor-info" shadow="hover">
          <h2>Instructor Information</h2>
          <div>  <img
                :src="course?.instructorImage || defaultProfilePic"
                alt="Instructor Image"
                class="instructor-image">
              </div>
        <div>
           <p><strong>{{ course.teacherName }}</strong></p>
            <p class="bio">{{ course?.instructorBio || " to be fetched" }}</p>
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
import { useRoute, useRouter } from 'vue-router';
import ProfileSidebar from './ProfileSidebar.vue';
import Courseware from './Courseware.vue';
import RatingAndReview from './RatingAndReview.vue';
import DoughnutChart from './DoughnutChart.vue';
import CommentSection from './CommentSection.vue';
import axiosInstances from '@/services/axiosInstance';
import '@fortawesome/fontawesome-free/css/all.css';
import '@fortawesome/fontawesome-free/js/all.js';


const route = useRoute();
const router = useRouter();
const user = ref(null); 
const userId = localStorage.getItem('userId');
const course = ref(null);
const courseId = route.params.courseId;
const isLiked = ref(false); //
const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const defaultProfilePic = "/assets/Avatars/student.jpg";
const defaultCoverPic = "/assets/Banner/whale.jpg";

const incrementLikes = async () => {
  isLiked.value = !isLiked.value;
  course.value.likesCount += isLiked.value ? 1 : -1;

  // try {
  //   await axiosInstances.post('/course/like', {
  //     courseId: course.value.id,
  //     isLiked: isLiked.value,
  //   });
  // } catch (error) {
  //   console.error('Failed to update likes:', error);
  //   isLiked.value = !isLiked.value;
  //   likesCount.value += isLiked.value ? -1 : 1;
  // }
};

const fetchUserData = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}`);
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

const fetchCourseDetails = async () => {
  try {
    const response = await axiosInstances.axiosInstance.get(`students/${userId}/courses/${courseId}`);
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

const toggleSidebar = () => {
  isSidebarVisible.value = !isSidebarVisible.value;
};

onMounted(async () => {
  await fetchUserData();
  await fetchCourseDetails();
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

.description, .bio {
  margin: 20px 100px;
  text-align: justify;
  font-size: 16px;
  line-height: 1.6;
}

.like-button i {
  color: #999; /* Default icon color */
  font-size: 24px; /* Adjust size as needed */
  cursor: pointer;
  transition: color 0.3s ease, transform 0.3s ease; /* Smooth color and transform */
}

.like-button i.liked {
  color: #ff5a5f; /* Highlight color for liked state */
  animation: heart-pop 0.3s ease; /* Pop animation */
}

@keyframes heart-pop {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}

</style>