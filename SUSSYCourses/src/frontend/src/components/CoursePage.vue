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
           <!-- Like Button -->
          <el-button
              type="text"
              class="like-button"
              @click="incrementLikes"
            >
            <span :class="{ liked: isLiked }">{{ isLiked ? '❤️ Unlike' : '🤍 Like' }}</span>
            </el-button>
        </el-card>
        <!-- <p><strong>Enrolled Students:</strong> {{ course.numStudentsEnrolled }}</p> -->
        
        <!-- Instructor Information -->
        <el-card class="instructor-info" shadow="hover">
          <h2>Instructor Information</h2>
          <div>  <img
                :src="course?.teacherProfilePictureUrl || defaultTeacherProfilePic"
                alt="Instructor Image"
                class="instructor-image">
              </div>
        <div>
           <p><strong>{{ course.teacherName }}</strong></p>
            <p class="bio">{{ course?.teacherBio || "The teacher doesn't want to introduce him/herself" }}</p>
            <el-button type="primary" class="tts-button" @click="readBio('en')">
            🔊 Read Bio (English)
          </el-button>
          <el-button type="success" class="tts-button" @click="readBio('zh')">
            🔊 Read Bio (Chinese)
          </el-button>
          <el-button type="warning" class="tts-button" @click="readBio('ja')">
            🔊 Read Bio (Japanese)
          </el-button>
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

const route = useRoute();
const router = useRouter();
const user = ref(null); 
const userId = localStorage.getItem('userId');
const course = ref(null);
const courseId = route.params.courseId;
const isLiked = ref(false);

const activeIndex = ref('1');
const isSidebarVisible = ref(false);
const defaultProfilePic = "/assets/Avatars/student.jpg";
const defaultTeacherProfilePic = "/assets/Avatars/instructor.jpg";
const defaultCoverPic = "/assets/Courses/whale.png";

import axios from 'axios';

const readBio = async (language) => {
  const bioText = course.value?.teacherBio || "No bio available for this instructor.";
  let translatedText = bioText;

  if (language !== 'en') {
    translatedText = await translateText(bioText, language);
  }

  const utterance = new SpeechSynthesisUtterance(translatedText);
  utterance.rate = 1; 
  utterance.pitch = 1;
  utterance.volume = 1;

  const voices = window.speechSynthesis.getVoices();
  if (language === 'zh') {
    utterance.voice = voices.find((voice) => voice.lang === 'zh-CN'); 
  } else if (language === 'ja') {
    utterance.voice = voices.find((voice) => voice.lang === 'ja-JP'); 
  } else {
    utterance.voice = voices.find((voice) => voice.lang === 'en-US'); 
  }

  window.speechSynthesis.speak(utterance);
};

const translateText = async (text, targetLanguage) => {
  const apiKey = process.env.VUE_APP_GOOGLE_API_KEY;

  try {
    const response = await axios.post(
      `https://translation.googleapis.com/language/translate/v2`,
      {
        q: text,
        target: targetLanguage,
      },
      {
        params: {
          key: apiKey, 
        },
      }
    );
    return response.data.data.translations[0].translatedText;
  } catch (error) {
    console.error("Translation failed:", error.response?.data || error.message);
    return text;
  }
};

const incrementLikes = async () => {
  isLiked.value = !isLiked.value;
  course.value.likesCount += isLiked.value ? 1 : -1;
  try {
    
    await axiosInstances.axiosInstance.put(`students/${userId}/courses/${courseId}/like-unlike`);
  } catch (error) {
    console.error('Failed to update likes:', error);
    isLiked.value = !isLiked.value;
    course.value.likesCount += isLiked.value ? -1 : 1;
  }
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
    isLiked.value = course.value.liked;
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

.tts-button {
  margin: 10px 5px 0 0;
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  cursor: pointer;
}

.tts-button[type="success"] {
  background-color: #67c23a;
  color: #fff;
}

.tts-button[type="warning"] {
  background-color: #e6a23c;
  color: #fff;
}

.course-image{
  width: 300px;
  height: 160px;
  border-radius: 8px;
}

.course-image:hover {
  transform: scale(1.05) translateY(-5px);
  transition: transform 0.3s ease-out;
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

.like-button span {
  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s ease, transform 0.3s ease;
}

.like-button span.liked {
  color: #ff5a5f; 
  animation: heart-pop 0.3s ease;
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