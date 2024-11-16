<template>
  <!-- Top Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo.png" alt="Element logo" style="width: 60px;" />
      </router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/">Log Out</router-link>
    </el-menu-item>
  </el-menu>

  <div class="course-page" v-if="course">
    <!-- Course Description Section -->
    <!-- <el-card class="course-description" shadow="hover">
      <h2>Course Description</h2>
      <img :src="course.image" alt="Course Image" class="course-image" />
      <p class="course-title"><strong>{{ course.name }}</strong></p>
      <p class="description">{{ course.description }}</p> -->

      <!-- Like Button and Total Likes -->
      <!-- <div class="like-section">
        <el-button type="primary" icon="el-icon-thumb" @click="likeCourse">
          <span v-if="!liked">Like</span>
          <span v-else>Liked</span>
        </el-button>
        <span>Total Likes: {{ likes }}</span>
      </div>
    </el-card> -->

    <!-- Instructor Info Section -->
    <el-card class="instructor-info" shadow="hover">
      <h2>Instructor Information</h2>
      <!-- <el-row>
        <el-col :span="4">
          <img :src="course.instructorImage" alt="Instructor Image" class="instructor-image" />
        </el-col>
        <el-col :span="20">
          <p><strong>{{ course.instructorName }}</strong></p>
          <p class="bio">{{ course.instructorBio }}</p>
        </el-col>
      </el-row> -->
    </el-card>

    <!-- Courseware Section -->
    <Courseware :course-id="course[id]" />

    <!-- Comment Section -->
    <CommentSection :student-id="studentId"/>
  </div>

  <div v-else>
    <p>Loading course information...</p>
  </div>
</template>

<script setup>
// import { ref, onMounted } from 'vue';
import { ref } from 'vue';

// import { useRoute } from 'vue-router';
import Courseware from './Courseware.vue';
import CommentSection from './CommentSection.vue';

// Variables
const studentId = ref('1'); // Example student ID
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
  image: '/assets/Courses/vue-course.jpg',
  instructorName: 'Jane Doe',
  instructorImage: '/assets/Instructors/jane-doe.jpg',
  instructorBio: 'A passionate Vue.js developer and instructor with over 5 years of experience.',
  likes: 123,
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
.course-page {
  padding: 20px;
  margin: 20px auto;
  max-width: 1200px;
}

.course-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-top: 10px;
}

.course-description, .instructor-info, .course-content {
  margin-top: 40px;
}

.like-section {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
}

.chapter {
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