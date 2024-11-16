<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo2.png" alt="Element logo" style="width: 60px;" />
      </router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/">Log Out</router-link>
    </el-menu-item>
  </el-menu>

  <div class="page-container">
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
  </div>
</template>

<script setup>

import { ref } from 'vue';
import Courseware from './Courseware.vue';
import CommentSection from './CommentSection.vue';
import DoughnutChart from './DoughnutChart.vue';

const studentId = ref('1'); // Example student ID
const userRating = ref(0);
const userReview = ref("");
// const courseId = ref(null);
// const course = ref(null);
// const likes = ref(0); // To be fetched
// const liked = ref(false);

// Route handling
// const route = useRoute();
// courseId.value = route.params.courseId;

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

// Fetch course data
// onMounted(async () => {
//   if (!courseId.value) {
//     console.error("No course ID provided!");
//     return;
//   }
//   try {
//     const response = await fetch(`/api/courses/${courseId.value}`);
//     if (!response.ok) throw new Error("Failed to fetch course data.");
//     course.value = await response.json();
//     likes.value = course.value.likes || 0; // Assume the course object has a likes property
//   } catch (error) {
//     console.error("Error fetching course data:", error);
//   }
// });

// Like course function
// const likeCourse = async () => {
//   if (!liked.value) {
//     likes.value += 1;
//     liked.value = true;
//   }
  // try {
  //   const response = await fetch(`/api/courses/${courseId.value}/like`, { method: 'POST' });
  //   if (response.ok) {
  //     likes.value += 1;
  //     liked.value = true;
  //   } else {
  //     console.error("Failed to like course");
  //   }
  // } catch (error) {
  //   console.error("Error liking the course:", error);
  // }
// };
</script>


<style scoped>
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

.el-menu-demo img {
  width: 60px;
  height: auto;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-menu-demo .el-menu-item {
  font-size: 18px;
  color: black;
  background-color: transparent;
  transition: color 0.3s;
  font-family: 'Aptos Narrow', sans-serif;
}

.el-menu-demo {
  background-color: white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 75px;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.description, .bio {
  margin: 20px 100px;
  text-align: justify;
  font-size: 16px;
  line-height: 1.6;
}
</style>