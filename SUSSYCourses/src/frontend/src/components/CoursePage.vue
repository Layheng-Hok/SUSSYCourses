<template>
  <!-- Navigation Menu -->
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <el-menu-item index="0">
      <router-link to="/">
        <img src="@/assets/logo.png" alt="Element logo" fit="contain" style="width: 60px;" />
      </router-link>
    </el-menu-item>
    <el-menu-item index="1">
      <router-link to="/">Log Out</router-link>
    </el-menu-item>
  </el-menu>

  <div class="course-page">
    <!-- Course Description Section -->
    <el-card class="course-description" shadow="hover">
      <h2>Course Description</h2>
      <img src="/assets/Courses/course.jpg" alt="Course Image" fit="cover" />
      <p><strong>{{ courseName }}</strong></p>
      <p class="description">{{ courseDescription }}</p>
      
      <!-- Like Button and Total Likes -->
      <div class="like-section">
        <el-button type="primary" icon="el-icon-thumb" @click="likeCourse">Like</el-button>
        <span>Total Likes: {{ likes }}</span>
      </div>
    </el-card>

    <!-- Instructor Info Section -->
    <el-card class="instructor-info" shadow="hover">
      <h2>Instructor Information</h2>
      <el-row>
        <el-col :span="4">
          <img src= "/assets/Avatars/instructor.jpg" alt="Instructor Image" fit="cover" />
        </el-col>
        <el-col :span="20">
          <p><strong>{{ instructorName }}</strong></p>
          <p class="bio">{{ instructorBio }}</p>
        </el-col>
      </el-row>
    </el-card>

    <!-- Course Content Section -->
    <el-card class="course-content" shadow="hover">
      <h2>Course Content</h2>
      <el-collapse>
        <el-collapse-item title="Teaching Chapters" name="1">
          <div v-for="chapter in teachingChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <a :href="`/assets/Materials/${material.title}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" /> {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Homework Chapters" name="2">
          <div v-for="chapter in homeworkChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <a :href="`/assets/Materials/${material.title}`" target="_blank">
                  <component :is="materialIcon(material.type)" style="width: 1em; height: 1em; margin-right: 5px;" /> {{ material.title }}
                </a>
              </el-list-item>
            </el-list>
          </div>
        </el-collapse-item>

        <el-collapse-item title="Project Chapters" name="3">
          <div v-for="chapter in projectChapters" :key="chapter.name" class="chapter">
            <h3>{{ chapter.name }}</h3>
            <el-list>
              <el-list-item v-for="material in chapter.materials" :key="material.title">
                <a :href="`/assets/Materials/${material.title}`" target="_blank">
                <component style="width: 1em; height: 1em" :is="materialIcon(material.type)" /> {{ material.title }}
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
      <el-input class = "comment-input" type="textarea" placeholder="Add a comment..." v-model="newComment" rows="3" />
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
import { ref } from 'vue';
import { Document, DataBoard, VideoCamera, Files } from '@element-plus/icons-vue';

const courseName = ref("The Web Developer Bootcamp");
// const courseImage = ref("@/assets/tm1.png");
const courseDescription = ref(`Now with over 10 hours of React content.  Just updated on May 15th, 2023.

Massive new React "expansion pack" covers: React basics, JSX, props, state, Vite, MaterialUI, hooks, useEffect, React design patterns, and more.

Hi! Welcome to the brand new version of The Web Developer Bootcamp, Udemy's most popular web development course.  This course was just completely overhauled to prepare students for the 2023 job market, with over 60 hours of brand new content. This is the only course you need to learn web development. There are a lot of options for online developer training, but this course is without a doubt the most comprehensive and effective on the market. 
 This is the only course you need to learn web development.`);
const instructorName = ref("Dr. Angela Yu");
// const instructorImage = ref("/assets/Avatar/instructor.jpg");
const instructorBio = `
Hi! I'm Angela Yu. I'm a developer with a serious love for teaching. I've spent the last few years teaching people to program at 2 different immersive bootcamps where I've helped hundreds of people become web developers and change their lives. My graduates work at companies like Google, Salesforce, and Square.
Most recently, I led Galvanize's SF's 6 month immersive program as Lead Instructor and Curriculum Director. 
Join me on this crazy adventure!
`;

const teachingChapters = ref([{ name: "Chapter 1", materials: [{ title: "Introduction.pdf", type: "pdf" }] ,
  materials2: [{ title: "Introduction.pdf", type: "pdf" }] },
  { name: "Chapter 2", materials: [{ title: "Introduction.pdf", type: "pdf" }] }],
);
const homeworkChapters = ref([{ name: "Homework 1", materials: [{ title: "Assignment 1.pdf", type: "pdf" }] }]);
const projectChapters = ref([{ name: "Project 1", materials: [{ title: "Project Plan.pptx", type: "pptx" }] }]);

const comments = ref([
  { id: 1, text: "This course is amazing!", author: "Alice", timestamp: "2024-10-29 10:00 AM" },
  { id: 2, text: "Looking forward to the next lesson!", author: "Bob", timestamp: "2024-10-29 11:00 AM" }
]);
const newComment = ref("");

const likes = ref(0);
const likeCourse = () => {
  likes.value += 1;
};

const materialIcon = (type) => {
  switch (type) {
    case 'pdf': return Document;
    case 'pptx': return DataBoard; 
    case 'video': return VideoCamera; 
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
.description, .bio {
  margin: 20px 100px;
  text-align: justify;
  font-size: 16px;
  line-height: 1.6;
}

.course-page {
  padding: 20px;
  margin: 20px auto;
  max-width: 1200px;
}

.course-description, .instructor-info, .course-content, .comment-section {
  margin-top: 40px;
}
.like-section {
  margin: auto ;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;

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
</style>
