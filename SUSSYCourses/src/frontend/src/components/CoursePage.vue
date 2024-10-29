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
    <el-card class="course-description" shadow="hover">
      <h2>Course Description</h2>
      <img :src="course.image" alt="Course Image" class="course-image" />
      <p class="course-title"><strong>{{ course.name }}</strong></p>
      <p class="description">{{ course.description }}</p>

      <!-- Like Button and Total Likes -->
      <div class="like-section">
        <el-button type="primary" icon="el-icon-thumb" @click="likeCourse">
          <span v-if="!liked">Like</span>
          <span v-else>Liked</span>
        </el-button>
        <span>Total Likes: {{ likes }}</span>
      </div>
    </el-card>

    <!-- Instructor Info Section -->
    <el-card class="instructor-info" shadow="hover">
      <h2>Instructor Information</h2>
      <el-row>
        <el-col :span="4">
          <img :src="course.instructorImage" alt="Instructor Image" class="instructor-image" />
        </el-col>
        <el-col :span="20">
          <p><strong>{{ course.instructorName }}</strong></p>
          <p class="bio">{{ course.instructorBio }}</p>
        </el-col>
      </el-row>
    </el-card>

   <!-- Course Content Section with Interactive Media -->
   <el-card class="course-content" shadow="hover">
      <h2>Course Content</h2>
      <el-collapse>
        <el-collapse-item title="Teaching Chapters" name="1">
          <div v-for="chapter in course.teachingChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <div v-if="material.type === 'mp4'" class="video-container">
                  <video controls :src="`/assets/Materials/${material.title}`" width="50%"></video>
                </div>
                <div v-else>
                  <a :href="`/assets/Materials/${material.title}`" target="_blank">
                    <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" /> 
                    {{ material.title }}
                  </a>
                </div>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Homework Chapters" name="2">
          <div v-for="chapter in course.homeworkChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <a :href="`/assets/Materials/${material.title}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" /> 
                  {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Project Chapters" name="3">
          <div v-for="chapter in course.projectChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <a :href="`/assets/Materials/${material.title}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" /> 
                  {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <!-- Comment Section -->
    <el-card class="comment-section" shadow="hover">
      <h2>Comments ({{ comments.length }})</h2>
      <el-input class="comment-input" type="textarea" placeholder="Add a comment..." v-model="newComment" rows="3" />
      <el-button type="primary" @click="submitComment">Submit</el-button>

      <div class="comments">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <p>
            <strong>{{ comment.author }}:</strong> {{ comment.text }}
            <span class="timestamp">{{ comment.timestamp }}</span>
          </p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { Document, DataBoard, VideoCamera, Files } from '@element-plus/icons-vue';

const route = useRoute();
const course = ref(null);
const likes = ref(0);
const newComment = ref("");
const comments = ref([]);
const liked = ref(false);

// Fetch actual data here 
const coursesData = [
  {
    id: '1',
    name: "React Crash Course",
    description: "This is a comprehensive web development course.",
    image: "/assets/Courses/course.jpg",
    instructorName: "Dr. Angela Yu",
    instructorImage: "/assets/Avatars/instructor.jpg",
    instructorBio: "Developer with a love for teaching.",
    teachingChapters: [
      { name: "Chapter 1", materials: [{ title: "Lecture 1.mp4", type: "mp4" }, { title: "Introduction.pdf", type: "pdf" }] },
    ],
    homeworkChapters: [{ name: "Homework 1", materials: [{ title: "Assignment 1.pdf", type: "pdf" }] }],
    projectChapters: [{ name: "Project 1", materials: [{ title: "Project Plan.pptx", type: "pptx" }] }],
  },
  {
    id: '2',
    name: "Vue Crash Course",
    description: "This is a comprehensive web development course.",
    image: "/assets/Courses/course2.jpg",
    instructorName: "Mr. Jack Bruh",
    instructorImage: "/assets/Avatars/student.jpg",
    instructorBio: "Developer with a love for teaching.",
    teachingChapters: [
      { name: "Chapter 1", materials: [ { title: "Introduction.pdf", type: "pdf" }] },
    ],
    homeworkChapters: [{ name: "Homework 1", materials: [{ title: "Assignment 1.pdf", type: "pdf" }] }],
    projectChapters: [{ name: "Project 1", materials: [{ title: "Project Plan.pptx", type: "pptx" }] }],
  }
];

onMounted(() => {
  const courseId = route.params.courseId;
  course.value = coursesData.find(c => c.id === courseId);
  if (!course.value) {
    console.error("Course not found!");
  }
});

const likeCourse = () => {
  likes.value += 1;
  liked.value = true;
};

const materialIcon = (type) => {
  switch (type) {
    case 'pdf': return Document;
    case 'pptx': return DataBoard; 
    case 'mp4': return VideoCamera; 
    default: return Files; 
  }
};

const submitComment = () => {
  if (newComment.value) {
    comments.value.push({
      id: Date.now(),
      text: newComment.value,
      author: "Current User",
      timestamp: new Date().toLocaleString()
    });
    newComment.value = "";
  }
};
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

.course-description, .instructor-info, .course-content, .comment-section {
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

.comments {
  margin-top: 15px;
}

.comment-item {
  margin-bottom: 10px;
  padding: 10px;
  border-bottom: 1px solid #ccc;
  text-align: left;
}

.comment-input {
  margin-bottom: 20px;
}

.timestamp {
  font-size: 12px;
  color: #888;
  float: right;
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